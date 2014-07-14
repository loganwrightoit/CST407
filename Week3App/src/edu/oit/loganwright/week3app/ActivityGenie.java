package edu.oit.loganwright.week3app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class ActivityGenie extends Activity {

	private SharedPreferences sharedPref = null;
	
	private final String KEY_WISH_ONE = "WISH1";
	private final String KEY_WISH_TWO = "WISH2";
	private final String KEY_WISH_THREE = "WISH3";

	private ToggleButton buttonOne = null;
	private ToggleButton buttonTwo = null;
	private ToggleButton buttonThree = null;
	
	private Button buttonReset = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_genie);
		
		sharedPref = getPreferences(Context.MODE_PRIVATE);
		
		buttonOne = (ToggleButton) findViewById(R.id.toggleButton1);
		buttonTwo = (ToggleButton) findViewById(R.id.toggleButton2);
		buttonThree = (ToggleButton) findViewById(R.id.toggleButton3);
		buttonReset = (Button) findViewById(R.id.button1);
		
		buttonOne.setChecked(sharedPref.getBoolean(KEY_WISH_ONE, false));
		buttonTwo.setChecked(sharedPref.getBoolean(KEY_WISH_TWO, false));
		buttonThree.setChecked(sharedPref.getBoolean(KEY_WISH_THREE, false));

		buttonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonOne.setChecked(false);
				buttonTwo.setChecked(false);
				buttonThree.setChecked(false);
			}
		});
	}
	
	@Override
	protected void onPause ()
	{
		super.onPause();

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(KEY_WISH_ONE, buttonOne.isChecked());
		editor.putBoolean(KEY_WISH_TWO, buttonTwo.isChecked());
		editor.putBoolean(KEY_WISH_THREE, buttonThree.isChecked());
		editor.commit();
	}

	public void onToggleClicked(View view)
	{
		ToggleButton button = (ToggleButton) view;		
		
	    if (!button.isChecked()) {
	    	button.setChecked(true);
	    }
	}

}
