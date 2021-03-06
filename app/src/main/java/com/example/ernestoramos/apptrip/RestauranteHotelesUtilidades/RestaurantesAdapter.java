package com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ernestoramos.apptrip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantesAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<Lugares> lstRestaurante;
    private OnItemClickListener listener;
    private LayoutInflater inflater;

    public RestaurantesAdapter(Context c, ArrayList<Lugares> lst, OnItemClickListener listener) {
        this.c = c;
        this.listener=listener;
        this.lstRestaurante = lst;
        inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return lstRestaurante.size();
    }
    @Override
    public Object getItem(int position) {
        return lstRestaurante.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface OnItemClickListener{
        void onItemClick(Lugares objRes, int position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.listarestaurante,parent,false);
        }
        TextView nombre= (TextView) convertView.findViewById(R.id.txtNombreR);
        ImageView img= (ImageView) convertView.findViewById(R.id.Imagen);
        CardView lstContainer=(CardView) convertView.findViewById(R.id.lstContainer);
        final Lugares obj = lstRestaurante.get(position);
        final int pos=position;
        nombre.setText(obj.getNombre());
        Picasso.get()
                .load(obj.url)
                .into(img);



        lstContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onItemClick(obj, pos);
            }
        });

        return convertView;
    }
}