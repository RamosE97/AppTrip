package com.example.ernestoramos.apptrip;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.Sesion.Sesion;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.Utils;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener, View.OnFocusChangeListener {

    //Declaracion de controles
    TextView lblMensaje;
    TextView lblRegistro;
    Button btnIngresar;
    @NotEmpty(messageId = R.string.correo,order=1)
    EditText txtUsuario;
    @NotEmpty(messageId = R.string.contraseña,order=2)
    EditText txtClave;

    //Manejo de sesiones
    Sesion _SESION=Sesion.getInstance();
    //Declaracion de variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;
    public static String URL=null;
    //Declaracion de la variable para guardar los datos en sesion
    private SharedPreferences preferencias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicialización de variables

        preferencias=getSharedPreferences("preferencias", MODE_PRIVATE);
        lblRegistro = findViewById(R.id.lblRegistro);
        lblMensaje=findViewById(R.id.lblMensaje);
        this.txtClave = findViewById(R.id.txtClave);
        this.txtUsuario = findViewById(R.id.txtUsuario);
        btnIngresar = findViewById(R.id.btnIngresar);
        requestQueue= Volley.newRequestQueue(this);
        txtClave.setText(R.string.IngresePass);
        txtClave.setTextColor(Color.parseColor("#afafaf"));
        existePreferencia();
        //Ocultando action bar
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        //Creando los listener
        lblRegistro.setOnClickListener(this);
        btnIngresar.setOnClickListener(this);
        txtClave.setOnFocusChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void existePreferencia() {
        String email= getEmail();

        if(!TextUtils.isEmpty(email)){
            txtUsuario.setText(email);
            txtClave.setText("");
        }
        else {

        }
    }
    private String getEmail() {
        return  preferencias.getString("correo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblRegistro:
                Intent act = new Intent(MainActivity.this,Registrar.class);
                startActivity(act);
                finish();
                break;
            case R.id.btnIngresar:
                if(FormValidator.validate(this,new SimpleErrorPopupCallback(this))){
                  LlamarWebServices();
                }
                break;
        }
    }

    private void LlamarWebServices(){
        progeso=new ProgressDialog(this);
        progeso.setMessage(getString(R.string.MensajeCarga));
        progeso.show();
        String url="https://sonsotrip.webcindario.com/Modelos/Login.php?email="+txtUsuario.getText().toString()+"&credencial="+txtClave.getText().toString()+"";
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    private void GuardarDatosShared(String correo){
        SharedPreferences.Editor editor= preferencias.edit();
        editor.putString("correo",correo);
        //Guarda los valores en segundo plano
        editor.apply();
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.dismiss();
        lblMensaje.setText(R.string.ErrorDatos);
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.dismiss();
        JSONArray json=response.optJSONArray("Usuario");
        JSONObject jsonObject=null;
        try{
            for(int i=0; i<json.length();i++){
                jsonObject=json.getJSONObject(i);
            }
                Intent act = new Intent(this, Inicio.class);
                act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                _SESION.setNombre(jsonObject.getString("nombre"));
                _SESION.setCorreo(jsonObject.getString("email"));
                _SESION.setId(jsonObject.getString("id"));
                URL=jsonObject.getString("foto");

                GuardarDatosShared(_SESION.getCorreo());
                startActivity(act);
                finish();

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFocusChange(View v, boolean b) {
        switch (v.getId()) {
            case R.id.txtClave:
                String vtext=txtClave.getText().toString();
                if (b) {
                    if(txtClave.getText().toString().contains(getString(R.string.IngresePass))){
                        txtClave.setText("");
                    }else {
                        txtClave.setText(vtext);
                    }
                    txtClave.setTextColor(Color.BLACK);
                }else{
                    if(txtClave.getText().toString().isEmpty()){
                        txtClave.setText(R.string.IngresePass);
                        txtClave.setTextColor(Color.parseColor("#afafaf"));
                    }
                }
                break;

        }
    }
}
