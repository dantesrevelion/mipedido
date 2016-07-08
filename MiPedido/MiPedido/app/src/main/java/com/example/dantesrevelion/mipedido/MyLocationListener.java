package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by vectormx on 17/05/2016.
 */
public class MyLocationListener implements LocationListener {


    static Location loc;



    public static Location getLocation(){
        return loc;
    }



    @Override
    public void onLocationChanged(Location location) {
        // Este mŽtodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la detecci—n de un cambio de ubicacion
        location.getLatitude();
        location.getLongitude();
        /*
        String Text = "Mi ubicaci—n actual es: " + "\n Lat = "
                + loc.getLatitude() + "\n Long = " + loc.getLongitude();
        messageTextView.setText(Text);
        */
        loc=location;
      //  setLocation(location,actividad);

     //   System.out.println(" Long :"+location.getLongitude()+" Lat:"+location.getLatitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
