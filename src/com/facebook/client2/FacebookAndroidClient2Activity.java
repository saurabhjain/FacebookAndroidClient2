package com.facebook.client2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FacebookAndroidClient2Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem menu_item) {
    	switch (menu_item.getItemId()) {
    	case R.id.facebook:
    		Intent postOnFacebookWallIntent = new Intent(this, ShareOnFacebook.class);
    		postOnFacebookWallIntent.putExtra("facebookMessage", "test post...");
    		startActivity(postOnFacebookWallIntent);
    		return true;
    	default:
        	return super.onOptionsItemSelected(menu_item);
    	}
    }
}