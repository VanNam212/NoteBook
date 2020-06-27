package com.example.notebook.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notebook.model.Word;

import java.util.ArrayList;
import java.util.Date;

//database
public class DBWord extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Phiên bản
    private static final int DATABASE_VERSION = 1;

    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Word_Manager";

    // Tên bảng: Word.
    private static final String TABLE_WORD = "Word";
    private static final String TABLE_WORD2 = "Grammar";
//    private static final String TABLE_USER = "user";

//    private static final String COLUMN_USER_NAME = "Name";
//    private static final String COLUMN_USER_UPGRADE = "IsUpgrade";
    private static final String COLUMN_WORD_ID = "Word_Id";
    private static final String COLUMN_WORD_ORIGIN = "Word_Origin";
    private static final String COLUMN_WORD_SUB = "Word_Sub";
    private static final String COLUMN_WORD_DATE = "Word_Date";
    private static final String COLUMN_WORD_Language_Origin = "Word_Language_Origin";
    private static final String COLUMN_WORD_Language_Sub = "Word_Language_Sub";
    private static final String COLUMN_WORD_ID2 = "Grammar_Id";
    private static final String COLUMN_WORD_ORIGIN2 = "Grammar_Origin";
    private static final String COLUMN_WORD_SUB2 = "Grammar_Sub";
    private static final String COLUMN_WORD_DATE2 = "Grammar_Date";
    private static final String COLUMN_WORD_Language_Origin2 = "Grammar_Language_Origin";
    private static final String COLUMN_WORD_Language_Sub2 = "Grammar_Language_Sub";

    public DBWord(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DBWord.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_WORD + "("
                + COLUMN_WORD_ID + " INTEGER PRIMARY KEY autoincrement, " + COLUMN_WORD_ORIGIN + " TEXT, "
                + COLUMN_WORD_SUB + " TEXT, " + COLUMN_WORD_DATE + " TEXT, " + COLUMN_WORD_Language_Origin + " TEXT, " + COLUMN_WORD_Language_Sub + " TEXT" + ")";

        // Script tạo bảng.
        String script2 = "CREATE TABLE " + TABLE_WORD2 + "("
                + COLUMN_WORD_ID2 + " INTEGER PRIMARY KEY autoincrement, " + COLUMN_WORD_ORIGIN2 + " TEXT, "
                + COLUMN_WORD_SUB2 + " TEXT, " + COLUMN_WORD_DATE2 + " TEXT, " + COLUMN_WORD_Language_Origin2 + " TEXT, " + COLUMN_WORD_Language_Sub2 + " TEXT" + ")";

//        String script3 = "CREATE TABLE " + TABLE_USER + "("
//                + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_UPGRADE + " INTEGER " + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        db.execSQL(script2);
//        db.execSQL(script3);
//        addUser();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "DBWord.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD2);

        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultNotesIfNeed() {
        int count = this.getWordsCount();
        if (count == 0) {
            Word note1 = new Word(1, "Hello",
                    "Xin chào", new Date().toLocaleString() + "", "English", "Vietnamese");
            Word note2 = new Word(2, "GoodBye",
                    "Tạm biệt", new Date().toLocaleString() + "", "English", "Vietnamese");
            this.addWord(note1);
            this.addWord(note2);
            this.addGrammar(note1);
            this.addGrammar(note2);
        }
    }

//    public void addUser() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_NAME, "VanNam-HoaiAnh");
//        values.put(COLUMN_USER_UPGRADE, 0);
//
//        // Trèn một dòng dữ liệu vào bảng.
//        db.insert(TABLE_USER, null, values);
//        // Đóng kết nối database.
//        db.close();
//    }
//
//    public int updateUser() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_UPGRADE, 1);
//
//        // updating row
//        return db.update(TABLE_USER, values, null, null);
//    }
//
//    public int getIsUpdate() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_NAME, COLUMN_USER_UPGRADE}, null, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        int isUpgrade = cursor.getInt(1);
//
//        return isUpgrade;
//    }

    public void addWord(Word word) {
        Log.i(TAG, "DBWord.addNote ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD_ORIGIN, word.getOriginal_Text());
        values.put(COLUMN_WORD_SUB, word.getSub_Text());
        values.put(COLUMN_WORD_DATE, word.getDate_Text());
        values.put(COLUMN_WORD_Language_Origin, word.getLanguage_Origin());
        values.put(COLUMN_WORD_Language_Sub, word.getLanguage_Sub());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_WORD, null, values);
        // Đóng kết nối database.
        db.close();
    }

    public Word getWord(int id) {
        Log.i(TAG, "DBWord.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORD, new String[]{COLUMN_WORD_ID,
                        COLUMN_WORD_ORIGIN, COLUMN_WORD_SUB, COLUMN_WORD_DATE, COLUMN_WORD_Language_Origin, COLUMN_WORD_Language_Sub}, COLUMN_WORD_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Word note = new Word(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return note
        return note;
    }

    public ArrayList<Word> getAllWords() {
        Log.i(TAG, "DBWord.getAllWords ... ");

        ArrayList<Word> wordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORD;
//        String selectQuery = "SELECT  * FROM " + TABLE_WORD + " ORDER BY "+COLUMN_WORD_ORIGIN + " asc";   //asc là tăng dần, desc là giảm dần
//        String selectQuery = "SELECT  * FROM " + TABLE_WORD + " ORDER BY "+COLUMN_WORD_ORIGIN + " asc AND " + COLUMN_WORD_SUB + " asc";   //asc là tăng dần, desc là giảm dần

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setWord_ID(cursor.getInt(0));
                word.setOriginal_Text(cursor.getString(1));
                word.setSub_Text(cursor.getString(2));
                word.setDate_Text(cursor.getString(3));
                word.setLanguage_Origin(cursor.getString(4));
                word.setLanguage_Sub(cursor.getString(5));

                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // return note list
        return wordList;
    }

    public ArrayList<Word> getAllWords(String str1, String str2) {
        Log.i(TAG, "DBWord.getAllWords ... ");

        ArrayList<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_WORD + " WHERE ( " + COLUMN_WORD_Language_Origin + " = ? " + " AND " + COLUMN_WORD_Language_Sub + " = ? " + " ) OR ( " + COLUMN_WORD_Language_Sub + " = ? " + " AND " + COLUMN_WORD_Language_Origin + " = ?" + " )",
                new String[]{str1, str2, str1, str2});

        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setWord_ID(cursor.getInt(0));
                word.setOriginal_Text(cursor.getString(1));
                word.setSub_Text(cursor.getString(2));
                word.setDate_Text(cursor.getString(3));
                word.setLanguage_Origin(cursor.getString(4));
                word.setLanguage_Sub(cursor.getString(5));

                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // return note list
        return wordList;
    }

    public int getWordsCount() {
        Log.i(TAG, "DBWord.getWordsCount ... ");

        String countQuery = "SELECT * FROM " + TABLE_WORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateWord(Word word) {
        Log.i(TAG, "DBWord.updateWord ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD_ORIGIN, word.getOriginal_Text());
        values.put(COLUMN_WORD_SUB, word.getSub_Text());
        values.put(COLUMN_WORD_DATE, word.getDate_Text());
        values.put(COLUMN_WORD_Language_Origin, word.getLanguage_Origin());
        values.put(COLUMN_WORD_Language_Sub, word.getLanguage_Sub());

        // updating row
        return db.update(TABLE_WORD, values, COLUMN_WORD_ID + " = ?",
                new String[]{String.valueOf(word.getWord_ID())});
    }

    public void deleteWord(Word word) {
        Log.i(TAG, "DBWord.updateWord ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORD, COLUMN_WORD_ID + " = ?",
                new String[]{String.valueOf(word.getWord_ID())});
        db.close();
    }

    public void addGrammar(Word word) {
        Log.i(TAG, "DBWord.addNote ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD_ORIGIN2, word.getOriginal_Text());
        values.put(COLUMN_WORD_SUB2, word.getSub_Text());
        values.put(COLUMN_WORD_DATE2, word.getDate_Text());
        values.put(COLUMN_WORD_Language_Origin2, word.getLanguage_Origin());
        values.put(COLUMN_WORD_Language_Sub2, word.getLanguage_Sub());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_WORD2, null, values);

        // Đóng kết nối database.
        db.close();
    }

    public Word getGrammar(int id) {
        Log.i(TAG, "DBWord.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORD2, new String[]{COLUMN_WORD_ID2,
                        COLUMN_WORD_ORIGIN2, COLUMN_WORD_SUB2, COLUMN_WORD_DATE2, COLUMN_WORD_Language_Origin2, COLUMN_WORD_Language_Sub2}, COLUMN_WORD_ID2 + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Word note = new Word(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return note
        return note;
    }

    public ArrayList<Word> getAllGrammars() {
        Log.i(TAG, "DBWord.getAllWords ... ");

        ArrayList<Word> wordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_WORD2;
//        String selectQuery = "SELECT  * FROM " + TABLE_WORD2 + " ORDER BY "+COLUMN_WORD_ORIGIN2 + " asc";   //asc là tăng dần, desc là giảm dần
//        String selectQuery = "SELECT  * FROM " + TABLE_WORD2 + " ORDER BY "+COLUMN_WORD_ORIGIN2 + " asc AND " + COLUMN_WORD_SUB2 + " asc";   //asc là tăng dần, desc là giảm dần


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setWord_ID(cursor.getInt(0));
                word.setOriginal_Text(cursor.getString(1));
                word.setSub_Text(cursor.getString(2));
                word.setDate_Text(cursor.getString(3));
                word.setLanguage_Origin(cursor.getString(4));
                word.setLanguage_Sub(cursor.getString(5));

                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // return note list
        return wordList;
    }

    public ArrayList<Word> getAllGrammars(String str1, String str2) {
        Log.i(TAG, "DBWord.get0AllWords ... ");

        ArrayList<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_WORD2 + " WHERE (" + COLUMN_WORD_Language_Origin2 + " = ?" + " AND " + COLUMN_WORD_Language_Sub2 + " = ?" + " ) OR (" + COLUMN_WORD_Language_Sub2 + " = ?" + " AND " + COLUMN_WORD_Language_Origin2 + " = ?" + " )",
                new String[]{str1, str2, str1, str2});


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setWord_ID(cursor.getInt(0));
                word.setOriginal_Text(cursor.getString(1));
                word.setSub_Text(cursor.getString(2));
                word.setDate_Text(cursor.getString(3));
                word.setLanguage_Origin(cursor.getString(4));
                word.setLanguage_Sub(cursor.getString(5));

                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // return note list
        return wordList;
    }

    public int getGrammarsCount() {
        Log.i(TAG, "DBWord.getWordsCount ... ");

        String countQuery = "SELECT * FROM " + TABLE_WORD2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateGrammar(Word word) {
        Log.i(TAG, "DBWord.updateWord ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD_ORIGIN2, word.getOriginal_Text());
        values.put(COLUMN_WORD_SUB2, word.getSub_Text());
        values.put(COLUMN_WORD_DATE2, word.getDate_Text());
        values.put(COLUMN_WORD_Language_Origin2, word.getLanguage_Origin());
        values.put(COLUMN_WORD_Language_Sub2, word.getLanguage_Sub());

        // updating row
        return db.update(TABLE_WORD2, values, COLUMN_WORD_ID2 + " = ?",
                new String[]{String.valueOf(word.getWord_ID())});
    }

    public void deleteGrammar(Word word) {
        Log.i(TAG, "DBWord.updateWord ... " + word.getOriginal_Text());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORD2, COLUMN_WORD_ID2 + " = ?",
                new String[]{String.valueOf(word.getWord_ID())});
        db.close();
    }
}
