package edu.ucsd.cse110.walkstatic.speech;

import android.os.Bundle;

import androidx.annotation.Nullable;

public interface VoiceDictation {
    public void setListener(SpeechListener listener);
    public void doRecognition(@Nullable Bundle arguments);
}
