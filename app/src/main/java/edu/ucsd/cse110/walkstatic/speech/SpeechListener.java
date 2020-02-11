package edu.ucsd.cse110.walkstatic.speech;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SpeechListener {

    public void onSpeech(@NonNull String received, @Nullable Bundle options);
}
