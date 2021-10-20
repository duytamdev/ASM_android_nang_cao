package com.tamstudio.asm_duytam.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.model.Location;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment  {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    FloatingSearchView searchView;
    Button btnLocation1,btnLocation2,btnLocation3;
    View mView;
    Location destination;
    FusedLocationProviderClient client;
    LocationRequest locationRequest;
    LocationCallback callback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_maps,container,false);
        checkPermission();
        initUI();
        initMap();
        getCurentLocation();
        actionSearchView();
        actionButtons();
        return mView;
    }

    private void actionButtons() {
        btnLocation1.setOnClickListener(view -> {
            destination = new Location("Trường Cao Đẳng FPT Polytechnic (CS1)",new LatLng(10.7908175,106.681756));
            actionAddLocation(destination);
        });
        btnLocation2.setOnClickListener(view -> {
            destination = new Location("Trường Cao Đẳng FPT Polytechnic (CS2)",new LatLng(10.8118612,106.6798565));
            actionAddLocation(destination);
        });
        btnLocation3.setOnClickListener(view -> {
            destination = new Location("Trường Cao Đẳng FPT Polytechnic (CS3)",new LatLng(10.8531827, 106.629756519));
            actionAddLocation(destination);
        });
    }
    private void actionAddLocation(Location location){
        try {
            if(location!=null){
                mMap.addMarker(new MarkerOptions().position(location.getLatLng()).title(location.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.getLatLng(), 16));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void actionSearchView() {
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }
            @Override
            public void onSearchAction(String currentQuery) {
                String location = searchView.getQuery();
                List<Address> addressList = new ArrayList<>();
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location,1);
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    destination = new Location(null,latLng);
                    mMap.addMarker(new MarkerOptions().position(destination.getLatLng()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Không tìm thấy địa điểm",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initUI() {
        searchView = mView.findViewById(R.id.search_location);
        btnLocation1 = mView.findViewById(R.id.btn_location_1);
        btnLocation2 = mView.findViewById(R.id.btn_location_2);
        btnLocation3 = mView.findViewById(R.id.btn_location_3);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getCurentLocation() {
        client = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (android.location.Location location: locationResult.getLocations()) {
                    if(location!= null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude,longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(latitude+" - "+longitude));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                    }
                }

            }
        };
        client.requestLocationUpdates(locationRequest,callback,null);
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permission, PERMISSION_REQUEST_CODE);
            }
        }
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Vui long cho phep truy dinh vi", Toast.LENGTH_SHORT).show();
            }
        }else{
            getCurentLocation();
        }
    }

}
