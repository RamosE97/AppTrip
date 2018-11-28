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
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.HotelesAdapter;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelesFragment extends Fragment implements  Response.Listener<JSONObject>, Response.ErrorListener, HotelesAdapter.OnItemClickListener {
final static String URL_WEB_SERVICE = "https://sonsotrip.webcindario.com/Modelos/ListaHoteles.php";
        //Declaracion de variables a utilizar
        RequestQueue requestQueue;
        JsonObjectRequest jsonObjectRequest;
        ProgressDialog progeso;
        ListView lstH;


@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragmento_hoteles, container,false);
        lstH=v.findViewById(R.id.lstH);
        return v;
        }


@Override
public void onStart() {
        super.onStart();
        if(isAdded() && getContext()!=null && getActivity()!=null) {
        progeso = new ProgressDialog(getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(Inicio.lstHoteles.size()<1){
            LlamarWebServices();
        }else{
            HotelesAdapter adapter=new HotelesAdapter(getContext(), Inicio.lstHoteles, this);
            lstH.setAdapter(adapter);
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
        JSONArray json=response.optJSONArray("Hoteles");
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
                Inicio.lstHoteles.add(objLugares);
            }
                HotelesAdapter adapter=new HotelesAdapter(getContext(), Inicio.lstHoteles, this);
                lstH.setAdapter(adapter);
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
