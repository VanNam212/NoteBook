package com.example.notebook.service;

import com.example.notebook.model.Word;

import java.util.ArrayList;

public interface DBService {

//    void addUser();
//
//    int updateUser();
//
//    int getIsUpdate();

    void addWord(Word word);

    Word getWord(int id);

    ArrayList<Word> getAllWords();

    ArrayList<Word> getAllWords(String str1, String str2);

    int getWordsCount();

    int updateWord(Word word);

    void deleteWord(Word word);

    void addGrammar(Word word);

    Word getGrammar(int id);

    ArrayList<Word> getAllGrammars();

    ArrayList<Word> getAllGrammars(String str1, String str2);

    int getGrammarsCount();

    int updateGrammar(Word word);

    void deleteGrammar(Word word);
}
