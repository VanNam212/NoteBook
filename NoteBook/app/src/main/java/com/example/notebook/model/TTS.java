package com.example.notebook.model;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TTS extends UtteranceProgressListener implements TextToSpeech.OnInitListener {

    private final String TAG = TTS.class.getSimpleName();

    private TextToSpeech textToSpeech;

    public TTS(Context context) {
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setOnUtteranceProgressListener(this);
    }

    //speak text that use language US
    public void speak(String text) {
        if(textToSpeech != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String myUtteranceID = "myUtteranceID";
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, myUtteranceID);
            }
            else {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "myUtteranceID");
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, hashMap);
            }
        }
    }

    //mấy cái này ko cần care đâu, vì t cũng ko dùng nó, chỉ gọi ra cho đủ phương thức thôi

    public void stop() {
        textToSpeech.stop();
    }

    public void shutdown() {
        textToSpeech.shutdown();
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }

    @Override
    public void onStart(String utteranceId) {
        Log.d(TAG, "onStart / utteranceID = " + utteranceId);
    }

    @Override
    public void onDone(String utteranceId) {
        Log.d(TAG, "onDone / utteranceID = " + utteranceId);
    }

    @Override
    public void onError(String utteranceId) {
        Log.d(TAG, "onError / utteranceID = " + utteranceId);
    }

    //cái này cần để khởi tạo TTS
    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR)
            //set language
            textToSpeech.setLanguage(Locale.US);
    }
}
