package com.example.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class Subida extends AppCompatActivity {

    private EditText texto;
    private TextView informacion;
    private Button button;
    public final static String WEB = "http://192.168.2.246/acceso/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida);
        texto = findViewById(R.id.edtSubida);
        informacion = findViewById(R.id.txtSubida);
        button = findViewById(R.id.btnSubida);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == button){
                    subida();
                }
            }
        });
    }

    private void subida() {
        String fichero = texto.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(Subida.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            informacion.setText("Error en el fichero: " + e.getMessage());
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    //progreso.setCancelable(false);
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener(){
                        public void onCancel(DialogInterface dialog){
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    // called when response HTTP status is "200 OK"
                    progreso.dismiss();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progreso.dismiss();
                }
            });
    }
}
