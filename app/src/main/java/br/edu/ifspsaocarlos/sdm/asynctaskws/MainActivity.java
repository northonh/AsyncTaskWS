package br.edu.ifspsaocarlos.sdm.asynctaskws;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String URL_BASE = "http://www.nobile.pro.br/sdm/";
    private Button buscarInformacoesBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscarInformacoesBT = findViewById(R.id.bt_buscar_informacoes);
        buscarInformacoesBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_buscar_informacoes) {
            buscarTexto(URL_BASE + "texto.php");
        }
    }

    private void buscarTexto(String url){
        AsyncTask<String, Void, String> buscaTextoAT = new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                // Executa na thread principal, Activity
                super.onPreExecute();
                Toast.makeText(getApplicationContext(), "Buscando String no Web Service", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... strings) {
                // Executa na thread secundária
                StringBuilder sb = new StringBuilder();
                try {
                    String url = strings[0];
                    // HttpURLConnection -> InputStream -> BufferedReader -> StringBuilder -> String
                    HttpURLConnection conexao = (HttpURLConnection) (new URL(url)).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return sb.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                // Executa na thread principal, Activity
                super.onPostExecute(s);
                TextView textoTV = findViewById(R.id.tv_texto);
                textoTV.setText(s);
            }
        };

        buscaTextoAT.execute(url);
    }
}
