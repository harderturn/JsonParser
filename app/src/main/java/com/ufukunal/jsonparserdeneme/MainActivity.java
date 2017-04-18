package com.ufukunal.jsonparserdeneme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView tv,tv1,tv2,tv23,tv3,tv4;
    private LinearLayout linearLayout;
    private TableLayout ll;
    private Button btnPuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPuan = (Button)findViewById(R.id.btnPuan);
        tv = (TextView)findViewById(R.id.deneme);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new backgroundTask().execute();
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        ll = (TableLayout) findViewById(R.id.main_table);

        btnPuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PuanDurumuActivity.class);
                startActivity(i);
            }
        });
    }


    private class backgroundTask extends AsyncTask<Void,Integer,Void> {

        private List<Fikstur> liste;

        public backgroundTask() {
            liste = new ArrayList<>();
        }
        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect("http://www.tffistanbul.org/puantajvefikstur.aspx?ligid=1&haftaid=20").timeout(0).get();
                Elements evSahibleri = document.select("#aspnetForm > div.container.maincontainer > div > div.col-md-9.mainleftcol > center > div:nth-child(6) > div:nth-child(1) > table > tbody > tr > td > table > tbody > tr > td:nth-child(1) > span");
                Elements evSahibiSkorlari = document.select("#aspnetForm > div.container.maincontainer > div > div.col-md-9.mainleftcol > center > div:nth-child(6) > div:nth-child(1) > table > tbody > tr > td > table > tbody > tr > td:nth-child(2) > span");
                Elements deplasmanlar = document.select("#aspnetForm > div.container.maincontainer > div > div.col-md-9.mainleftcol > center > div:nth-child(6) > div:nth-child(1) > table > tbody > tr > td > table > tbody > tr > td:nth-child(4) > span");
                Elements deplasmanSkorlari = document.select("#aspnetForm > div.container.maincontainer > div > div.col-md-9.mainleftcol > center > div:nth-child(6) > div:nth-child(1) > table > tbody > tr > td > table > tbody > tr > td:nth-child(3) > span");
//
                for(int i=0;i<evSahibleri.size();i++){
                   Fikstur fikstur = new Fikstur();
                    fikstur.setEvSahibi(evSahibleri.get(i).text());
                    fikstur.setEvSahibiSkor(evSahibiSkorlari.get(i).text());
                    fikstur.setDeplasman(deplasmanlar.get(i).text());
                    fikstur.setDeplasmanSkor(deplasmanSkorlari.get(i).text());
                    liste.add(fikstur);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (int i = 0;i<6;i++){

                TableRow tr_head = new TableRow(MainActivity.this);
                if(i%2!=0) tr_head.setBackgroundColor(Color.GRAY);
                tr_head.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                tv1 = new TextView(MainActivity.this);
                tv1.setText(liste.get(i).getEvSahibi());
                tv1.setTextColor(Color.WHITE);
                tr_head.addView(tv1);

                tv2 = new TextView(MainActivity.this);
                tv2.setText(liste.get(i).getEvSahibiSkor());
                tv2.setTextColor(Color.WHITE);
                tr_head.addView(tv2);

                tv23 = new TextView(MainActivity.this);
                tv23.setText("-");
                tv23.setTextColor(Color.WHITE);
                tr_head.addView(tv23);

                tv3 = new TextView(MainActivity.this);
                tv3.setText(liste.get(i).getDeplasmanSkor());
                tv3.setTextColor(Color.WHITE);
                tr_head.addView(tv3);

                tv4 = new TextView(MainActivity.this);
                tv4.setText(liste.get(i).getDeplasman());
                tv4.setTextColor(Color.WHITE);
                tr_head.addView(tv4);


                ll.addView(tr_head, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Bilgilendirme");
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.show();
        }

    }

}
