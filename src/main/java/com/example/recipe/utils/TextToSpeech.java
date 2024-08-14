package com.example.recipe.utils;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.ArrayList;

import static com.example.recipe.utils.LoggerUtil.logger;


public class TextToSpeech {

    private static final VoiceManager voiceManager = VoiceManager.getInstance();
    private static Voice voice;

    private static ArrayList<String> getVoices() {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        ArrayList<String> voices = new ArrayList<>();
        for (Voice voice : voiceManager.getVoices()) {
            voices.add(voice.getName());
        }

        return voices;
    }

    public static void speak(String message) {
        try {
            if (voice != null) {
                voice.deallocate();
            }
            voice = voiceManager.getVoice(getVoices().get(0));
            if (voice == null) {
                System.err.println("Cannot find voice: kevin16");
                System.exit(1);
            }

            // allocate the resources for the voice
            voice.allocate();

            // set the speed at which the text will be spoken (words per minute)
            voice.setRate(Integer.parseInt("150"));

            // set the volume (0-10)
            voice.setVolume(Integer.parseInt("10"));

            voice.setStyle("casual");

            // convert text to speech
            voice.speak(message);

            // deallocate the resources when done
            voice.deallocate();
        } catch (Exception e) {
            logger.error("Error occurred while converting text to speech: {}", e.getMessage());
        }
    }

    public static void stopSpeaking() {
        try {
            if (voice != null)
                voice.deallocate();
        } catch (Exception e) {
            logger.error("Error occurred while stopping speech: {}", e.getMessage());
        }
    }
}
