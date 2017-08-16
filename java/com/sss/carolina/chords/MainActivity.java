package com.sss.carolina.chords;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // благодоря этому классу мы будет разбирать данные на куски
    public Elements title;
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> titleList = new ArrayList<String>();
    // Listview Adapter для вывода данных
    private ArrayAdapter<String> adapter;
    // List view
    private ListView lv;
    Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // определение данных
        lv = (ListView) findViewById(R.id.lvMain);
        // запрос к нашему отдельному поток на выборку данных
        new NewThread().execute();
        // Добавляем данные для ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, titleList);

        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doc = Jsoup.connect("http://5lad.ru").get();
                    title = doc.select(".level1");
                    String relHref = title.attr("href");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            try {

                doc = Jsoup.connect("http://5lad.ru").get();
                title = doc.select(".level1");
                String relHref = title.attr("href");
                titleList.clear();

                for (Element titles : title) {

                    titleList.add(titles.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            // после запроса обновляем листвью
            lv.setAdapter(adapter);
        }


    }
}

