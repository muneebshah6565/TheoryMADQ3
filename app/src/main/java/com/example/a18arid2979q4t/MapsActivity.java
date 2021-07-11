package com.example.a18arid2979q4t;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(30, 69);
        getAPIData();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    public void getAPIData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.trackcorona.live/api/countries";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response
                    try {
                        JSONObject res = new JSONObject(response);
                        JSONArray data = res.getJSONArray("data");
                        Log.d("DATA", data.toString());
                        JSONObject obj;
                        LatLng myPos;
                        for (int i = 0; i < data.length(); i++) {
                            obj = new JSONObject(data.get(i).toString());
                            myPos = new LatLng(obj.getDouble("latitude"),obj.getDouble("longitude"));

                            mMap.addMarker(new MarkerOptions().position(myPos).title(obj.getString("location") + ": " + obj.getString("confirmed") + " Confirmed"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Log.d("ERROR","error => "+error.toString())
        );
        queue.add(getRequest);
    }
}