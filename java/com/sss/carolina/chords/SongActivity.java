package com.sss.carolina.chords;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SongActivity extends AppCompatActivity {

    // благодоря этому классу мы будет разбирать данные на куски
    public String title;


    private ListView lv;
    private Elements link;
    Document doc;
    TextView songTV;
    String songText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_song);
        songTV = (TextView)findViewById(R.id.songTV);
        new NewThread().execute();


    }

    public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            try {

                doc = Jsoup.connect(getIntent().getStringExtra("keyName")).get();

                title = doc.getElementsByClass("textofsong").text();


                Log.e("sd", title);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            songTV.setText(title);

        }


    }

}
