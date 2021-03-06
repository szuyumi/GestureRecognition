package com.example.monster.airgesture.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.monster.airgesture.utils.FileCopyUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WelkinShadow on 2017/10/27.
 */

public class DictionaryDBImpl implements DictionaryDB {

    private Context context;
    private SQLiteDatabase database;
    private static final String TAG = "DictionaryDBImpl";
    private static final String DB_NAME = "dictionary.db";
    private static final String DB_PATH = "/data/data/com.example.monster.airgesture/database";

    private static final CandidateWord[] candidateWords1 = new CandidateWord[]{
            new CandidateWord("I", 0, "1", 1),
            new CandidateWord("T", 0, "1", 1),
            new CandidateWord("Z", 0, "1", 1),
            new CandidateWord("J", 0, "1", 1)};

    private static final CandidateWord[] candidateWords2 = new CandidateWord[]{
            new CandidateWord("E", 0, "1", 1),
            new CandidateWord("F", 0, "1", 1),
            new CandidateWord("H", 0, "1", 1),
            new CandidateWord("K", 0, "1", 1),
            new CandidateWord("L", 0, "1", 1)};

    private static final CandidateWord[] candidateWords3 = new CandidateWord[]{
            new CandidateWord("A", 0, "1", 1),
            new CandidateWord("M", 0, "1", 1),
            new CandidateWord("N", 0, "1", 1)};

    private static final CandidateWord[] candidateWords4 = new CandidateWord[]{
            new CandidateWord("V", 0, "1", 1),
            new CandidateWord("W", 0, "1", 1),
            new CandidateWord("X", 0, "1", 1),
            new CandidateWord("Y", 0, "1", 1)};

    private static final CandidateWord[] candidateWords5 = new CandidateWord[]{
            new CandidateWord("C", 0, "1", 1),
            new CandidateWord("G", 0, "1", 1),
            new CandidateWord("O", 0, "1", 1),
            new CandidateWord("Q", 0, "1", 1),
            new CandidateWord("S", 0, "1", 1),
            new CandidateWord("U", 0, "1", 1)};

    private static final CandidateWord[] candidateWords6 = new CandidateWord[]{
            new CandidateWord("B", 0, "1", 1),
            new CandidateWord("D", 0, "1", 1),
            new CandidateWord("P", 0, "1", 1),
            new CandidateWord("R", 0, "1", 1)};

    public DictionaryDBImpl(Context context) {
        Log.i(TAG, "database initial ");
        this.context = context;
        File dbFile = new File(DB_PATH + DB_NAME);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (!dbFile.exists()) {
            Log.i(TAG, "copy database from assets");
            try {
                dbFile.createNewFile();
                inputStream = context.getAssets().open(DB_NAME);
                outputStream = new FileOutputStream(dbFile);
                boolean result = FileCopyUtil.copy(inputStream, outputStream);
                if (!result) {
                    Log.e(TAG, "fail to copy database");
                }
                Log.i(TAG, "copy right");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        database = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
    }

    @Override
    public List<CandidateWord> getWordList(String coding) {
        Log.i(TAG, "database query");
        Cursor cursor = database.rawQuery("SELECT * FROM dictionary WHERE code LIKE '"
                + coding + "%' ORDER BY length DESC", null);
        CandidateWord candidateWord = null;
        List<CandidateWord> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                float probability = cursor.getFloat(cursor.getColumnIndex("probability"));
                int length = cursor.getInt(cursor.getColumnIndex("length"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                candidateWord = new CandidateWord(word, probability, code, length);
                result.add(candidateWord);
                Log.d(TAG, "database query : word = " + word + " length = " + length + " code = " + code);
                Log.d(TAG, "get" + candidateWord.getWord());
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "finish querying the result length = " + result.size());
        return result;
    }

    @Override
    public List<CandidateWord> getLetter(String type) {
        switch (type) {
            case "1":
                return Arrays.asList(candidateWords1);
            case "2":
                return Arrays.asList(candidateWords2);
            case "3":
                return Arrays.asList(candidateWords3);
            case "4":
                return Arrays.asList(candidateWords4);
            case "5":
                return Arrays.asList(candidateWords5);
            case "6":
                return Arrays.asList(candidateWords6);
        }
        return null;
    }
}
