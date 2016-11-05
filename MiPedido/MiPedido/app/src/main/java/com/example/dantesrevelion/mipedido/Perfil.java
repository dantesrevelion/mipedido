package com.example.dantesrevelion.mipedido;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Perfil extends BaseActivity {
    LocationManager mlocManager;
    MyLocationListener mlocListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        TextView titulo1 = (TextView) findViewById(R.id.txtPerfilUsuario);
        TextView titulo2 = (TextView) findViewById(R.id.txtPerfilCorreo);
        titulo1.setText(getIntent().getExtras().getString("usuario"));
        titulo2.setText(getIntent().getExtras().getString("correo"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("resquest");
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);
        System.out.println(" LAT LONG " + MyLocationListener.getLocation());
        if (MyLocationListener.getLocation() != null) {

            System.out.println(" LAT LONG " + MyLocationListener.getLocation());
            Location myloc = MyLocationListener.getLocation();



            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(myloc.getLatitude(), myloc.getLongitude(), 1);
                System.out.println("ADDRESSES " + addresses.size());
                for (int i = 0; i < addresses.size(); i++) {
                    TextView txt = (TextView) findViewById(R.id.txtPerfilLoc);
                    System.out.println("ADDRESS " + addresses.get(i).getAddressLine(0));
                    txt.setText(" " + addresses.get(i).getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(" LAT LONG "+MyLocationListener.getLocation());
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocManager.removeUpdates(mlocListener);
    }
}
