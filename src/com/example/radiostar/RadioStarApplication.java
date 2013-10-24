package com.example.radiostar;

import android.app.Application;
import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.SpeechKit;

public class RadioStarApplication extends Application {
	private SpeechKit _speechKit;

	public SpeechKit getSpeechKit() {
		if (_speechKit == null)
        {
            _speechKit = SpeechKit.initialize(getApplicationContext(), AppInfo.SpeechKitAppId, AppInfo.SpeechKitServer, AppInfo.SpeechKitPort, AppInfo.SpeechKitSsl, AppInfo.SpeechKitApplicationKey);
            _speechKit.connect();
            
            Prompt beep = _speechKit.defineAudioPrompt(R.raw.beep);
            _speechKit.setDefaultRecognizerPrompts(beep, Prompt.vibration(100), null, null);
        }
		return _speechKit;
	}
	
	public void releastSpeechKit() {
		if (_speechKit != null) {
			_speechKit.release();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
