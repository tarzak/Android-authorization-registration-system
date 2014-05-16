package com.example.android_39_sqliteregistrationsys;

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

public class GreetingsActivity extends Activity implements OnClickListener {
	
	public final static String EXTRA_MESSAGE = "com.example.android_39_sqliteregistrationsys.MESSAGE";
	final String LOG_TAG = "myLogs";
	
	String newPersonName, newSkype, newEmail, message;
	int newAge;
	String[] selectionArgs;
	
	TextView tVGreetings;
	EditText eTPersonNameShow, eTAgeShow, eTEmailShow, eTSkypeShow;
	Button btnRenew;
	
	DBHelper dbHelper;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greetings);
		
		tVGreetings = (TextView) findViewById(R.id.tVGreetings);
		eTPersonNameShow = (EditText) findViewById(R.id.eTPersonNameShow);
		eTAgeShow = (EditText) findViewById(R.id.eTAgeShow);
		eTEmailShow = (EditText) findViewById(R.id.eTEmailShow);
		eTSkypeShow = (EditText) findViewById(R.id.eTSkypeShow);
		btnRenew = (Button) findViewById(R.id.btnRenew);
		btnRenew.setOnClickListener(this);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(AuthorizationMainActivity.EXTRA_MESSAGE);
		Log.d(LOG_TAG, "GreetingsActivity has started with intent message " + message);
		
		tVGreetings.setText("Hello, " + message + "!");
		
		dbHelper = new DBHelper(this);
		// connecting to the DB
		db = dbHelper.getWritableDatabase();
		
		Cursor c = null;
		String selection = "login = ?";		
		String[] selectionArgs = { message };
				
		c = db.query("sqliteregistrationsys", null, selection, selectionArgs, null, null, null);
		
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex("login");
			int personNameColIndex = c.getColumnIndex("personname"); eTPersonNameShow.setText(c.getString(personNameColIndex));
			int ageColIndex = c.getColumnIndex("age"); eTAgeShow.setText(c.getString(ageColIndex));
			int emailColIndex = c.getColumnIndex("email"); eTEmailShow.setText(c.getString(emailColIndex));
			int skypeColIndex = c.getColumnIndex("skype"); eTSkypeShow.setText(c.getString(skypeColIndex));
		} else
			Log.d(LOG_TAG, "0 rows");
		
		// closing DB
		dbHelper.close();
		String intentMessage = getIntentMessage();
		Log.d(LOG_TAG, "Custom message is " + intentMessage);
	}

	@Override
	public void onClick(View v) {
		// creating object for data
		ContentValues cv = new ContentValues();
		String[] selectionArgs = { message };
		newPersonName = eTPersonNameShow.getText().toString();
		newAge = Integer.parseInt(eTAgeShow.getText().toString());
		newEmail = eTEmailShow.getText().toString();
		newSkype = eTSkypeShow.getText().toString();
		switch (v.getId()){
			case R.id.btnRenew:
				Log.d(LOG_TAG, "selectionArgs = " + selectionArgs.toString());
				cv.put("personname", newPersonName);
				cv.put("email", newEmail);
				cv.put("age", newAge);
				cv.put("skype", newSkype);
				
				// renewing by login
			    int updCount = db.update("sqliteregistrationsys", cv, "login = ?", selectionArgs);
			    Log.d(LOG_TAG, "updated rows count = " + updCount);
			break;
		}
	}
	public String getIntentMessage () {
		Intent intent = getIntent();
		String intentMessage = intent.getStringExtra(AuthorizationMainActivity.EXTRA_MESSAGE);
		
		return intentMessage;
	}
}
