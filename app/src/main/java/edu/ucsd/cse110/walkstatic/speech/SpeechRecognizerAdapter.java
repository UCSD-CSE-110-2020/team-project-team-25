package edu.ucsd.cse110.walkstatic.speech;

import android.Manifest;
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
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkCallingOrSelfPermission;

public class SpeechRecognizerAdapter implements VoiceDictation, RecognitionListener {
    private SpeechRecognizer speechRecognizer;
    private SpeechListener speechListener;
    private Bundle lastBundle;
    private Context context;

    public SpeechRecognizerAdapter(Context context){
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        this.speechRecognizer.setRecognitionListener(this);
        this.context = context;
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
        String recognizedStrings = String.join("", strings);
        if(this.speechListener == null){
            return;
        }
        this.speechListener.onSpeech(recognizedStrings, this.lastBundle);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (PermissionChecker.checkCallingOrSelfPermission(this.context, requiredPermission) == PermissionChecker.PERMISSION_DENIED) {
                Log.e("SpeechRecognizerAdapter", "No permission for recording!");
            }
        }
    }
}
