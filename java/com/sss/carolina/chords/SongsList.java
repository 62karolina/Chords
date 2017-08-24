package com.sss.carolina.chords;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SongsList extends AppCompatActivity implements Serializable {


    // благодоря этому классу мы будет разбирать данные на куски
    public Elements title;
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> titleList = new ArrayList<String>();
    public ArrayList<String> titleList1 = new ArrayList<String>();

    // Listview Adapter для вывода данных
    private ArrayAdapter<String> adapter;
    // List view
    private ListView lv;
    Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);
        // определение данных
        lv = (ListView) findViewById(R.id.lvSongList);
        // запрос к нашему отдельному поток на выборку данных
        new NewThread().execute();
        // Добавляем данные для ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, titleList1);

        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catName = "";
                for (int i = 0; i < titleList.size(); i++) {
                    catName = catName + titleList.get(i) + " ";
                }
                Log.d("gfg", catName);

                Intent intent = new Intent(SongsList.this, SongActivity.class);
                intent.putExtra("keyName", titleList.get(position));

                startActivity(intent);


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

                doc = Jsoup.connect(getIntent().getStringExtra("keyName")).get();
                title = doc.select(".level2");
                Elements mBody = title.select("a");
                //   mBody.select("a");
                Elements urls = mBody.tagName("href");
                // link = title.select(".href");

                titleList.clear();
                for (Element i : urls) {
                    titleList.add(i.attr("abs:href"));
                }

                for (Element titles : title) {

                    titleList1.add(titles.text());
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
