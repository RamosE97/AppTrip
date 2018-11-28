package com.example.ernestoramos.apptrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.FavoritosAdapter;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;
import com.example.ernestoramos.apptrip.Sesion.Sesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favoritos extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, FavoritosAdapter.OnItemClickListener {

    //Variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;
    //Urls webServices
    private final String URL_WEB_SERVICES="https://sonsotrip.webcindario.com/Modelos/ListaFavoritos.php?";
    Sesion _SESION=Sesion.getInstance();
    ListView lstFav;
    public static ArrayList<Lugares> lstFavoritos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Favoritos");
        }
        //Inicializo la solicitud
        requestQueue= Volley.newRequestQueue(this);
        lstFav=findViewById(R.id.lstFav);
        Consulta();
    }
    private void Consulta(){
        //Permite insertar o consultar en base a boolean esInsercion
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        String url=URL_WEB_SERVICES+"&idUsuarios="+_SESION.getId();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.dismiss();
        Log.e("Error cargando lista", error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.dismiss();
        JSONArray json=response.optJSONArray("ListaFav");
        JSONObject jsonObject=null;
        lstFavoritos.clear();
        Lugares objLugares;
        try{
            for(int i=0; i<json.length();i++){
                jsonObject=json.getJSONObject(i);
                objLugares =new Lugares();
                objLugares.setId(jsonObject.getInt("id"));
                objLugares.setNombre(jsonObject.getString("nombre"));
                objLugares.setDireccion(jsonObject.getString("direccion"));
                objLugares.setDescripcion(jsonObject.getString("descripcion"));
                objLugares.setImageUrl(jsonObject.getString("url"));
                objLugares.setTelefono(jsonObject.getString("telefono"));
                lstFavoritos.add(objLugares);
            }
            FavoritosAdapter adapter=new FavoritosAdapter(this, lstFavoritos, this);
            lstFav.setAdapter(adapter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(Lugares objRes, int position) {
        Intent intent=new Intent(this, ItemLugares.class);
        intent.putExtra("objeto", objRes);
        startActivity(intent);
    }
}
