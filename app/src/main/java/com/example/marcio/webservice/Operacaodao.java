package com.example.marcio.webservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

public class Operacaodao {
    private Context context;

    public Operacaodao(Context context){
        this.context = context;

    }

    public long inserir_filme(Movies movies){

        ContentValues valores = new ContentValues();
        valores.put("tittle",movies.getTitle());
        valores.put("image",movies.getImage());
        valores.put("rating",movies.getRating());
        valores.put("releaseYear",movies.getReleaseYear());

        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long id = sqldb.insert("movient",null,valores);
        sqldb.close();
        db.close();
        return id;
    }

    public long inserir_genre(Genre genre){

        ContentValues valores = new ContentValues();
        valores.put("genre",genre.getGenre().toUpperCase());
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long id = sqldb.insert("genrent",null,valores);
        sqldb.close();
        db.close();
        return id;
    }

    public long inserir_genremovie(int cd_genre,int cd_movie){

        ContentValues valores = new ContentValues();
        valores.put("genreid",cd_genre);
        valores.put("movieid",cd_movie);
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long id = sqldb.insert("genremovie",null,valores);
        sqldb.close();
        db.close();
        return id;
    }


    public ArrayList<Genre> listar(String desc){

        String sql ="Select id,genre from genrent where genre ="+ "'" + desc+"'";
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        ArrayList<Genre> loperacao = new ArrayList<Genre>();
        if(cursor.moveToFirst()){
            do{
                Genre bdgenre = new Genre();
                bdgenre.setId((int) cursor.getLong(0));
                bdgenre.setGenre( cursor.getString(1));
                loperacao.add(bdgenre);
            }while(cursor.moveToNext());
        }
        return loperacao;
    }

    public ArrayList<Movies> listarfilme(){

        String sql ="Select * from movient";
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        ArrayList<Movies> loperacao = new ArrayList<Movies>();
        if(cursor.moveToFirst()){
            do{
                Movies bdfilme = new Movies();
                bdfilme.setId_filme((int) cursor.getLong(0));
                bdfilme.setTitle(cursor.getString(1));
                bdfilme.setImage(cursor.getString(2));
                bdfilme.setRating(cursor.getFloat(3));
                bdfilme.setReleaseYear(cursor.getInt(4));
                loperacao.add(bdfilme);
            }while(cursor.moveToNext());
        }
        return loperacao;
    }

    public int quantidadeFilmes() {

        String sql ="Select * from movient";
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        return cursor.getCount();

    }

    public Object pegarfilme(int i ) {
        Movies filme = new Movies();
        String sql ="Select * from movient";
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        cursor.moveToPosition(i);
        filme.setId_filme((int) cursor.getLong(0));
        filme.setTitle(cursor.getString(1));
        filme.setImage(cursor.getString(2));
        filme.setRating(cursor.getFloat(3));
        filme.setReleaseYear(cursor.getInt(4));
        return filme;
    }


    public String[] listarfilmeporgenero(int id_p){

        String sql ="Select genreid from genremovie where movieid =" + id_p ;
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        int t[] = new int[cursor.getCount()];
        String generos[] = new String[cursor.getCount()];
        if(cursor.moveToFirst()){
            int g;
            for(g=0;g<cursor.getCount();g++){
               cursor.moveToPosition(g);
               t[g]=cursor.getInt(0);
                generos[g]=new Operacaodao(context).desc_genero(t[g]);
            }
        }
        return generos;
    }

    public String desc_genero(int id_p){
        String sql ="Select genre from genrent where id =" + id_p ;
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        //int t[] = new int[cursor.getCount()];
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        return null;
    }

    public Movies pegarfilmeid(int id) {
        Movies filme = new Movies();
        String sql ="Select * from movient where id ="+id;
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        cursor.moveToFirst();
        filme.setId_filme((int) cursor.getLong(0));
        filme.setTitle(cursor.getString(1));
        filme.setImage(cursor.getString(2));
        filme.setRating(cursor.getFloat(3));
        filme.setReleaseYear(cursor.getInt(4));
        filme.setGenre(new Operacaodao(context).listarfilmeporgenero(id));
        return filme;


    }

    public void alterar_filme(Movies movies) {

        ContentValues valores = new ContentValues();
        valores.put("tittle",movies.getTitle());
        valores.put("image",movies.getImage());
        valores.put("rating",movies.getRating());
        valores.put("releaseYear",movies.getReleaseYear());

        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long id = sqldb.update("movient",valores,"id = ?",new String[]{String.valueOf(movies.getId_filme())});
        sqldb.close();
        db.close();
    }


    public int[] listarfilmeporgenero_id(int id_p){

        String sql ="Select id from genremovie where movieid =" + id_p ;
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery(sql, null);
        int t[] = new int[cursor.getCount()];
        int generos[] = new int[cursor.getCount()];
        if(cursor.moveToFirst()){
            int g;
            for(g=0;g<cursor.getCount();g++){
                cursor.moveToPosition(g);
                t[g]=cursor.getInt(0);
                generos[g]=t[g];
            }
        }
        return generos;
    }

    public void alterar_genremovie(int i, int idfilme, int idgenero) {
        ContentValues valores = new ContentValues();
        valores.put("genreid",idgenero);
        valores.put("movieid",idfilme);
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        long id = sqldb.update("genremovie",valores,"id = ?",new String[]{String.valueOf(i)});
        sqldb.close();
        db.close();


    }

    public void deletegenoerofilme(int i) {
        //ContentValues valores = new ContentValues();
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        sqldb.delete("genremovie","id = ?",new String[]{String.valueOf(i)});
        sqldb.close();
        db.close();


    }

    public void deletefilme(long id) {
        Conecaodb db = new Conecaodb(context);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        sqldb.delete("movient","id = ?",new String[]{String.valueOf(id)});
        sqldb.close();
        db.close();
    }
}
