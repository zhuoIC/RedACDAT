package com.example.red;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;

public class Descarga extends AppCompatActivity implements View.OnClickListener {
    EditText texto;
    Button botonImagen;
    Button botonFile;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);
        texto = findViewById(R.id.editText);
        botonImagen = findViewById(R.id.button);
        botonImagen.setOnClickListener(this);
        botonFile = findViewById(R.id.button2);
        botonFile.setOnClickListener(this);
        imagen = findViewById(R.id.imageView);
    }
    @Override
    public void onClick(View v) {
        String url = texto.getText().toString();
        if (v == botonImagen)
            descargaImagen(url);
        if(v==botonFile)
            descargaFichero(url);
    }
    private void descargaImagen(String url) {

        /*Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder) // Carga
                .error(R.drawable.placeholder_error) // Error
                .into(imagen);*/
        // utilizar OkHttp3
        OkHttpClient client = new OkHttpClient();
        Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(client)).build();
        picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder) // Carga
                .error(R.drawable.placeholder_error) // Error
                .into(imagen);

    }

    private void descargaFichero(String url)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(/* Context */ this) {
            ProgressDialog pd;
            @Override
            public void onStart() {
                pd = new ProgressDialog(Descarga.this);
                pd.setTitle("Please Wait..");
                pd.setMessage("AsyncHttpResponseHadler is in progress");
                pd.setIndeterminate(false);
                pd.setCancelable(false);
                pd.show();
                Toast.makeText(Descarga.this, "Descargando...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(Descarga.this, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(Descarga.this, "It just works", Toast.LENGTH_SHORT).show();
                String destino = "/storage/sdcard/copia_"+file.getName()+".txt";
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                    FileWriter fstream = new FileWriter(destino, true);
                    BufferedWriter out = new BufferedWriter(fstream);

                    String aLine = null;
                    while ((aLine = in.readLine()) != null) {
                        out.write(aLine);
                        out.newLine();
                    }
                    in.close();
                    out.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                pd.dismiss();
            }

        });
    }
}
