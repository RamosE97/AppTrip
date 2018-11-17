package com.example.ernestoramos.apptrip.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ernestoramos.apptrip.Inicio;
import com.example.ernestoramos.apptrip.MainActivity;
import com.example.ernestoramos.apptrip.R;
import com.example.ernestoramos.apptrip.Sesion.Sesion;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment  extends Fragment {
    Sesion _SESION=Sesion.getInstance();
    final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    private int PICK_IMAGE_REQUEST = 2;
    private String UPLOAD_URL ="http://tidesignsolutions.com/esperanzayvida/Modelos/PefilFotoUsuario.php?";
    private String KEY_IMAGEN = "foto";
    private String KEY_ID = "id";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    Bitmap bitmap;
    ImageView addPic, profilePic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Creas un objeto view para retornar después el fragment
        View v =inflater.inflate(R.layout.fragmento_perfil, container,false);

        //Inicializas los controles
        TextView lblNombre=(TextView)v.findViewById(R.id.lblINombre);
        lblNombre.setText(_SESION.getNombre());
        TextView lblCorreo=(TextView)v.findViewById(R.id.lblICorreo);
        lblCorreo.setText(_SESION.getCorreo());
        addPic=(ImageView)v.findViewById(R.id.addPic);
        profilePic=(ImageView)v.findViewById(R.id.profilePic);
        Button btnCerrarSesion=(Button)v.findViewById(R.id.btnCerrarSesion);

        //Variable adicional
        requestQueue = Volley.newRequestQueue(getContext());

        //Listener en una sóla linea
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirGaleria();
            }
        });

        return v;
    }
    private void AbrirGaleria() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    private void CargarWebServices() {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Subiendo...", "Espere por favor...", false, false);
        stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("registra")){
                    loading.dismiss();
                    Toast.makeText(getContext(),"Se registro con éxito",Toast.LENGTH_LONG).show();
                }else{
                    loading.dismiss();
                    Toast.makeText(getContext(),"No se registro con éxito",Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                String imagen = convertirImgString(bitmap);
                Map<String ,String> parametros = new HashMap<>();
                parametros.put(KEY_ID,_SESION.getId());
                parametros.put(KEY_IMAGEN,imagen);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imagenbyte = array.toByteArray();
        String imagenstring = Base64.encodeToString(imagenbyte,Base64.DEFAULT);
        return imagenstring;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return  image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri SelectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = getBitmapFromUri(SelectedImage);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), SelectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profilePic.setImageBitmap(bitmap);
                CargarWebServices();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void logOut(){
        Intent inten= new Intent(getActivity(),MainActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _SESION.CerrarSesion();
        startActivity(inten);
    }
}
