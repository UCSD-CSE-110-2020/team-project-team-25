package edu.ucsd.cse110.walkstatic.speech;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkCallingOrSelfPermission;

public class SpeechRecognizerAdapter implements VoiceDictation, RecognitionListener {
    private SpeechRecognizer speechRecognizer;
    private SpeechListener speechListener;
    private Bundle lastBundle;
    private Activity activity;

    public SpeechRecognizerAdapter(Activity activity){
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        this.speechRecognizer.setRecognitionListener(this);
        this.activity = activity;
    }

    @Override
    public void setListener(SpeechListener listener) {
        this.speechListener = listener;
    }

    @Override
    public void doRecognition(@Nullable Bundle arguments) {
        this.lastBundle = arguments;
        this.requestRecordAudioPermission();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.speechRecognizer.startListening(intent);
    }

    @Override
    public void cancel() {
        this.speechRecognizer.cancel();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Log.e("SpeechRecognizerAdapter",  "error #" +  error);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> strings = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(this.speechListener == null){
            return;
        }
        if(strings.size() == 0){
            return;
        }
        this.speechListener.onSpeech(strings.get(0), this.lastBundle);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private void requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this.activity, Manifest.permission.RECORD_AUDIO) != PermissionChecker.PERMISSION_GRANTED) {
            Log.d("SpeechRecognizerAdapter", "No permission for recording!");
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    0);
        }
    }
}
