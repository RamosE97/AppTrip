package com.example.ernestoramos.apptrip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import com.example.ernestoramos.apptrip.Fragmentos.HotelesFragment;
import com.example.ernestoramos.apptrip.Fragmentos.InicioFragment;
import com.example.ernestoramos.apptrip.Fragmentos.PerfilFragment;
import com.example.ernestoramos.apptrip.Fragmentos.RestaurantesFragment;
import com.example.ernestoramos.apptrip.RestauranteHotelesUtilidades.Lugares;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager pager;

    public static ArrayList<Lugares> lstRestaurantes = new ArrayList<>();
    public static ArrayList<Lugares> lstHoteles = new ArrayList<>();
    //Manejo de sesiones
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        bottomNavigationView = findViewById(R.id.bottom_nav);
        pager = findViewById(R.id.fragment_container);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int opc = 0;
                switch (item.getItemId()) {
                    case R.id.Inicio:
                        opc = 0;
                        break;
                    case R.id.Hotel:
                        opc = 1;
                        break;
                    case R.id.Restaurante:
                        opc = 2;
                        break;
                    case R.id.Perfil:
                        opc = 3;
                        break;
                }
                pager.setCurrentItem(opc);
                return true;
            }
        });

        pager.setAdapter(new MyViewPager(getSupportFragmentManager()));

    }



    class MyViewPager extends FragmentPagerAdapter {
        public MyViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch (i) {
                case 0: // Fragment # 0 - This will show FirstFragment

                    return new InicioFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title

                    return new HotelesFragment();
                case 2: // Fragment # 1 - This will show SecondFragment

                    return new RestaurantesFragment();
                case 3:
                    return new PerfilFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
