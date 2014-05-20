package com.example.android_39_sqliteregistrationsys;

import com.example.android_39_sqliteregistrationsys.DBHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AuthorizationMainActivity extends Activity implements OnClickListener {
	
	public final static String EXTRA_MESSAGE = "com.example.android_39_sqliteregistrationsys.MESSAGE";
	String message;
	final String LOG_TAG = "myLogs";
	
	TextView tVLogin;
	EditText eTLogin, eTPassword;
	Button btnLogin, btnRegistration;
	
	DBHelper dbHelper;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorization_main);
		
		tVLogin = (TextView) findViewById(R.id.tVLogin);
		eTLogin = (EditText) findViewById(R.id.eTLogin);
		eTPassword = (EditText) findViewById(R.id.eTPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegistration = (Button) findViewById(R.id.btnRegistration);
		btnLogin.setOnClickListener(this);
		btnRegistration.setOnClickListener(this);
		
		dbHelper = new DBHelper(this);
				
	}

	@Override
	public void onClick(View v) {
		String id = v.getId();
		
		switch (id) {
		case R.id.btnLogin:
			login();
			break;
		
		case R.id.btnRegistration:
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			Log.d(LOG_TAG, "Button Register was pressed");
		break;
		}				
	}
	
	@Override
	private void login() {
		String loginEnt = eTLogin.getText().toString().toLowerCase();
		String passwordEnt = eTPassword.getText().toString();
		String selection = null;
		String[] columns = null;
		String[] selectionArgs = null;
		
		db = dbHelper.getWritableDatabase();
		
		Cursor c = null;
		
		Log.d(LOG_TAG, "Button login was pressed");
		selection = "login = ?";
		selectionArgs = new String[] { loginEnt };
		columns = new String[] { "password" };
		c = db.query("sqliteregistrationsys", columns, selection, selectionArgs, null, null, null);
					
		if (c == null) {
			Log.d(LOG_TAG, "Cursor is null"); 
		} else {
			Log.d(LOG_TAG, "Cursor is not null");
			Log.d(LOG_TAG, "Number of rows " + c.getCount());
			if (c.getCount() == 0) {
				tVLogin.setText("There is no such user");
				tVLogin.setTextColor(getResources().getColor(R.color.opaque_red));
			}
			else {	
				if (c.moveToFirst()) {
					String passwordCheck = "";
					
					do {
						for (String cn : c.getColumnNames()) {
							passwordCheck = c.getString(c.getColumnIndex(cn));
						}
						Log.d(LOG_TAG, passwordCheck + passwordEnt);
					} while (c.moveToNext());
					
					if (passwordEnt.equalsIgnoreCase(passwordCheck)){
						Log.d(LOG_TAG, "You have entered correct password!");
						Intent intent = new Intent(this, GreetingsActivity.class);
						message = loginEnt;
						intent.putExtra(EXTRA_MESSAGE, message);
						startActivity(intent);
					}
					else
						Log.d(LOG_TAG, "Login or Password is invalid, try again!");
				}
			}
		} 
		
		dbHelper.close();
	}
}
