/*	adialog - Android part
	Copyright 2015-2016 Rivoreo

	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
*/

package org.rivoreo.adialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RequestReceiver extends BroadcastReceiver implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
	public RequestReceiver() {
	}

	private Uri pipe_path;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d("action", action);
		if(action.equals("question") || action.equals("information") || action.equals("warning") || action.equals("error")) {

			// Use a named pipe to return data to the shell
			pipe_path = intent.getData();
			if(pipe_path == null) {
				setResultData("Use data URI to pass the pipe path");
				setResultCode(255);
				return;
			}
			// XXX: I can't detect if the file is a named pipe or not
			// java.nio.file.

			AlertDialog dialog = (new AlertDialog.Builder(context)).create();
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			//dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
			//dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
			//AlertDialog(new AlertDialog.Builder()).setMessage()
			//Dialog dialog = new Dialog(context);
			dialog.setOnCancelListener(this);
			String title = intent.getStringExtra("title");
			String text = intent.getStringExtra("text");
			//if(text == null) throw new IllegalArgumentException("'text' is required");
			if(text == null) {
				setResultData("'text' is required");
				setResultCode(255);
				return;
			}
			if(title != null) dialog.setTitle(title);
			dialog.setMessage(text);
			//boolean double_button = intent.getBooleanExtra("double-button", false);
			//dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok), this);
			//if(double_button) dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", this);
			int button_count = intent.getIntExtra("buttons", 1);
			Log.d("button_count", String.valueOf(button_count));
			switch(button_count) {
				//case 3:
				//	dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NEUTRAL", this);
				case 2:
					dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), this);
				case 1:
					dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok), this);
				case 0:
					break;
				default:
					setResultData("error: Invalid argument for 'buttons'");
					setResultCode(255);
					return;
			}
			//dialog.setIcon();
			dialog.show();
/* Can't block onReceive
			try {
				Looper.loop();
			} catch(RuntimeException e) {
				//e.printStackTrace();
				//return;
			}*/

			setResultData("Dialog should now shown, I will write results into " + pipe_path.toString());
			setResultCode(1);

/* It is OK to use AlertDialog, no need to startActivity MessageBox
			if(intent.getStringExtra("text") == null) {
				setResultData("'text' is required");
				setResultCode(0);
				return;
			}
			Intent messagebox_intent = new Intent(context, MessageBox.class);
			messagebox_intent.setData(pipe_path);
			messagebox_intent.putExtras(intent);
			messagebox_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(messagebox_intent);
			setResultCode(1);
*/

		} else {
			//Log.w("onReceive", String.format("Unknown action '%s'", action));
			//throw new UnsupportedOperationException(String.format("Unknown action '%s'", action));
			setResultData(String.format("Unknown action '%s'", action));
			setResultCode(255);
		}
	}

	public void write_pipe(int code) {
		try {
			FileOutputStream os = new FileOutputStream(pipe_path.toString());
			os.write(String.valueOf(code).getBytes());
			os.close();
		} catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		Log.d("method", String.format("org.rivoreo.adialog.RequestReceiver::onClick(%s, %d)", dialog.toString(), which));
		//setResultCode(which == AlertDialog.BUTTON_POSITIVE ? 1 : 0);
		//throw new RuntimeException("close");
		write_pipe(which == AlertDialog.BUTTON_POSITIVE ? 0 : 1);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		//setResultCode(0);
		//throw new RuntimeException("canceled");
		write_pipe(1);
	}

}
