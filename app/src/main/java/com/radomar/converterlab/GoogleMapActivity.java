package com.radomar.converterlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.radomar.converterlab.task.AsyncGooglePlaces;
import com.radomar.converterlab.utils.Utils;

/**
 *
 * Created by Radomar on 18.09.2015
 */
public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String mCity;
    private String mAddress;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        mCity = intent.getStringExtra(Utils.KEY_CITY);
        mAddress = intent.getStringExtra(Utils.KEY_ADDRESS);

        initMap();
    }

    private void initMap() {
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentGoogleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);

//        execute asyncTask. Another way able to use callback interface to get place position
        if (Utils.getInstance(this).isOnline()) {
            new AsyncGooglePlaces() {
                @Override
                protected void onPostExecute(LatLng _position) {
                    super.onPostExecute(_position);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_position, 17));
                    googleMap.addMarker(new MarkerOptions()
                            .position(_position));
                }
            }.execute(mCity, mAddress);
        } else {
            Toast.makeText(this, "network is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

}


