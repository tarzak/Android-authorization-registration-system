package com.example.android_39_sqliteregistrationsys;

import java.util.Random;

import com.example.android_39_sqliteregistrationsys.DBHelper;
import android.app.Activity;
import android.content.ContentValues;
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


public class RegistrationActivity extends Activity implements OnClickListener {
	final String LOG_TAG = "myLogs";
	int i1, i2;
	
	TextView tVRegTitle;
	EditText eTLoginReg, eTPasswordReg, eTEmail, 
			 eTPersonName, eTAge, eTSkype, eTCaptcha;
	Button btnOk;
	
	DBHelper dbHelper;
	SQLiteDatabase db;
		
	Random randomNumber = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		eTLoginReg = (EditText) findViewById(R.id.eTLoginReg);
		eTPasswordReg = (EditText) findViewById(R.id.eTPasswordReg);
		eTEmail = (EditText) findViewById(R.id.eTEmail);
		eTPersonName = (EditText) findViewById(R.id.eTPersonName);
		eTAge = (EditText) findViewById(R.id.eTAge);
		eTSkype = (EditText) findViewById(R.id.eTSkype);
		eTCaptcha = (EditText) findViewById(R.id.eTCaptcha);
		tVRegTitle = (TextView) findViewById(R.id.tVRegTitle);
		
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
		captchaGenerating();
				
		dbHelper = new DBHelper(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		// object for data
		ContentValues cv = new ContentValues();
		
		// gettin data from EditTexts
		String login = eTLoginReg.getText().toString().toLowerCase();
		String password = eTPasswordReg.getText().toString();
		String email = eTEmail.getText().toString();
		String personName = eTPersonName.getText().toString();
		String skype = eTSkype.getText().toString();
		String ageString = eTAge.getText().toString();
		String captchaString = eTCaptcha.getText().toString();
				
		String selection = null;
		String[] selectionArgs = null;
		
		
		
		Cursor c = null;
				
		// conncting to the DB
		db = dbHelper.getWritableDatabase();
		
		selection = "login = ?";
		selectionArgs = new String[] { login };
		
		c = db.query("sqliteregistrationsys", null, selection, selectionArgs, null, null,
				null);
		
		switch (v.getId()) {
		case R.id.btnOk:
			if ((login.equalsIgnoreCase("")) || (password.equalsIgnoreCase("")) || 
				(email.equalsIgnoreCase("")) || (personName.equalsIgnoreCase("")) || 
				(ageString.toString().equalsIgnoreCase("")) || 
				(skype.equalsIgnoreCase("")) || captchaString.toString().equalsIgnoreCase("")) {
				tVRegTitle.setText("You've not filled all the fields!");
				tVRegTitle.setTextColor(getResources().getColor(R.color.opaque_red));
			}
			else {
				int captcha = Integer.valueOf(captchaString);
				int age = Integer.valueOf(ageString);
				
				if (c != null && c.moveToFirst()) {
					tVRegTitle.setText("Login " + login + " is already taken! Choose something else please.");
					tVRegTitle.setTextColor(getResources().getColor(R.color.opaque_red));
					
					Log.d(LOG_TAG, "Index of the Login column is " + c.getColumnIndex("login"));
				}
				else {
					if ((captchaString != null) && (captcha == i1+i2)) {
						Log.d(LOG_TAG, "--- Insert in mytable: ---");
				
						// preparing data for pasting: key + value
						cv.put("login", login);
						cv.put("password", password);
						cv.put("email", email);
						cv.put("personname", personName);
						cv.put("age", age);
						cv.put("skype", skype);
						
						// pasting row and getting its ID
						long rowID = db.insert("sqliteregistrationsys", null, cv);
						
						Log.d(LOG_TAG, "row inserted, ID = " + rowID);
						
						Intent intent = new Intent(this, AuthorizationMainActivity.class);
						startActivity(intent); 
					}
					else {
						tVRegTitle.setText("You've entered wrong captcha, try again!");
						tVRegTitle.setTextColor(getResources().getColor(R.color.opaque_red));
						captchaGenerating();
					}
				}
			}
			break;
		}
	dbHelper.close();	
	}

	public void captchaGenerating() {
		i1 = randomNumber.nextInt(100);
		i2 = randomNumber.nextInt(100);
		String captchaText = i1 + " + " + i2 + " = ";
		eTCaptcha.setText("");
		eTCaptcha.setHint(captchaText);
	}
}