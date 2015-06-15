package com.lanacion.ahora.model;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Leandro on 11/04/2015.
 */
public class MyLocation implements Serializable {

    public MyLocation() {}

    public MyLocation(Location loc) {
        this.lat = loc.getLatitude();
        this.lon = loc.getLongitude();
    }

    public double lat;
    public double lon;

}
