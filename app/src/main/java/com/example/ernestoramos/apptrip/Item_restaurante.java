package com.example.ernestoramos.apptrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Restaurante;
import com.example.ernestoramos.apptrip.Sesion.Sesion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item_restaurante  extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    //Variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;
    //Variables de control para web services
    Boolean esInsercion=false, esFavorito=false, esEliminacion=false;
    //Urls webServices
    private final String URL_WEB_SERVICES="https://sonsotrip.webcindario.com/Modelos/AddFavorito.php?";
    private final String URL_CONSULTA="https://sonsotrip.webcindario.com/Modelos/ConsultarFavoritos.php?";
    private final String URL_ELIMINACION="https://sonsotrip.webcindario.com/Modelos/EliminarFavorito.php?";
    //Manejo de sesiones
    Sesion _SESION=Sesion.getInstance();
    //Controles
    private TextView txtItemNombre, txtDescripcionItem, txtDireccionItem, txtTelefonoItem;
    private ImageView ImagenItem, idFav;
    Restaurante objR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_restaurante);
        //Asignacion de controles
        this.ImagenItem=findViewById(R.id.ImagenItem);
        this.txtItemNombre=findViewById(R.id.txtItemNombre);
        this.txtDescripcionItem=findViewById(R.id.txtDescripcionItem);
        this.txtDireccionItem=findViewById(R.id.txtDireccionItem);
        this.txtTelefonoItem=findViewById(R.id.txtTelefonoItem);
        this.idFav=findViewById(R.id.idFav);

        //Me permite obtener el objeto enviado desde el listado de restaurantes, descargar la imagen y en base al objeto si existe cambiar el titulo de la action bar
        if(getIntent().getSerializableExtra("ObjetoRestaurante")!=null){
            objR= (Restaurante) getIntent().getSerializableExtra("ObjetoRestaurante");
            this.txtItemNombre.setText(objR.getNombre());
            this.txtDescripcionItem.setText(objR.getDescripcion());
            this.txtDireccionItem.setText(objR.getDireccion());
            this.txtTelefonoItem.setText(objR.getTelefono());
            Picasso.get()
                    .load(objR.getUrl())
                    .into(this.ImagenItem);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(objR.getNombre());
            }

        }
        //Inicializo la solicitud
        requestQueue= Volley.newRequestQueue(this);
        //Listener a la imageView de favoritos
        idFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Si el restaurante no es favorito, entonces permite la insercion
                if(!esFavorito){
                    //Permite definir insercion como verdadero y eliminacion como falsa, ya que no es favorito aun
                    esInsercion =true;
                    esEliminacion=false;
                    LlamarWebServices();
                }else{
                    //Define eliminacion ya que ya es favorito
                    esEliminacion=true;
                    EliminarFavorito();
                    esInsercion=false;
                }
            }
        });
        esInsercion=false;
        LlamarWebServices();
    }

    private void LlamarWebServices(){
        //Permite insertar o consultar en base a boolean esInsercion
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        if(esInsercion){
            String url=URL_WEB_SERVICES+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
            url.replace(" ","%20");
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        }else{
            String url=URL_CONSULTA+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
            url.replace(" ","%20");
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void EliminarFavorito(){
        //Metodo destinado solo a eliminar
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        String url=URL_ELIMINACION+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.hide();
        //Primera verificacion, si no esta eliminado, pero si insertando, fallo en la insercion
        if(esInsercion && !esEliminacion){
            Log.e("Error guardar favorito", error.getMessage());
            idFav.setImageResource(R.drawable.ic_nofav);
        }else{
            //Si no esta insertando y tampoco eliminando, esta consultando, fallo en consultar
            if(!esEliminacion){
                Log.e("No se pudo consultar", error.getMessage());
                esFavorito=false;
            }else{
                //No inserta, no consulta, entonces elimina
                Log.e("No se pudo eliminar", error.getMessage());
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.hide();
        //Primera verificacion, si no esta eliminado, pero si insertando, entonces inserto, esFavorito se vuelve verdadero y se reemplaza la iamgen
        if(esInsercion && !esEliminacion){
                idFav.setImageResource(R.drawable.ic_fav);
                esFavorito=true;
        }else{
            //No esta eliminando, ni insertando entonces se consulta
            if(!esEliminacion){
                JSONArray json=response.optJSONArray("ConsultaFavoritos");
                JSONObject jsonObject=null;
                try{
                    for(int i=0; i<json.length();i++){
                        jsonObject=json.getJSONObject(i);
                    }
                    if(json.length()>0){
                        //Si devuelve un valor, es porque es favorito
                        idFav.setImageResource(R.drawable.ic_fav);
                        esFavorito=true;
                    }else{
                        //No devuelve, no es favorito
                        idFav.setImageResource(R.drawable.ic_nofav);
                        esFavorito=false;
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                //Se elimino con exito, se desmarca esFavorito
                idFav.setImageResource(R.drawable.ic_nofav);
                esFavorito=false;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
