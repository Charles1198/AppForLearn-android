package com.bqteam.appforlearn.function.map;

import com.amap.api.maps.model.LatLng;

/**
 * @author charles
 * @date 2018/1/24
 */

public class Bank {
    private String name;
    private String address;
    private LatLng latLng;

    public Bank(String name, String address, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.latLng = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
