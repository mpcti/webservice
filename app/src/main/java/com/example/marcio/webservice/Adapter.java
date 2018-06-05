package com.example.marcio.webservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter extends BaseAdapter {
    Operacaodao operacaodao;
    Context context;

    public Adapter(Context context){
    this.context = context;
        operacaodao = new Operacaodao(context);
    }

    @Override
    public int getCount() {

      return operacaodao.quantidadeFilmes();
    }

    @Override
    public Object getItem(int i) {
        return operacaodao.pegarfilme(i);
    }

    @Override
    public long getItemId(int i) {
        return ( (Movies) operacaodao.pegarfilme(i)).getId_filme();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

    if(view ==null){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_listview_p,null);
    }


        WebView um = view.findViewById(R.id.img);
        TextView umtitlle = view.findViewById(R.id.tw2);
        TextView umimage = view.findViewById(R.id.tw4);
        TextView umrating = view.findViewById(R.id.tw6);
        TextView umrealeseyear = view.findViewById(R.id.tw8);
        TextView umgenero = view.findViewById(R.id.twg1);
        TextView doisgenero = view.findViewById(R.id.twg2);
        TextView tresgenero = view.findViewById(R.id.twg3);



        Movies movies = (Movies) getItem(i);
         umtitlle.setText(movies.getTitle());
         umimage.setText(movies.getImage());
         umrating.setText(String.valueOf(movies.getRating()));
         umrealeseyear.setText( String.valueOf(movies.getReleaseYear()));


        um.getSettings().setJavaScriptEnabled(true);
        um.getSettings().setSupportZoom(false);
        um.loadUrl(movies.getImage());

        String p[];
        p = new Operacaodao(context).listarfilmeporgenero(movies.getId_filme());
        movies.setGenre(p);
        //umgenero.setText("action");
        int tamanho = movies.getGenre().length;

        if (1<=tamanho) {
            umgenero.setText(movies.getGenre()[0]);
        }else {
            umgenero.setText("");
        }

        if (2<=tamanho) {
            doisgenero.setText(movies.getGenre()[1]);
        }else {
            doisgenero.setText("");
        }
        if (3<=tamanho) {
            tresgenero.setText(movies.getGenre()[2]);
        }else {
            tresgenero.setText("");
        }

        return view;
    }
}


