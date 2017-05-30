package np.com.rishavchudal.klltest;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hrishav on 5/28/17.
 */
public class LocationUpdater extends Service implements LocationListener {

    private final Context context;
    boolean isGPSOn = false;
    boolean isNetworkOn = false;
    public boolean isLocationEnable = false;
    private static final long MIN_DISTANCE_TO_REQUEST_LOCATION = 1;
    private static final long MIN_TIME_FOR_UPDATES = 1000 * 10;
    Location location;
    double longitude, latitude;
    LocationManager locationManager;
    OnUserLocationChangeListener onUserLocationChangeListnener;

    public LocationUpdater(Context context1,OnUserLocationChangeListener userLocationChangeListener) {
        this.context = context1;
        this.onUserLocationChangeListnener = userLocationChangeListener;
        checkLocationCoordinates();
    }

    public Location checkLocationCoordinates() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSOn && !isNetworkOn) {
                isLocationEnable = false;
            }
            else {
                isLocationEnable = true;
                if (isNetworkOn) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSOn) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }


                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_TO_REQUEST_LOCATION, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
        }
        }
        catch (Exception e){
            Log.e(getClass().getSimpleName(),StringCredentials.LOCATION_ERROR);
        }
        return location;
    }

    // call this to getLatitude
    public double getLatitude()
    {
        checkLocationCoordinates();
        if(location!=null)
        {
            latitude=location.getLatitude();
        }
        return latitude;
    }
    //call this to getLongitude
    public double getLongitude()
    {
        checkLocationCoordinates();
        if(location!=null)
        {
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean isLocationEnable() {
        checkLocationCoordinates();
        return this.isLocationEnable;
    }

    @Override
    public void onLocationChanged(Location location) {
       onUserLocationChangeListnener.moveMarkerToCurrentLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface OnUserLocationChangeListener{
        void moveMarkerToCurrentLocation(double myLat,double myLng);
    }
}
