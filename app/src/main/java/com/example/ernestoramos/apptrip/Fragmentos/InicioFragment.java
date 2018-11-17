package com.example.ernestoramos.apptrip.Fragmentos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ernestoramos.apptrip.R;
import com.example.ernestoramos.apptrip.Sesion.Sesion;

public class InicioFragment extends Fragment {
    Sesion _SESION=Sesion.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_inicio, container,false);

    }
}
