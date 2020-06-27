package com.example.notebook.service;

import android.content.Context;

import com.example.notebook.model.Word;
import com.example.notebook.repository.DBWord;

import java.util.ArrayList;

public class DBServiceImpl implements DBService {
    private DBWord dbWord;

    public DBServiceImpl(Context context) {
        this.dbWord = new DBWord(context);
    }

//    @Override
//    public void addUser() {
//        dbWord.addUser();
//    }
//
//    @Override
//    public int updateUser() {
//        return dbWord.updateUser();
//    }
//
//    @Override
//    public int getIsUpdate() {
//        return dbWord.getIsUpdate();
//    }

    @Override
    public void addWord(Word word) {
        dbWord.addWord(word);
    }

    @Override
    public Word getWord(int id) {
        return dbWord.getWord(id);
    }

    @Override
    public ArrayList<Word> getAllWords() {
        return dbWord.getAllWords();
    }

    @Override
    public ArrayList<Word> getAllWords(String str1, String str2) {
        return dbWord.getAllWords(str1, str2);
    }

    @Override
    public int getWordsCount() {
        return dbWord.getWordsCount();
    }

    @Override
    public int updateWord(Word word) {
        return dbWord.updateWord(word);
    }

    @Override
    public void deleteWord(Word word) {
        dbWord.deleteWord(word);
    }

    @Override
    public void addGrammar(Word word) {
        dbWord.addGrammar(word);
    }

    @Override
    public Word getGrammar(int id) {
        return dbWord.getGrammar(id);
    }

    @Override
    public ArrayList<Word> getAllGrammars() {
        return dbWord.getAllGrammars();
    }

    @Override
    public ArrayList<Word> getAllGrammars(String str1, String str2) {
        return dbWord.getAllGrammars(str1, str2);
    }

    @Override
    public int getGrammarsCount() {
        return dbWord.getGrammarsCount();
    }

    @Override
    public int updateGrammar(Word word) {
        return dbWord.updateGrammar(word);
    }

    @Override
    public void deleteGrammar(Word word) {
        dbWord.deleteGrammar(word);
    }
}
