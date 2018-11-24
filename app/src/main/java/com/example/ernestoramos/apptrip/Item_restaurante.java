package com.example.ernestoramos.apptrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Restaurante;
import com.squareup.picasso.Picasso;

public class Item_restaurante  extends AppCompatActivity {
    private TextView txtItemNombre, txtDescripcionItem, txtDireccionItem, txtTelefonoItem;
    private ImageView ImagenItem;
    Restaurante objR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_restaurante);
        this.ImagenItem=findViewById(R.id.ImagenItem);
        this.txtItemNombre=findViewById(R.id.txtItemNombre);
        this.txtDescripcionItem=findViewById(R.id.txtDescripcionItem);
        this.txtDireccionItem=findViewById(R.id.txtDireccionItem);
        this.txtTelefonoItem=findViewById(R.id.txtTelefonoItem);

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


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
