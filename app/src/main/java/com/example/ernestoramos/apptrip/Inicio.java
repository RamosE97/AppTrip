package com.example.ernestoramos.apptrip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ernestoramos.apptrip.Fragmentos.HotelesFragment;
import com.example.ernestoramos.apptrip.Fragmentos.InicioFragment;
import com.example.ernestoramos.apptrip.Fragmentos.RestaurantesFragment;
import com.example.ernestoramos.apptrip.Mapas.ubicacion;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager pager;
    private SharedPreferences preferencias;
    String SPNombre, SPCorreo;
    private MenuItem prevMenuItem;
    TextView lblCorreo, lblNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                }
                pager.setCurrentItem(opc);
                return true;
            }
        });

        pager.setAdapter(new MyViewPager(getSupportFragmentManager()));
        ///Inicializamos las variables
        preferencias = getSharedPreferences("preferencias", MODE_PRIVATE);
        SPNombre=preferencias.getString("nombre", "");
        SPCorreo=preferencias.getString("correo", "");
        View hView =  navigationView.getHeaderView(0);
        lblCorreo=(TextView) hView.findViewById(R.id.lblICorreo);
        lblNombre=(TextView) hView.findViewById(R.id.lblINombre);

        //Asignamos variables
        lblNombre.setText(SPNombre);
        lblCorreo.setText(SPCorreo);
        //Fin onCreate
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
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
Intent intent = new Intent(this, ubicacion.class);
startActivity(intent);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.Cerrar_sesion) {
                logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logOut(){
        Intent inten= new Intent(Inicio.this,MainActivity.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inten);
    }

}
