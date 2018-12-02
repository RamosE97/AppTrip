package com.example.ernestoramos.apptrip.Fragmentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.Inicio;
import com.example.ernestoramos.apptrip.ItemLugares;
import com.example.ernestoramos.apptrip.R;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.RestaurantesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantesFragment extends Fragment implements  Response.Listener<JSONObject>, Response.ErrorListener, RestaurantesAdapter.OnItemClickListener {
    final static String URL_WEB_SERVICE = "https://sonsotrip.webcindario.com/Modelos/ListaRestaurantes.php";
    //Declaracion de variables a utilizar
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progeso;
    ListView lstR;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragmento_restaurantes, container,false);
        lstR=v.findViewById(R.id.lstR);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(isAdded() && getContext()!=null && getActivity()!=null) {
            progeso = new ProgressDialog(getContext());
            requestQueue = Volley.newRequestQueue(getContext());
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if(Inicio.lstRestaurantes.size()<1){
                LlamarWebServices();
            }else{
                RestaurantesAdapter adapter=new RestaurantesAdapter(getContext(), Inicio.lstRestaurantes, this);
                lstR.setAdapter(adapter);
            }
        }
    }

    private void LlamarWebServices(){
        progeso.setMessage(getString(R.string.MensajeCarga));
        progeso.show();
        URL_WEB_SERVICE.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL_WEB_SERVICE, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.dismiss();
        Log.e("onError", error.getMessage());
        Toast.makeText(getContext(), "Error al descargar datos de restaurantes", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progeso.dismiss();
        JSONArray json=response.optJSONArray("Restaurante");
        JSONObject jsonObject=null;
        Inicio.lstRestaurantes.clear();
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
                objLugares.setLatitud(jsonObject.getString("latitud"));
                objLugares.setLongitud(jsonObject.getString("longitud"));
                Inicio.lstRestaurantes.add(objLugares);
            }
            RestaurantesAdapter adapter=new RestaurantesAdapter(getContext(), Inicio.lstRestaurantes, this);
            lstR.setAdapter(adapter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(Lugares objRes, int position) {
        Intent intent=new Intent(getContext(), ItemLugares.class);
        intent.putExtra("objeto", objRes);
        startActivity(intent);
    }
}
