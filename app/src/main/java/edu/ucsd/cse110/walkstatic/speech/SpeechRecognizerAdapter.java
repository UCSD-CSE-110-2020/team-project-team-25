package edu.ucsd.cse110.walkstatic.speech;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

public class SpeechRecognizerAdapter implements VoiceDictation, RecognitionListener {
    private static final String logKey = "SpeechRecognizerAdapter";

    private static SpeechRecognizer speechRecognizer;
    private SpeechListener speechListener;
    private Bundle lastBundle;
    private Activity activity;
    private boolean running;

    public SpeechRecognizerAdapter(Activity activity){
        if(speechRecognizer == null){
            SpeechRecognizerAdapter.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        }
        SpeechRecognizerAdapter.speechRecognizer.setRecognitionListener(this);
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
        SpeechRecognizerAdapter.speechRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(logKey, "Event onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(logKey, "Event onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(logKey, "Event onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(logKey, "Event onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.e(logKey,  "error #" +  error);
        if(this.speechListener == null){
            return;
        }
        this.speechListener.onSpeechDone(true, this.lastBundle);
    }

    @Override
    public void onResults(Bundle results) {
        this.onPartialResults(results);
        Log.d(logKey, "Event results");
        this.notifyDone();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> strings = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(this.speechListener == null){
            return;
        }
        if(strings.size() == 0){
            return;
        }
        this.speechListener.onSpeech(strings.get(0), this.lastBundle);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(logKey, "Event #" + eventType);
    }

    private void requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this.activity, Manifest.permission.RECORD_AUDIO) != PermissionChecker.PERMISSION_GRANTED) {
            Log.d(logKey, "No permission for recording!");
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    0);
        }
    }

    private void notifyDone(){
        if(this.speechListener == null){
            return;
        }
        this.speechListener.onSpeechDone(false, this.lastBundle);
        this.lastBundle = null;
    }

    @Override
    public void cancel(){
        SpeechRecognizerAdapter.speechRecognizer.cancel();
    }
}
