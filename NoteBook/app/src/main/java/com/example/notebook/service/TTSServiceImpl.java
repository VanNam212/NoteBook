package com.example.notebook.service;

import android.content.Context;

import com.example.notebook.model.TTS;
//class này gọi lại các phương thức của class TTS trong model, đơn giản vậy thôi, bấm ctrl chuột trái là chuyển đến phương thức đó
public class TTSServiceImpl implements TTSService {
    private TTS tts;



    public TTSServiceImpl(Context context) {
        //khởi tạo TTS (text to speech), context là cái để xác định TTS dùng cho form nào
        tts = new TTS(context);
    }

    @Override
    public void speak(String text) {
        tts.speak(text);
    }

    @Override
    public void stop() {
        tts.stop();
    }

    @Override
    public void shutdown() {
        tts.shutdown();
    }

    @Override
    public boolean isSpeaking() {
        return tts.isSpeaking();
    }
}
