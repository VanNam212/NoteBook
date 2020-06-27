package com.example.notebook.model;

public class Word implements Comparable<Word> {
    private int Word_ID;
    private String Original_Text;
    private String Sub_Text;
    private String Date_Text;
    private String Language_Origin;
    private String Language_Sub;

    public Word() {
    }

    public Word(int word_ID, String original_Text, String sub_Text, String date_Text, String language_Origin, String language_Sub) {
        Word_ID = word_ID;
        Original_Text = original_Text;
        Sub_Text = sub_Text;
        Date_Text = date_Text;
        Language_Origin = language_Origin;
        Language_Sub = language_Sub;
    }

    public Word(String original_Text, String sub_Text, String date_Text, String language_Origin, String language_Sub) {
        Original_Text = original_Text;
        Sub_Text = sub_Text;
        Date_Text = date_Text;
        Language_Origin = language_Origin;
        Language_Sub = language_Sub;
    }

    public Word(String original_Text, String sub_Text) {
        Original_Text = original_Text;
        Sub_Text = sub_Text;
    }

    public int getWord_ID() {
        return Word_ID;
    }

    public void setWord_ID(int word_ID) {
        Word_ID = word_ID;
    }

    public String getOriginal_Text() {
        return Original_Text;
    }

    public void setOriginal_Text(String original_Text) {
        Original_Text = original_Text;
    }

    public String getSub_Text() {
        return Sub_Text;
    }

    public void setSub_Text(String sub_Text) {
        Sub_Text = sub_Text;
    }

    public String getDate_Text() {
        return Date_Text;
    }

    public void setDate_Text(String date_Text) {
        Date_Text = date_Text;
    }

    public String getLanguage_Origin() {
        return Language_Origin;
    }

    public void setLanguage_Origin(String language_Origin) {
        Language_Origin = language_Origin;
    }

    public String getLanguage_Sub() {
        return Language_Sub;
    }

    public void setLanguage_Sub(String language_Sub) {
        Language_Sub = language_Sub;
    }

    @Override
    public int compareTo(Word o) {
        if (this.getOriginal_Text().compareTo(o.getOriginal_Text()) == 0) {
            return this.getSub_Text().compareTo(o.getSub_Text());
        } else {
            return this.getOriginal_Text().compareTo(o.getOriginal_Text());
        }
    }
}
