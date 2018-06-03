package com.example.marcio.webservice;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class Main extends AppCompatActivity {
    Context context = this;
    EditText et_tittle, et_image,et_rating,etry,genero1,genero2,genero3;
    String tipoOperacao ="";
    Button bbt, bttd;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_tittle = (EditText) findViewById(R.id.et_tittle);
        et_image = (EditText) findViewById(R.id.et_image);
        et_rating = (EditText) findViewById(R.id.et_rating);
        etry = (EditText) findViewById(R.id.etry);
        genero1 = (EditText) findViewById(R.id.etgen1);
        genero2 = (EditText) findViewById(R.id.etgen2);
        genero3 = (EditText) findViewById(R.id.etgen3);
        //bbt = (Button) findViewById(R.id.bt_save);
        bttd = (Button) findViewById(R.id.bt_delete);
        //bttd.setVisibility(context);
        consultaweb();
        tipoOperacao = getIntent().getStringExtra("tipo");
        if(tipoOperacao.equals("alteracao")){
            id = getIntent().getLongExtra("id",0);
            Movies movies = new Operacaodao(context).pegarfilmeid((int) id);
            et_tittle.setText(movies.getTitle());
            et_image.setText(movies.getImage());
            et_rating.setText( Float.toString(movies.getRating()));
            etry.setText(movies.getReleaseYear()+"");
            //bbt.setText("Alterar");
            int tamanho = movies.getGenre().length;

            if (1<=tamanho) {
                genero1.setText(movies.getGenre()[0]);
            }else {
                genero1.setText("");
            }

            if (2<=tamanho) {
                genero2.setText(movies.getGenre()[1]);
            }else {
                genero2.setText("");
            }
            if (3<=tamanho) {
                genero3.setText(movies.getGenre()[2]);
            }else {
                genero3.setText("");
            }
        }else {
            /*gerar*/
            et_tittle.setText("");
            et_image.setText("");
            et_rating.setText("");
            etry.setText("");
            genero1.setText("");
            genero2.setText("");
            genero3.setText("");
        }
        //bbt.setText("Salvar");
    }

    public void consultaweb(){
        AsyncHttpClient cliente = new AsyncHttpClient();
    String URL="http://api.androidhive.info/json/movies.json";
    cliente.get(URL,new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("meuapp",response.toString());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            //Log.d("meuapp2",response.toString());
            Gson gson = new GsonBuilder().create();
            ArrayList<Movies> g = new ArrayList<Movies>() ;
             g = new Operacaodao(context).listarfilme();
            if(g.size()<1) {
                int tam = response.length();
                Log.d("meuapp","do objeto " + response.length());
                try {
                    int x;
                    for (x = 0; x < tam; x++) {
                        Movies movies = gson.fromJson(response.get(x).toString(), Movies.class);
                        //Log.d("meuapp", "do objeto " + movies.toString());
                        //Log.d("meuapp", "res " + Arrays.toString(movies.getGenre()));
                        ///p; //= movies.getGenre();
                        //Log.d("teste", "res " + p[0]);

                        int idfilme = (int) new Operacaodao(context).inserir_filme(movies);

                        int v;
                        for (v = 0; v < movies.getGenre().length; v++) {

                            Genre go = new Genre();
                            go.setGenre(movies.getGenre()[v]);

                            int idgenero;
                            ArrayList<Genre> lgenre = new ArrayList<Genre>();
                            lgenre = new Operacaodao(context).listar(go.getGenre().toUpperCase());

                            if (lgenre.size() > 0) {

                                if (lgenre.get(0).getGenre() != go.getGenre().toUpperCase()) {
                                    idgenero = (int) new Operacaodao(context).inserir_genre(go);
                                } else {
                                    idgenero = lgenre.get(0).getId();
                                }
                                new Operacaodao(context).inserir_genremovie(idgenero, idfilme);

                            } else {
                                idgenero = (int) new Operacaodao(context).inserir_genre(go);
                                new Operacaodao(context).inserir_genremovie(idgenero, idfilme);
                            }
                        }
                    /*ArrayList<String> po = new ArrayList<String>();
                    po.add("ree");
                    po.add("raa");
                    String[] p = new String[po.size()];

                    p[0] = "";
                    */

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("meuapp","do objeto" + movie.toString());


                ArrayList<Movies> lfimes = new ArrayList<Movies>();
                lfimes = new Operacaodao(context).listarfilme();

                tam = lfimes.size();
                int y;
                for (y = 0; y < tam; y++) {
                    String p[];
                    p = new Operacaodao(context).listarfilmeporgenero(y);
                    lfimes.get(y).setGenre(p);
                    Log.d("meuapp", "banco " + y + "  " + lfimes.get(y).toString());

                    String testes = new Operacaodao(context).desc_genero(1);
                    //Log.d("revisao", testes);

                }
                Toast.makeText(getApplicationContext(),"WebService Consumido",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("erro",responseString);
        }
    });

    }

    public void cadastra(View v){

        if(et_tittle.getText().toString().isEmpty() || et_image.getText().toString().isEmpty()
        || et_rating.getText().toString().isEmpty() || etry.getText().toString().isEmpty()
                || genero1.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Os Campos com * são Obrigatórios", Toast.LENGTH_LONG).show();
        } else {

            String titulo = et_tittle.getText().toString();
            String image = et_image.getText().toString();
            double reating = Double.parseDouble(et_rating.getText().toString());
            int ano = Integer.parseInt(etry.getText().toString());
            String ge1 = genero1.getText().toString();
            String ge2 = genero2.getText().toString();
            String ge3 = genero3.getText().toString();
            String[] array = new String[3];
            array[0] = ge1;
            array[1] = ge2;
            array[2] = ge3;
            //Log.d("meuapp",array[0] +" ggg "+ array[1]+" g  "+array[2] + " arrr");
            Movies newfilme = new Movies();
            newfilme.setTitle(titulo);
            newfilme.setImage(image);
            newfilme.setRating((float) reating);
            newfilme.setReleaseYear(ano);
            newfilme.setGenre(array);
            int idfilme = (int) id;

            if (tipoOperacao.equals("alteracao")) {
                newfilme.setId_filme((int) id);
                new Operacaodao(context).alterar_filme(newfilme);

                int[] idgenero = new int[newfilme.getGenre().length];
                int h;
                for (h = 0; h < newfilme.getGenre().length; h++) {

                    Genre go = new Genre();
                    go.setGenre(newfilme.getGenre()[h]);

                    ArrayList<Genre> lgenre = new ArrayList<Genre>();
                    lgenre = new Operacaodao(context).listar(go.getGenre().toUpperCase());

                    if (lgenre.size() > 0) {

                        if (lgenre.get(0).getGenre() != go.getGenre().toUpperCase()) {
                            idgenero[h] = (int) new Operacaodao(context).inserir_genre(go);
                        } else {
                            idgenero[h] = lgenre.get(0).getId();
                        }
                        //new Operacaodao(context).inserir_genremovie(idgenero, idfilme);
                        //Toast.makeText(getApplicationContext(),"Salvo",Toast.LENGTH_LONG).show();

                    } else {
                        idgenero[h] = (int) new Operacaodao(context).inserir_genre(go);
                        //new Operacaodao(context).inserir_genremovie(idgenero, idfilme);


                    }
                }

                int filge[];
                filge = new Operacaodao(context).listarfilmeporgenero_id((int) id);

                int l;
                int tamanho = filge.length;
                Log.d("meuapp", tamanho + " tamanho");
                for (l = 0; l < idgenero.length; l++) {

                    if (l <= tamanho - 1 && tamanho != 0) {
                        Log.d("meuapp", String.valueOf(idgenero[l]));
                        new Operacaodao(context).alterar_genremovie(filge[l], idfilme, idgenero[l]);
                    } else {
                        new Operacaodao(context).inserir_genremovie(idgenero[l], idfilme);
                    }
                    String[] kl;
                    kl = new Operacaodao(context).listarfilmeporgenero(idfilme);
                    Log.d("meuapp", kl[0]);

                }
                et_tittle.setText("");
                et_image.setText("");
                et_rating.setText("");
                etry.setText("");
                genero1.setText("");
                genero2.setText("");
                genero3.setText("");
                getIntent().putExtra("tipo", "novo");
                getIntent().putExtra("id", "0");
                Toast.makeText(getApplicationContext(), "Alterado", Toast.LENGTH_LONG).show();
            } else {
                idfilme = (int) new Operacaodao(context).inserir_filme(newfilme);

                int h;
                for (h = 0; h < newfilme.getGenre().length; h++) {

                    Genre go = new Genre();
                    go.setGenre(newfilme.getGenre()[h]);

                    int idgenero;
                    ArrayList<Genre> lgenre = new ArrayList<Genre>();
                    lgenre = new Operacaodao(context).listar(go.getGenre().toUpperCase());

                    if (lgenre.size() > 0) {

                        if (lgenre.get(0).getGenre() != go.getGenre().toUpperCase()) {
                            idgenero = (int) new Operacaodao(context).inserir_genre(go);
                        } else {
                            idgenero = lgenre.get(0).getId();
                        }
                        new Operacaodao(context).inserir_genremovie(idgenero, idfilme);

                    } else {
                        idgenero = (int) new Operacaodao(context).inserir_genre(go);
                        new Operacaodao(context).inserir_genremovie(idgenero, idfilme);


                    }
                    et_tittle.setText("");
                    et_image.setText("");
                    et_rating.setText("");
                    etry.setText("");
                    genero1.setText("");
                    genero2.setText("");
                    genero3.setText("");

                    Toast.makeText(getApplicationContext(), "Salvo", Toast.LENGTH_LONG).show();
                }
            }
            // Log.d("meuapp", ano +" " + titulo+ " " + image + " " + reating +" " + ge1 +" " + ge2 + " " + ge3) ;
        }
    }

    public void excluir(View v){

        if(tipoOperacao.equals("alteracao")) {

            int[]kl;
            kl = new Operacaodao(context).listarfilmeporgenero_id((int)id);
            int l;
            int tamanho = kl.length;
            Log.d("meuapp",tamanho+" tamanho");
            for(l=0;l<tamanho;l++){
                new Operacaodao(context).deletegenoerofilme(kl[l]);
            }

            new Operacaodao(context).deletefilme(id);
            et_tittle.setText("");
            et_image.setText("");
            et_rating.setText("");
            etry.setText("");
            genero1.setText("");
            genero2.setText("");
            genero3.setText("");
            getIntent().putExtra("tipo","novo");
            getIntent().putExtra("id","0");
            Toast.makeText(getApplicationContext(), "Excluido Com Sucesso", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(getApplicationContext(), "È preciso ter cadastrado para poder exclui", Toast.LENGTH_SHORT).show();
        }
    }

}