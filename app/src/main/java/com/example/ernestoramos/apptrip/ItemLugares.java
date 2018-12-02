package com.example.ernestoramos.apptrip;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;
import com.example.ernestoramos.apptrip.Sesion.Sesion;
import com.example.ernestoramos.apptrip.directionhelpers.FetchURL;
import com.example.ernestoramos.apptrip.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemLugares extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, OnMapReadyCallback, TaskLoadedCallback {

    //Variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;
    //Urls webServices
    private final String URL_WEB_SERVICES="https://sonsotrip.webcindario.com/Modelos/AddFavorito.php?";
    private final String URL_CONSULTA="https://sonsotrip.webcindario.com/Modelos/ConsultarFavoritos.php?";
    private final String URL_ELIMINACION="https://sonsotrip.webcindario.com/Modelos/EliminarFavorito.php?";
    private final String URL_CONSULTA_ITEM="https://sonsotrip.webcindario.com/Modelos/ConsultaItemIndividual.php?";
    //Variables de control para web services
    int opc=0;
    private final int INSERTAR=1, CONSULTARFAVORITO=2, ELIMINAR=3, CONSULTARITEM=4;
    Boolean esFavorito=false;
    //Manejo de sesiones
    Sesion _SESION=Sesion.getInstance();
    //Controles
    private TextView txtItemNombre, txtDescripcionItem, txtDireccionItem, txtTelefonoItem;
    private ImageView ImagenItem, idFav;
    Lugares objR;

    //Todo lo del mapa
    private GoogleMap mMap;
    private static  final int LOCATION_REQUEST = 500;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_lugares);
        //Asignacion de controles
        this.ImagenItem=findViewById(R.id.ImagenItem);
        this.txtItemNombre=findViewById(R.id.txtItemNombre);
        this.txtDescripcionItem=findViewById(R.id.txtDescripcionItem);
        this.txtDireccionItem=findViewById(R.id.txtDireccionItem);
        this.txtTelefonoItem=findViewById(R.id.txtTelefonoItem);
        this.idFav=findViewById(R.id.idFav);

        //Inicializo la solicitud
        requestQueue= Volley.newRequestQueue(this);

        //Me permite obtener el objeto enviado desde el listado de restaurantes, descargar la imagen y en base al objeto si existe cambiar el titulo de la action bar
        if(getIntent().getSerializableExtra("objeto")!=null){
            objR= (Lugares) getIntent().getSerializableExtra("objeto");
            AsignacionValores(objR.getNombre(), objR.getDescripcion(), objR.getDireccion(), objR.getTelefono(), objR.getUrl());
            opc=CONSULTARFAVORITO;
            ConsultarFavorito();
        }else{
            String valor=getIntent().getStringExtra("IDLugar");
            if(getIntent().getStringExtra("IDLugar")!=null){
                objR= new Lugares();
                objR.setId(Integer.parseInt(valor));
                opc=CONSULTARITEM;
                ConsultarItem();
            }
        }
        //Listener a la imageView de favoritos
        idFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si el restaurante no es favorito, entonces permite la insercion
                if(!esFavorito){
                    //Permite definir insercion como verdadero y eliminacion como falsa, ya que no es favorito aun
                    opc=INSERTAR;
                    Insertar();
                }else{
                    //Define eliminacion ya que ya es favorito
                    opc=ELIMINAR;
                    EliminarFavorito();
                }
            }
        });

        //Mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);


        place2 = new MarkerOptions().position(new LatLng(Double.parseDouble(objR.getLatitud()), Double.parseDouble(objR.getLongitud()))).title("Location 2");

        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
        new FetchURL(ItemLugares.this).execute(url, "driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(place1);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBzp4eh91iI3jkC06VLB0lMJkfeYXSi_lo";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void AsignacionValores(String nom, String descrip, String direc, String Tel, String Url){
        this.txtItemNombre.setText(nom);
        this.txtDescripcionItem.setText(descrip);
        this.txtDireccionItem.setText(direc);
        this.txtTelefonoItem.setText(Tel);
        Picasso.get()
                .load(Url)
                .into(this.ImagenItem);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(nom);
        }
    }

    private void Insertar(){
        //Permite insertar o consultar en base a boolean esInsercion
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        String url=URL_WEB_SERVICES+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }
    private void ConsultarFavorito(){
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        String url=URL_CONSULTA+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }
    private void ConsultarItem(){
        //Metodo destinado solo a eliminar
        progeso=new ProgressDialog(this);
        progeso.setMessage("Cargando...");
        progeso.show();
        String url=URL_CONSULTA_ITEM+"idLugares="+objR.getId()+"&idUsuarios="+_SESION.getId();
        url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);

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
        progeso.dismiss();
        switch (opc){
            //Insertar
            case 1:
                Log.e("Error guardar favorito", error.getMessage());
                idFav.setImageResource(R.drawable.ic_nofav);
                esFavorito=false;
                break;
                //Consultar
            case 2:
                Log.e("No se pudo consultar", error.getMessage());
                break;
                //Eliminar
            case 3:
                Log.e("No se pudo eliminar", error.getMessage());
                break;
                //Item
            case 4:
                Log.e("Fallo item individual", error.getMessage());
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("EsFav",esFavorito);
        setResult(RESULT_OK,returnIntent);
        super.onBackPressed();
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.dismiss();
        switch (opc){
            //Insercion
            case 1: {
                        idFav.setImageResource(R.drawable.ic_fav);
                        esFavorito=true;
            }
                break;
                //Consultar favorito
            case 2:{
                JSONArray json=response.optJSONArray("ConsultaFavoritos");
                JSONObject jsonObject=null;
                try{
                    for(int i=0; i<json.length();i++){
                        jsonObject=json.getJSONObject(i);
                    }
                    if(jsonObject.getString("respuesta").equals("Ok")){
                        //Si devuelve un valor, es porque es favorito
                        idFav.setImageResource(R.drawable.ic_fav);
                        esFavorito=true;
                    }else{
                        //No devuelve, no es favorito
                        esFavorito=false;
                        idFav.setImageResource(R.drawable.ic_nofav);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                break;
            //Eliminacion
            case 3:
                esFavorito=false;
                idFav.setImageResource(R.drawable.ic_nofav);
                break;
                //Item
            case 4:{
                JSONArray json=response.optJSONArray("Item");
                JSONObject jsonObject=null;
                try{
                    for(int i=0; i<json.length();i++){
                        jsonObject=json.getJSONObject(i);
                    }
                    if(jsonObject.getString("respuesta").equals("Ok")){
                        AsignacionValores(jsonObject.getString("nombre"),jsonObject.getString("descripcion"),jsonObject.getString("direccion"),jsonObject.getString("telefono"),  jsonObject.getString("url") );
                        if(jsonObject.getString("fav").equals("Ok")){
                            idFav.setImageResource(R.drawable.ic_fav);
                            esFavorito=true;
                        }else{
                            //No devuelve, no es favorito
                            esFavorito=false;
                            idFav.setImageResource(R.drawable.ic_nofav);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Intente nuevamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                break;
        }
        //Primera verificacion, si no esta eliminado, pero si insertando, entonces inserto, esFavorito se vuelve verdadero y se reemplaza la iamgen
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
