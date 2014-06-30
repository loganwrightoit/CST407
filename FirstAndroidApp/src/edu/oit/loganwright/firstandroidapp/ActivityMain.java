package edu.oit.loganwright.firstandroidapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ActivityMain extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
	
	private Button buttonRotLeft = null;
	private Button buttonRotRight = null;
	private Button buttonCaptureImage = null;
	private ImageView imageViewCapture = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonRotLeft = (Button) findViewById(R.id.button2);
		buttonRotRight = (Button) findViewById(R.id.button1);
		buttonCaptureImage = (Button) findViewById(R.id.button3);
		imageViewCapture = (ImageView) findViewById(R.id.imageView1);

		buttonRotLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageViewCapture.setRotation(imageViewCapture.getRotation() - 10.0F);
			}
		});
		
		buttonRotRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageViewCapture.setRotation(imageViewCapture.getRotation() + 10.0F);
			}
		});
		
		buttonCaptureImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
		{
			if( resultCode == Activity.RESULT_OK ) {
				imageViewCapture.setImageURI(data.getData());
				imageViewCapture.setRotation(-90.0F);
			}
			return;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
