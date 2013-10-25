package com.example.radiostar;

import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.Vocalizer;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class VocalizerFragment extends Fragment implements
		OnSharedPreferenceChangeListener {

	private Vocalizer _vocalizer;
	private Object _lastTTSContext = null;

	public interface OnSpeakingListener {
		public void onSpeakingBegin();

		public void onSpeakingDone();
	}

	OnSpeakingListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnSpeakingListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSpeakingListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		// Create Vocalizer listener
		Vocalizer.Listener vocalizerListener = new Vocalizer.Listener() {
			@Override
			public void onSpeakingBegin(Vocalizer vocalizer, String text,
					Object context) {
				// updateCurrentText("Playing text: \"" + text + "\"",
				// Color.GREEN, false);
				// for debugging purpose: printing out the speechkit session id
				android.util.Log.d("RadioStar",
						"Vocalizer.Listener.onSpeakingBegin: session id ["
								+ ((RadioStarApplication) getActivity()
										.getApplication()).getSpeechKit()
										.getSessionId() + "]");
				listener.onSpeakingBegin();
			}

			@Override
			public void onSpeakingDone(Vocalizer vocalizer, String text,
					SpeechError error, Object context) {
				if (context == _lastTTSContext) {
					listener.onSpeakingDone();
					Log.v("VocalizerFragment",
							"onSpeakingDone: No more phrases");
				}

				// for debugging purpose: printing out the speechkit session id
				Log.d("Nuance SampleVoiceApp",
						"Vocalizer.Listener.onSpeakingDone: session id ["
								+ ((RadioStarApplication) getActivity()
										.getApplication()).getSpeechKit()
										.getSessionId() + "]");
			}

		};

		// Get preferred voice
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		String voice = sharedPref.getString(SettingsFragment.KEY_PREF_VOCALIZER_VOICE,
				getString(R.string.pref_vocalizerVoice_default));
		
		// Create a single Vocalizer here.
		_vocalizer = ((RadioStarApplication) getActivity().getApplication())
				.getSpeechKit().createVocalizerWithVoice(voice,
						vocalizerListener, new Handler());
		_vocalizer.setListener(vocalizerListener);

	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		sharedPref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		sharedPref.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (_vocalizer != null) {
			cancel();
		}
	}

	public void cancel() {
		_vocalizer.cancel();
	}

	public void speak(String text) {
		_lastTTSContext = new Object();
		_vocalizer.speakString(text, _lastTTSContext);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(SettingsFragment.KEY_PREF_VOCALIZER_VOICE)) {
			String voice = sharedPreferences.getString(key,
					getString(R.string.pref_vocalizerVoice_default));
			_vocalizer.setVoice(voice);
		}
	}

}
