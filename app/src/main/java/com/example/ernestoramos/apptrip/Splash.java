package com.example.ernestoramos.apptrip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Splash extends AppCompatActivity implements View.OnClickListener {

    ImageView imgLogo;
    Button btnComenzar;
    Animation uptdown,downtop;
    private static ConnectivityManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        btnComenzar = findViewById(R.id.btnComenzar);
        imgLogo = findViewById(R.id.imgLogoSplash);
        uptdown = AnimationUtils.loadAnimation(this,R.anim.uptdown);
        downtop = AnimationUtils.loadAnimation(this,R.anim.downtop);

        imgLogo.setAnimation(uptdown);
        btnComenzar.setAnimation(downtop);
        btnComenzar.setOnClickListener(this);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnComenzar:
                if(isOnline(this)){
                    if(!executeCmd("ping -c 1 -w 1 google.com", false).isEmpty()){
                        Intent act = new Intent(this,MainActivity.class);
                        startActivity(act);
                    }
                }else {
                    Intent act = new Intent(this,SinConexion.class);
                    startActivity(act);
                }
                break;
        }
    }
    public static  String executeCmd(String cmd, boolean sudo){
        try{
            Process p;
            if(!sudo)
                p=Runtime.getRuntime().exec(cmd);
            else{
                p=Runtime.getRuntime().exec(new  String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            String res="";
            while((s=stdInput.readLine())!=null){
                res+=s+"\n";
            }
            p.destroy();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected() ;
    }
}
