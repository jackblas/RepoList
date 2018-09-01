package com.jackblaszkowski.repolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GitHubRepoDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "repolist.db";
    private static final int DATABASE_VERSION = 1;

    public GitHubRepoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RepoContract.RepoEntry.TABLE_NAME + " ("
                + RepoContract.RepoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RepoContract.RepoEntry.COLUMN_LOGIN + " TEXT NOT NULL,"
                + RepoContract.RepoEntry.COLUMN_NAME + " TEXT NOT NULL,"
                + RepoContract.RepoEntry.COLUMN_HTML_URL + " TEXT NOT NULL,"
                + RepoContract.RepoEntry.COLUMN_DESCRIPTION + " TEXT,"
                + RepoContract.RepoEntry.COLUMN_STARGAZERS + " INTEGER,"
                + RepoContract.RepoEntry.COLUMN_LANGUAGE + " TEXT NOT NULL"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RepoContract.RepoEntry.TABLE_NAME);
        onCreate(db);
    }
}
