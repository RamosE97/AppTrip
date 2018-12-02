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

public class HotelesUbicaciones extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static  final int LOCATION_REQUEST = 500;
    private MarkerOptions place1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles_ubicaciones);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapHoteles);
        mapFragment.getMapAsync(this);
        setTitle("Hoteles");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng sonsonate = new LatLng(13.7436094,-89.650556);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sonsonate,10));
        for(int i = 0; i< Inicio.lstHoteles.size(); i++){
            LatLng lugar = new LatLng(Double.parseDouble(Inicio.lstHoteles.get(i).getLatitud()),Double.parseDouble(Inicio.lstHoteles.get(i).getLongitud()));
            place1 = new MarkerOptions().position(lugar)
                    .title(Inicio.lstHoteles.get(i).getNombre())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.lochotel));
            mMap.addMarker(place1);
        }

    }
}
