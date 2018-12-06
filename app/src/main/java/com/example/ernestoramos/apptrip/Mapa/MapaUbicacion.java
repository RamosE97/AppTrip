package com.example.ernestoramos.apptrip.Mapa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ernestoramos.apptrip.R;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapaUbicacion extends AppCompatActivity implements OnMapReadyCallback {

    //Todo lo del mapa
    private GoogleMap mMap;
    private static  final int LOCATION_REQUEST = 500;
    private MarkerOptions place1;
    LatLng punto;
    Lugares objR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ubicacion);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
        //Obtenemos el objeto
        Bundle objRecibido =getIntent().getExtras();
        if(objRecibido!=null) {
            objR = (Lugares) objRecibido.getSerializable("lugar");
            //Asignamos la geolocalizacion al punto
            punto = new LatLng(Double.parseDouble(objR.getLatitud()), Double.parseDouble(objR.getLongitud()));
            //Asignamos la posicion del punto
            place1 = new MarkerOptions().position(punto).title(objR.getNombre());
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(objR.getNombre());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Activamos las herramientas de Zoom en el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Verificamos los permisos de ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        //Activamos la herramienta para encontrar nuestra ubicacion actual
        mMap.setMyLocationEnabled(true);
        //Se marca en el mapa el punto asignado
        mMap.addMarker(place1);
        //Dirige la vista hacia el punto en el mapa
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto,16));

    }
}
