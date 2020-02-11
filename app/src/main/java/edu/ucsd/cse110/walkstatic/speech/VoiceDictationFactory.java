package edu.ucsd.cse110.walkstatic.speech;

import android.content.Context;

public class VoiceDictationFactory {
    private static VoiceDictationBlueprint currentBlueprint = new SpeechRecognizerBlueprint();
    
    public static void setCurrentBlueprint(VoiceDictationBlueprint currentBlueprint){
        VoiceDictationFactory.currentBlueprint = currentBlueprint;
    }

    public static VoiceDictation getVoiceDictation(Context context){
        return VoiceDictationFactory.currentBlueprint.getVoiceDictation(context);
    }

    public interface VoiceDictationBlueprint {
        public VoiceDictation getVoiceDictation(Context context);
    }

    private static class SpeechRecognizerBlueprint implements VoiceDictationBlueprint {

        @Override
        public VoiceDictation getVoiceDictation(Context context) {
            return new SpeechRecognizerAdapter(context);
        }
    }
}
