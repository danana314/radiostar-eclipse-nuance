package com.example.radiostar;

import com.example.radiostar.HeadlinesFragment.OnHeadlineSelectedListener;
import com.example.radiostar.PlaybackControlFragment.OnPlaybackControlledListener;
import com.example.radiostar.VocalizerFragment.OnSpeakingListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TtsView extends Activity implements OnPlaybackControlledListener,
		OnSpeakingListener, OnHeadlineSelectedListener {
	private VocalizerFragment _vocalizerFragment;

	public TtsView() {
		super();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC); // So that the 'Media
															// Volume' applies
															// to this activity
		setContentView(R.layout.tts);

		FragmentManager fm = getFragmentManager();
		_vocalizerFragment = (VocalizerFragment) fm
				.findFragmentByTag("vocalizer");

		// If non-null, then it is currently being retained across a
		// configuration change
		if (_vocalizerFragment == null) {
			_vocalizerFragment = new VocalizerFragment();
			fm.beginTransaction().add(_vocalizerFragment, "vocalizer").commit();
		}
	}

	@Override
	public void onPlayPressed() {
		EditText et = (EditText) findViewById(R.id.text_ttsSource);
		String text = et.getText().toString();
		_vocalizerFragment.speak(text);
	}

	@Override
	public void onStopPressed() {
		_vocalizerFragment.cancel();
	}

	@Override
	public void onNextPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreviousPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakingBegin() {
		PlaybackControlFragment controlFrag = (PlaybackControlFragment) getFragmentManager()
				.findFragmentById(R.id.playbackcontrol);
		if (controlFrag != null) {
			controlFrag.showLoadingStopButton(false);
		}
		Log.v("TTS", "onSpeakingBegin: " + controlFrag.toString());
	}

	@Override
	public void onSpeakingDone() {
		PlaybackControlFragment controlFrag = (PlaybackControlFragment) getFragmentManager()
				.findFragmentById(R.id.playbackcontrol);
		if (controlFrag != null) {
			controlFrag.setPlaying(false);
		}
		Log.v("TTS", "onSpeakingDone: " + controlFrag.toString());
	}

	@Override
	public void onArticleSelected(int position) {
		EditText et = (EditText) findViewById(R.id.text_ttsSource);
		et.setText(Ipsum.getTitle(position) + "\n\n" + Ipsum.getDescription(position));
	}
}
