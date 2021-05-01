package com.tryone.dyplomtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tryone.dyplomtest1.constants.Constants;

import java.io.IOException;
import java.util.Locale;

public class MapActivityTest extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap map;
    private Geocoder geocoder;
    private Marker testMarker;
    private TextView tvMapAddress, tvMapCoordinates;
    private String adminArea, locality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localize();
        setContentView(R.layout.activity_map_test);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTest);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        tvMapAddress=findViewById(R.id.tvMapAddress);
        tvMapCoordinates=findViewById(R.id.tvMapCoordinates);
    }

    private void localize() {
        String languageToLoad = "ru"; //here you can select your system local language too...
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng= new LatLng(56.284538, 43.924212);
        map.setOnMapLongClickListener(this);
        testMarker=map.addMarker(new MarkerOptions().position(latLng).title("Вы здесь").draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
        Address address;
        try {
            address = geocoder.getFromLocation(testMarker.getPosition().latitude,testMarker.getPosition().longitude,1).get(0);
            tvMapAddress.setText(address.getAddressLine(0));
            tvMapCoordinates.setText("["+latLng.latitude+", "+latLng.longitude+"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        testMarker.setPosition(latLng);
        try {
            Address address=geocoder.getFromLocation(testMarker.getPosition().latitude,testMarker.getPosition().longitude,1).get(0);
            tvMapAddress.setText(address.getAddressLine(0));
            tvMapCoordinates.setText("["+latLng.latitude+", "+latLng.longitude+"]");
            adminArea=address.getAdminArea();
            Log.d("Marker data","Admin area:"+adminArea);
            locality=address.getLocality();
            Log.d("Marker data","locality:"+locality);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //map.addMarker(new MarkerOptions().position(latLng).title("The Place").draggable(true));
    }


    public void onMapClick(LatLng latLng) {
        //map.addMarker(new MarkerOptions().position(latLng).title("The Place").draggable(true));
        Log.d("TEST","HELLO");
        Log.d("POSiTION","HERE: "+testMarker.getPosition().latitude+" : "+testMarker.getPosition().longitude);
    }

    public void onClickGetMark(View view) {
        Intent i=new Intent(MapActivityTest.this,CreateTicketActivity.class);
        i.putExtra(Constants.MAP_ADDRESS_EXTRA,tvMapAddress.getText().toString());
        i.putExtra(Constants.MAP_COORDS_EXTRA_LATITUDE,testMarker.getPosition().latitude);
        i.putExtra(Constants.MAP_COORDS_EXTRA_LONGITUDE,testMarker.getPosition().longitude);
        i.putExtra(Constants.MAP_ADMIN_AREA_EXTRA,adminArea);
        i.putExtra(Constants.MAP_LOCALITY_EXTRA,locality);

        startActivity(i);
    }
}