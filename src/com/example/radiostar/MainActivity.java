package com.example.radiostar;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		final Button ttsButton = (Button) findViewById(R.id.btn_tts);
		final Button settingsButton = (Button) findViewById(R.id.btn_settings);
		Button.OnClickListener l = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == ttsButton) {
					Intent intent = new Intent(v.getContext(), TtsView.class);
					MainActivity.this.startActivity(intent);
				} else if (v == settingsButton) {
					Intent intent = new Intent(v.getContext(), SettingsActivity.class);
					MainActivity.this.startActivity(intent);
				}
			}
		};

		ttsButton.setOnClickListener(l);
		settingsButton.setOnClickListener(l);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((RadioStarApplication) getApplication()).releastSpeechKit();
	}

}
