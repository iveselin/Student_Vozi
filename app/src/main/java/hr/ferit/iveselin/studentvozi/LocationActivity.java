package hr.ferit.iveselin.studentvozi;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;
import hr.ferit.iveselin.studentvozi.utils.LoginUtils;


public class LocationActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "LocationActivity";
    public static final String KEY_TYPED_ADDRESS = "typed_address";

    public static Intent getLaunchIntent(Context fromContext, String typedAddress) {
        Intent launchIntent = new Intent(fromContext, LocationActivity.class);
        launchIntent.putExtra(KEY_TYPED_ADDRESS, typedAddress);
        return launchIntent;
    }

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1111;
    public static final float DEFAULT_ZOOM = 15f;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean permissionsGranted = false;
    private String address = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setUi();
        getExtras();
    }

    private void setUi() {
        ButterKnife.bind(this);
    }

    private void getExtras() {
        address = getIntent().getStringExtra(KEY_TYPED_ADDRESS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!LoginUtils.checkLogin()) {
            Toast.makeText(getApplicationContext(), "You have to login", Toast.LENGTH_SHORT).show();
            startActivity(LoginActivity.getLaunchIntent(this));
            finish();
        }

        checkLocationPermissions();
    }


    private void checkLocationPermissions() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            permissionsGranted = true;
            initMap();
            return;
        }

        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionsGranted = false;
                            return;
                        }
                    }
                    permissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String getClickedAddress(LatLng latLng) {
        Log.d(TAG, "getClickedAddress: getting address from location");
        List<Address> addresses = new ArrayList<>();

        Geocoder geocoder = new Geocoder(this);

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Log.d(TAG, "getClickedAddress: geolocation failed: " + e.getMessage());
        }

        if (addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0);
            Log.d(TAG, "getClickedAddress: found address" + address);
            return address;
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getClickedAddress: no data, returning null");
            return "";
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (permissionsGranted) {
            if (address == null || address.isEmpty()) {
                getDeviceLocation();
            } else {
                getAddressLocation();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Log.d(TAG, "onMapClick: user clicked on: " + latLng.latitude + ", " + latLng.longitude);
                    String clickedAddress = getClickedAddress(latLng);
                    Log.d(TAG, "onMapClick: creating dialog on address " + clickedAddress);
                    createDialog(clickedAddress);
                }
            });
        }
    }

    private void createDialog(final String clickedAddress) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(clickedAddress);

        alertBuilder.setPositiveButton(R.string.dialog_positive_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(RESULT_OK, AddRequestActivity.getResultsIntent(clickedAddress));
                finish();
            }
        });

        alertBuilder.setNegativeButton(R.string.dialog_negative_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void moveCamera(LatLng latLng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getAddressLocation() {
        Geocoder geocoder = new Geocoder(this);

        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.d(TAG, "getAddressLocation: failed error:" + e.getMessage());
        }

        if (list.size() > 0) {
            LatLng locationLatLNg = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
            moveCamera(locationLatLNg, DEFAULT_ZOOM);
        } else {
            Toast.makeText(this, "Ne mogu naci lokaciju adrese, prikazujemo tvoju", Toast.LENGTH_SHORT).show();
            getDeviceLocation();
        }
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (permissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                        } else {
                            Log.d(TAG, "onComplete: unsuccessful location");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: failed call to locate device: error " + e.getMessage());
        }
    }
}
