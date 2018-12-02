package com.example.ernestoramos.apptrip.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ernestoramos.apptrip.Escaner;
import com.example.ernestoramos.apptrip.Favoritos;
import com.example.ernestoramos.apptrip.Mapa.HotelesUbicaciones;
import com.example.ernestoramos.apptrip.Mapa.RestaurantesUbicaciones;
import com.example.ernestoramos.apptrip.R;
import com.example.ernestoramos.apptrip.Sesion.Sesion;

public class InicioFragment extends Fragment {
    CardView CardEscaner, CardFav, CardRes, CardHotel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragmento_inicio, container,false);
        this.CardEscaner=v.findViewById(R.id.CardEscaner);
        this.CardFav=v.findViewById(R.id.CardFav);
        this.CardRes = v.findViewById(R.id.CardRes);
        this.CardHotel = v.findViewById(R.id.CardHotel);

        this.CardEscaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext()!=null) {
                    Intent intent = new Intent(getContext(), Escaner.class);
                    startActivity(intent);
                }
            }
        });
        this.CardFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext()!=null) {
                    Intent intent = new Intent(getContext(), Favoritos.class);
                    startActivity(intent);
                }
            }
        });
        this.CardRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RestaurantesUbicaciones.class);
                startActivity(intent);
            }
        });
        this.CardHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HotelesUbicaciones.class);
                startActivity(intent);
            }
        });
        return v;
    }



}
