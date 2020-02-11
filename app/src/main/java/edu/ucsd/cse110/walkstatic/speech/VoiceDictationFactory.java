package edu.ucsd.cse110.walkstatic.speech;

import android.app.Activity;
import android.content.Context;

public class VoiceDictationFactory {
    private static VoiceDictationBlueprint currentBlueprint = new SpeechRecognizerBlueprint();
    
    public static void setCurrentBlueprint(VoiceDictationBlueprint currentBlueprint){
        VoiceDictationFactory.currentBlueprint = currentBlueprint;
    }

    public static VoiceDictation getVoiceDictation(Activity activity){
        return VoiceDictationFactory.currentBlueprint.getVoiceDictation(activity);
    }

    public interface VoiceDictationBlueprint {
        public VoiceDictation getVoiceDictation(Activity activity);
    }

    private static class SpeechRecognizerBlueprint implements VoiceDictationBlueprint {

        @Override
        public VoiceDictation getVoiceDictation(Activity activity) {
            return new SpeechRecognizerAdapter(activity);
        }
    }
}
