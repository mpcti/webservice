package com.example.marcio.webservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conecaodb extends SQLiteOpenHelper {

    private static final String NOME_BASE = "movies.db";
    private static final int VERSAO_BASE = 1;

    public Conecaodb(Context context) {
        super(context, NOME_BASE, null, VERSAO_BASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE movient("
                + " id integer primary key autoincrement,"
                + "tittle text,"
                + "image text,"
                + "rating real,"
                + "releaseyear real)";

        db.execSQL(sql);

        String sql1 = "CREATE TABLE genrent("
                + " id integer primary key autoincrement,"
                + "genre text)";

        db.execSQL(sql1);

        String sql2 = "CREATE TABLE genremovie("
                + " id integer primary key autoincrement,"
                + "genreid integer,"
                + "movieid integer,"
                + "FOREIGN KEY(genreid) REFERENCES genremovie(id),"
                + "FOREIGN KEY(movieid) REFERENCES movient(id))";

        db.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
