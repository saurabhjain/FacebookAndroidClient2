package com.facebook.client2;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class ShareOnFacebook extends Activity{

	private static final String APP_ID = "297254660288680";
	private static final String[] PERMISSIONS = new String[] {"publish_stream"};

	private static final String TOKEN = "access_token";
        private static final String EXPIRES = "expires_in";
        private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private String messageToPost;

	public boolean saveCredentials(Facebook facebook) {
        	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        	editor.putString(TOKEN, facebook.getAccessToken());
        	editor.putLong(EXPIRES, facebook.getAccessExpires());
        	return editor.commit();
    	}

    	public boolean restoreCredentials(Facebook facebook) {
        	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        	facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        	return facebook.isSessionValid();
    	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// =============================================================================
		// Commenting the setting up of content view for dialog as not making use of it.
		// =============================================================================
		//setContentView(R.layout.dialog); 

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null){
			facebookMessage = "Test wall post";
		}
		messageToPost = facebookMessage;
		
		// ===========================================================================================
		// Creating the bundle and passing it to facebook dialog method on the launch of the activity.
		// ===========================================================================================
		/*
		 * Note: Posting to facebook wall has been taken care by calling this dialog function only,
		 * and I am not making use of structural functions defined below.
		 */
		Bundle parameters = new Bundle();
    	parameters.putString("message", messageToPost);
    	facebook.dialog(this, "stream.publish", parameters, new WallPostDialogListener());
	}

	public void doNotShare(View button){
		finish();
	}
	public void share(View button){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
		}
	}

	public void loginAndPostToWall(){
		 facebook.authorize(this, PERMISSIONS, new LoginDialogListener());
	}

	public void postToWall(String message){
			Bundle parameters = new Bundle();
        	parameters.putString("message", message);
        	facebook.dialog(this, "stream.publish", parameters, new WallPostDialogListener());
	}

	class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	saveCredentials(facebook);
	    	if (messageToPost != null){
			postToWall(messageToPost);
		}
	    }
	    public void onFacebookError(FacebookError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onError(DialogError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onCancel() {
	    	showToast("Authentication with Facebook cancelled!");
	        finish();
	    }
	}

	class WallPostDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
            		final String postId = values.getString("post_id");
            		if (postId != null) {
            		showToast("Message posted to your facebook wall!");
            	} else {
            		showToast("Wall post cancelled!");
            	}
            	finish();
        	}
		public void onFacebookError(FacebookError e) {
			showToast("Failed to post to wall!");
			e.printStackTrace();
			finish();
		}
		public void onError(DialogError e) {
			showToast("Failed to post to wall!");
			e.printStackTrace();
			finish();
		}
		public void onCancel() {
			showToast("Wall post cancelled!");
			finish();
		}
    	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}