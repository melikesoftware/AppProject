package com.example.projecthomework.map;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;


import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projecthomework.PlacesFilter;
import com.example.projecthomework.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projecthomework.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ActivityResultLauncher<String> permissionLauncher;
    private FusedLocationProviderClient fusedLocationClient;


    private LatLng userLatLng;


    private String city;

    private String town;
    private String category;

    private String keyword1;
    private String keyword2;
    private String keyword3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        registerLauncher();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        town = intent.getStringExtra("town");
        category = intent.getStringExtra("category");
        keyword1 = intent.getStringExtra("keyword1");
        keyword2 = intent.getStringExtra("keyword2");
        keyword3 = intent.getStringExtra("keyword3");





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        readJson();

        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (hour >= 19 || hour < 6) {
            try {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_night));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Permission needed for map", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give permission", v -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)).show();
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            getUserLocation();
        }
    }

    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Benim Konumum"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                } else {
                    Toast.makeText(this, "Konum alınamadı", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), (ActivityResultCallback<Boolean>) o -> {
            if (o) {
                getUserLocation();
            } else {
                Toast.makeText(MapsActivity.this, "Permission needed for map", Toast.LENGTH_LONG).show();
            }
        });
    }


    public boolean onMarkerClick(@NonNull Marker marker) {
        String message = "Başlık: " + marker.getTitle() +
                "\nKonum: " + marker.getPosition().toString() +
                "\nAçıklama: " + marker.getSnippet();


        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;


    }





    public void readJson(){

        String jsonString=null;

        try {
            InputStream inputStream=getAssets().open("places.json");
            int size=inputStream.available();
            byte[]buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();
           jsonString=new String(buffer,"UTF-8");
            Toast.makeText(getApplicationContext(), "JSON başarıyla yüklendi.", Toast.LENGTH_SHORT).show();


            Gson gson=new Gson();
            Type listType = new TypeToken<List<PlacesFilter>>() {}.getType();
            List<PlacesFilter> placeList = gson.fromJson(jsonString, listType);

            List<PlacesFilter> filtered = new ArrayList<>();

            for (PlacesFilter p : placeList) {
                if (p.getCity().equalsIgnoreCase(city) &&
                        p.getTown().equalsIgnoreCase(town) &&
                        p.getCategory().equalsIgnoreCase(category) &&
                        (p.getKeywords().contains(keyword1) ||
                                p.getKeywords().contains(keyword2) ||
                                p.getKeywords().contains(keyword3)))
                    filtered.add(p);
                }



            List<PlacesFilter> top5 = filtered.subList(0, Math.min(filtered.size(), 5));

            for (PlacesFilter p : top5) {
                LatLng location = new LatLng(p.getLatitude(), p.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(p.getName())
                        .snippet(p.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }
            if (!top5.isEmpty()) {
                LatLng firstLocation = new LatLng(top5.get(0).getLatitude(), top5.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {


        mMap.addMarker(new MarkerOptions( ).position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Open directions?");
        alert.setMessage("Are you sure!");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Toast.makeText(MapsActivity.this,"Saved",Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("google.navigation:q="
                        + latLng.latitude + "," + latLng.longitude + "&mode=d" );

                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MapsActivity.this,"No",Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions( ).position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            }
        });
        alert.show();



    }

    }







