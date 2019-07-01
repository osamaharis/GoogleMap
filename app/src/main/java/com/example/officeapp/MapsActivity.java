package com.example.officeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AppComponentFactory;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zaag;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.net.Authenticator;
import java.util.List;
import java.util.Locale;

 public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
         LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
private LocationRequest locationRequest;
private Location lastLocation;
private  Marker currentlocationmarker;

private  static final int request_Location_code=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {

            checkuserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng Karachi = new LatLng(25.193468, 66.991730);
//       mMap.addMarker(new MarkerOptions().
//               position(Karachi).title("Karachi"));



      // mMap.addPolyline(new PolylineOptions().add)


        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Karachi, 18F));
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        // setpioClick(mMap);
        //setMapLongClick(mMap);
        //Onsearch(mMap);
    }


    public boolean checkuserLocationPermission()
     {
         if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_Location_code);
             } else {
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_Location_code);
             }


             return false;
         }
         else
         {
             return true;
         }

     }


     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    switch (requestCode)
    {
        case request_Location_code:
            if(grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    if(googleApiClient==null)
                    {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission Denied....",Toast.LENGTH_LONG).show();
            }
    }


    }

     protected  synchronized  void buildGoogleApiClient()
    {
        googleApiClient= new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

     @Override
     public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);



        }



     }

     @Override
     public void onConnectionSuspended(int i) {

     }

     @Override
     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

     }

     @Override
     public void onLocationChanged(Location location) {


        lastLocation= location;
if(currentlocationmarker!=null)
{
    currentlocationmarker.remove();
}

LatLng karachi = new LatLng(location.getAltitude(),location.getLongitude());
MarkerOptions markerOptions=new MarkerOptions();
markerOptions.position(karachi);
markerOptions.title("current Location");
markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
currentlocationmarker = mMap.addMarker(markerOptions);
mMap.moveCamera(CameraUpdateFactory.newLatLng(karachi));
mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

    if(googleApiClient !=null)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);

    }

    }
 }



  //  @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.map_options, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Change the map type based on the user's selection.
//        switch (item.getItemId()) {
//            case R.id.normal_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                return true;
//            case R.id.hybrid_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                return true;
//            case R.id.satellite_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                return true;
//            case R.id.terrain_map:
//                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    ///Google Maps can single out a location using a marker, which you create using the Marker class.
    // The default marker uses the standard Google Maps icon:
//    private void setpioClick(final GoogleMap map) {
//
//        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onPoiClick(PointOfInterest pointOfInterest) {
//
//
//                Marker marker = map.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));
//                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
//                mMap.setMyLocationEnabled(true);
//                marker.showInfoWindow();
//
//
//            }
//        });
//
//    }


//    private void setMapLongClick(final GoogleMap map) {
//
//        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//
//
//
//                String snippet = String.format(Locale.getDefault(),"Lat:%1$.5f,Long: %2$.5f",latLng.latitude,latLng.longitude);
//
//
//                map.addMarker(new MarkerOptions().position(latLng));
//
//            }
//        });
//
//    }

//    public void Onsearch(GoogleMap view)
//    {
//
//        EditText editText= findViewById(R.id.edsearch);
//        String location = editText.getText().toString();
//        List<Address> addressList= null ;
//        if(location!=null|| !location.equals(""))
//        {
//
//            Geocoder  geocoder= new Geocoder(this);
//            try {
//
//
//
//             addressList= geocoder.getFromLocationName(location,1);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address address=addressList.get(0);
//            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
//            mMap.addMarker(new MarkerOptions().position((latLng)).title("Karachi"));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        }
//    }


//    private void EnabaleMylocatiolan() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            new ActivityCompat.OnRequestPermissionsResultCallback(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}) {
//
//
//            }
//
//
//        }
//
//    }
