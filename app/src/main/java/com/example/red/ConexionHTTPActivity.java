package com.example.red;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConexionHTTPActivity extends AppCompatActivity implements View.OnClickListener{

    EditText direccion;
    RadioButton radioJava;
    RadioButton radioApache;
    Button conectar;
    WebView web;
    TextView tiempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_http);
        iniciar();
    }

    private void iniciar() {
        direccion = findViewById(R.id.edtURL);
        radioJava = findViewById(R.id.rdbJava);
        radioApache = findViewById(R.id.rdbApache);
        conectar = findViewById(R.id.btnConectar);
        web = findViewById(R.id.wbvMostrar);
        tiempo = findViewById(R.id.txvTiempo);
        conectar.setOnClickListener(ConexionHTTPActivity.this);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    @Override
    public void onClick(View v) {
        HTTP();
    }

    private void HTTP() {
        String texto = direccion.getText().toString();
        long inicio, fin;
        Resultado resultado;
        inicio = System.currentTimeMillis();
        if (radioJava.isChecked())
            resultado = Conexion.conectarJava(texto);
        else
            resultado = Conexion.conectarApache(texto);
        fin = System.currentTimeMillis();
        if (resultado.getCodigo())
            web.loadDataWithBaseURL(null, resultado.getContenido(),"text/html", "UTF-8", null);
        else
            web.loadDataWithBaseURL(null, resultado.getMensaje(),"text/html", "UTF-8", null);
        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");

    }
}
