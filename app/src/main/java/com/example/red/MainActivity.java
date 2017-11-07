package com.example.red;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnConexionHTTP,
            btnConexionAsincrona,
            btnConexionAHC,
            btnConexionVolley,
            btnDescarga,
            btnSubida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConexionHTTP = (Button) findViewById(R.id.btnConexionHTTP);
        btnConexionAsincrona = (Button) findViewById(R.id.btnConexionAsincrona);
        btnConexionVolley = (Button) findViewById(R.id.btnConexionVolley);
        btnConexionAHC = (Button) findViewById(R.id.btnConexionAHC);
        btnDescarga = (Button) findViewById(R.id.btnDescarga);
        btnSubida = (Button) findViewById(R.id.btnSubida);
        btnConexionAsincrona.setOnClickListener(this);
        btnConexionHTTP.setOnClickListener(this);
        btnConexionAHC.setOnClickListener(this);
        btnConexionVolley.setOnClickListener(this);
        btnDescarga.setOnClickListener(this);
        btnSubida.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view == btnConexionAsincrona){
            intent = new Intent(this, ConexionHTTPActivity.class);
            startActivity(intent);
        }
        if (view == btnConexionHTTP){
            intent = new Intent(this, ConexionAsincronaActivity.class);
            startActivity(intent);
        }
        if (view == btnConexionAHC){
            intent = new Intent(this, ConexionAHCActivity.class);
            startActivity(intent);
        }
        if (view == btnConexionVolley){
            intent = new Intent(this, ConexionVolleyActivity.class);
            startActivity(intent);
        }
        if (view == btnDescarga){
            intent = new Intent(this, Descarga.class);
            startActivity(intent);
        }
        if (view == btnSubida){
            intent = new Intent(this, Subida.class);
            startActivity(intent);
        }
    }
}