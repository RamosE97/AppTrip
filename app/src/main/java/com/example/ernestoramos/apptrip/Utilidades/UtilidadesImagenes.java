package com.example.ernestoramos.apptrip.Utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class UtilidadesImagenes {


    public static byte[] getImagenEnBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }

    //obtengo una imagen desde la ruta de archivo
    public static Bitmap getImagenDesdeRuta(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    //reduzco la imagen comprimiendola
    public static Bitmap reducirImagen(Bitmap original){
        //reduciendo imagen a formato JPG y con calidad del 70%
        Bitmap reducedBitmap = original;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        reducedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] imageInByte = stream.toByteArray();
        float lengthbmp = imageInByte.length;
        lengthbmp /= 1024;//to Kb
        //reduciendo aun mas a 1/4 del tamanio
        reducedBitmap = Bitmap.createScaledBitmap (reducedBitmap,(int) (reducedBitmap.getWidth() * .4), (int) (reducedBitmap.getHeight() * .4),true);

        //si la imagen pesa mas de 200kb se reduce aun mas
        if(lengthbmp>200.00){
            stream = new ByteArrayOutputStream();
            reducedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            imageInByte = stream.toByteArray();
            lengthbmp = imageInByte.length;
            lengthbmp /= 1024;//to Kb
        }
        //si la imagen apesar de las reducciones pesa mas de 1MB entonces no es una imagen valida
        if(lengthbmp>=1024){
            return null;
        }else{
            return reducedBitmap;
        }
    }



}
