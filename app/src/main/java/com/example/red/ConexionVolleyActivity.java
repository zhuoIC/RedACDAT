package com.example.red;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class ConexionVolleyActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MyTag";
    EditText mEditText;
    Button mButton;
    WebView mWebView;
    TextView mTextView;
    RequestQueue mRequestQueue;
    long inicio, fin;

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_volley);
        mEditText = findViewById(R.id.edtURL);
        mButton = findViewById(R.id.btnConectar);
        mButton.setOnClickListener(this);
        mWebView = findViewById(R.id.wbvMostrar);
        mTextView = findViewById(R.id.txvTiempo);
    }

    @Override
    public void onClick(View view) {
        String url;
        if (view == mButton) {
            url = mEditText.getText().toString();
            inicio = System.currentTimeMillis();
            makeRequest(url);
        }
    }

    public void makeRequest(String url) {
        final String enlace = url;
        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(this);

        final ProgressDialog progreso = new ProgressDialog(ConexionVolleyActivity.this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando . . .");
        progreso.setCancelable(true);
        progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                    mRequestQueue.cancelAll(TAG);
            }
        });
        progreso.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,  url,
                new Response.Listener<String>() {
                    @Override
                    public void
                    onResponse(String response) {
                        fin = System.currentTimeMillis();
                        progreso.dismiss();
                        mWebView.loadDataWithBaseURL(null, response.toString(), "text/html", "UTF-8", null);
                        mTextView.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = "Error";
                        fin = System.currentTimeMillis();
                        progreso.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError)
                            mensaje = "Timeout Error: " + error.getMessage();
                        else {
                            NetworkResponse errorResponse = error.networkResponse;
                            if (errorResponse != null && errorResponse.data != null)
                                try {
                                    mensaje = "Error: " + errorResponse.statusCode + " " + "\n" + new
                                            String(errorResponse.data, "UTF-8");
                                    Log.e("Error", mensaje);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    mensaje = "Error sin información";
                                }
                        }
                        mWebView.loadDataWithBaseURL(null,mensaje,"text/html", "UTF-8", null);
                        mTextView.setText("Duración: "+ String.valueOf(fin-inicio) + " milisegundos");
                    }
                });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

}
