package com.example.ernestoramos.apptrip.Sesion;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.ernestoramos.apptrip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;


public class Configuracion extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnFocusChangeListener {

    Sesion _SESION=Sesion.getInstance();
    @NotEmpty(messageId = R.string.contraseña, order=5)
    @MinLength(value = 8,messageId =R.string.ContraValida, order = 1)
    EditText txtClave;
    @NotEmpty(messageId = R.string.contraseña, order=6)
    @MinLength(value = 8,messageId =R.string.ContraValida, order = 2)
    EditText txtConfirmarClave;
    Button btnGuardarConfi;
    TextView lblMensaje;
    //Variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;

    private final String URL_WEB_SERVICES="https://sonsotrip.webcindario.com/Modelos/configuracion.php?";
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        txtClave = findViewById(R.id.txtClave);
        txtConfirmarClave = findViewById(R.id.txtConfirmarClave);
        btnGuardarConfi = findViewById(R.id.btnGuardarConfi);
        lblMensaje = findViewById(R.id.lblMensaje);
        //Inicializando variable
        requestQueue= Volley.newRequestQueue(this);
        c=this;
        btnGuardarConfi.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Validacion();
               }
           }
        );
    }

    private void Validacion(){
        if(FormValidator.validate(this,new SimpleErrorPopupCallback(this))){
            if(txtClave.getText().toString().equals(txtConfirmarClave.getText().toString())) {
                LlamarWebServices();
            }else{
                lblMensaje.setText(R.string.PassNoCoinciden);
            }
        }
    }

    private void LlamarWebServices() {
        progeso=new ProgressDialog(this);
        progeso.setMessage("Enviando datos al servidor");
        progeso.show();

        String url=URL_WEB_SERVICES+"id="+ _SESION.getId().toString()+"&credencial="+txtClave.getText().toString();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.hide();
        lblMensaje.setText(R.string.Error);
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.hide();

        JSONArray json=response.optJSONArray("Configuracion");
        JSONObject jsonObject=null;
        try{
            for(int i=0; i<=1;i++){
                jsonObject=json.getJSONObject(i);
            }
            if(jsonObject.getString("respuesta").equals("Ok")){
                //Si cambio la contrasena
                Toast.makeText(getApplicationContext(), "Se guardaron los cambios",Toast.LENGTH_SHORT).show();

            }else{
                //No cambio la contrasena
                Toast.makeText(getApplicationContext(), "No se guardaron los cambios",Toast.LENGTH_SHORT).show();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }


        finish();
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        switch (v.getId()) {
            case R.id.txtClave: {
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
            }
            break;
            case R.id.txtConfirmarClave:{
                String vtext = txtConfirmarClave.getText().toString();
                if (b) {
                    if (txtConfirmarClave.getText().toString().contains(getString(R.string.IngreseConfirmPass))) {
                        txtConfirmarClave.setText("");
                    } else {
                        txtConfirmarClave.setText(vtext);
                    }
                    txtConfirmarClave.setTextColor(Color.BLACK);
                } else {
                    if (txtConfirmarClave.getText().toString().isEmpty()) {
                        txtConfirmarClave.setText(R.string.IngreseConfirmPass);
                        txtConfirmarClave.setTextColor(Color.parseColor("#afafaf"));
                    }
                }
            }
        }
    }

}
