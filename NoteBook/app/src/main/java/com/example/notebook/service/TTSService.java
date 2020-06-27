package com.example.notebook.service;

//cái class này ko có tác dụng gì đâu, t làm theo mô hình mvc, để controller giao tiếp với model thông qua cái này
public interface TTSService {
//có các phương thức giống class TTS trong package model
    void speak(String text);

    void stop();

    void shutdown();

    boolean isSpeaking();
}
