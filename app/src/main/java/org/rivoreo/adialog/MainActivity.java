/*	adialog - Android part
	Copyright 2015-2016 Rivoreo

	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
*/

package org.rivoreo.adialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//finish();
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String text = intent.getStringExtra("text");
		if(text == null) {
			this.finish();
		}
		//startActivityForResult(); we need to call this from shell
		//setTheme(R.style.Theme_Transparent);
		//this.onCreateDialog()
		//setContentView(R.layout.activity_main);
		Dialog dialog = new Dialog(this);
		//AlertDialog dialog = (new AlertDialog.Builder(this)).create();
		//dialog.
		dialog.show();
		//finish();
		Log.d("method", "org.rivoreo.adialog.MainActivity::onCreate(Bundle)");
	}

/*
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		//return super.onCreateDialog(id, args);
		Log.d("method", String.format("onCreateDialog(%d, %s", id, args.toString()));
		return null;
	}
*/
}
