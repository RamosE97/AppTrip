package com.example.ernestoramos.apptrip.Mapa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ernestoramos.apptrip.Inicio;
import com.example.ernestoramos.apptrip.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class RestaurantesUbicaciones extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static  final int LOCATION_REQUEST = 500;
    private MarkerOptions place1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantes_ubicaciones);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapRestaurantes);
        mapFragment.getMapAsync(this);
        setTitle("Restaurantes");

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Habilitamos las herramientas de zoom en el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Verificamos el permiso a la ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        //Habilitamos el boton para reconocer nuestra ubicacion
        mMap.setMyLocationEnabled(true);
        //Hacemos que se haga un zoom en el mapa en esta posicion
       LatLng sonsonate = new LatLng(13.7436094,-89.650556);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sonsonate,10));

        //recorremos la lista de restaurantes para marcar los puntos
        for(int i=0; i<Inicio.lstRestaurantes.size();i++){

            LatLng lugar = new LatLng(Double.parseDouble(Inicio.lstRestaurantes.get(i).getLatitud()),Double.parseDouble(Inicio.lstRestaurantes.get(i).getLongitud()));
            place1 = new MarkerOptions().position(lugar)
                    .title(Inicio.lstRestaurantes.get(i).getNombre())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.locrest));
            //Marcamos la ubicacion del lugar
            mMap.addMarker(place1);
        }

    }


}
