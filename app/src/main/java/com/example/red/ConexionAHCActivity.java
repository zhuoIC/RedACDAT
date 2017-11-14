package com.example.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ConexionAHCActivity extends AppCompatActivity implements View.OnClickListener{

    Button conectar;
    TextView tiempo;
    EditText url;
    WebView web;
    long inicio,fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_aahc);
        iniciar();

    }

    private void iniciar() {
        conectar = findViewById(R.id.btnConectar);
        tiempo = findViewById(R.id.txvTiempo);
        url = findViewById(R.id.edtURL);
        web = findViewById(R.id.wbvMostrar);
        conectar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AAHC();

    }

    private void AAHC() {
        final String texto = url.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(ConexionAHCActivity.this);
        inicio = System.currentTimeMillis();
        RestClient.get(texto, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                //progreso.setCancelable(false);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                            RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, responseString.toString(), "text/html", "UTF-8", null);
                tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, responseString.toString(), "text/html", "UTF-8", null);
                tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
            }
        });
    }
}
