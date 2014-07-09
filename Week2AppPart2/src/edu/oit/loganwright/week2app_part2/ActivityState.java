package edu.oit.loganwright.week2app_part2;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityState extends Activity {

	private final String KEY_TEXTVIEW = "ID_TEXTVIEW";
	private TextView textView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state);
		textView = (TextView) findViewById(R.id.textView1);
		String randomString = UUID.randomUUID().toString();
		textView.setText(randomString);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString(KEY_TEXTVIEW, textView.getText().toString());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		textView.setText(savedInstanceState.getString(KEY_TEXTVIEW));
	}
	
}
