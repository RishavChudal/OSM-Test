package np.com.rishavchudal.klltest.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.bonuspack.overlays.Marker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.rishavchudal.klltest.LocationUpdater;
import np.com.rishavchudal.klltest.R;
import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.mvp.MainModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapDisplayFragment extends Fragment implements LocationUpdater.OnUserLocationChangeListener {

    @Bind(R.id.map_fragment_map)
    MapView mapView;
    @Bind(R.id.map_fragment_near_hospitals_btn)
    Button hospitals;
    @Bind(R.id.map_fragment_user_location_btn)
    Button location;
    IMapController mapController;
    public static final int MY_PERMISSIONS_REQUEST = 99;
    static double myLat, myLng;
    LocationUpdater lu;
    OnHospitalButtonListener onHospitalButtonListener;
    OnPermissionDialogListener onPermissionDialogListener;
    MainModel mainModel;
    static boolean isPermissionGiven = false;
    public MapDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lu = new LocationUpdater(getActivity(),this);
        mainModel = MainModel.getGetMainModelInstance();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onHospitalButtonListener = (OnHospitalButtonListener) context;
        }
        catch (ClassCastException ex){
            throw new ClassCastException(ex.getMessage() + "must implement OnHospitalButtonListener");
        }

        try {
            onPermissionDialogListener = (OnPermissionDialogListener) context;
        }
        catch (ClassCastException ex){
            throw new ClassCastException(ex.getMessage() + "must implement OnPermissionDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapFragment =  inflater.inflate(R.layout.fragment_map_display, container, false);
        ButterKnife.bind(this,mapFragment);
        return mapFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeOSM();
    }

    @OnClick(R.id.map_fragment_user_location_btn)
    public void userLocation(){
        lu.isLocationEnable();
        if(lu.isLocationEnable) {
            getSetLatLng();
            Toast.makeText(getActivity(), "Your location is" + " " +String.valueOf(myLat) +" , "+String.valueOf(myLng), Toast.LENGTH_SHORT).show();
            pointMyLocationInMap(myLat,myLng);
        }
        else {
            Toast.makeText(getActivity(), StringCredentials.OPEN_LOCATION_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.map_fragment_near_hospitals_btn)
    public void setHospitals(){
        if(lu.isLocationEnable()){
            getSetLatLng();
            onHospitalButtonListener.interactActivityForHospitals();
        }
        else {
            Toast.makeText(getActivity(), StringCredentials.OPEN_LOCATION_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    public void getSetLatLng(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLat = lu.getLatitude();
        myLng = lu.getLongitude();
        mainModel.setCurrentLat(String.valueOf(myLat));
        mainModel.setCurrentLng(String.valueOf(myLng));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void pointMyLocationInMap(double myLat, double myLng) {
        mapController = mapView.getController();
        Marker marker = new Marker(mapView);
        GeoPoint myLocation = new GeoPoint(myLat,myLng);
        mapController.setCenter(myLocation);
        mapController.animateTo(myLocation);
        mapController.setZoom(22);
        marker.setPosition(myLocation);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        mapView.getOverlays().clear();
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void initializeOSM() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!isPermissionGiven){
                openAlertDialog(getView());
            }
        }
        else {
            setMapInFragment();
        }
    }

    private void openAlertDialog(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(StringCredentials.PERMISSION_DIALOG_MESSAGE);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(StringCredentials.ALERTDIALOG_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isPermissionGiven = true;
                openSettings();
                dialogInterface.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton(StringCredentials.ALERTDIALOG_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialogBuilder.setNeutralButton(StringCredentials.ALERTDIALOG_NEUTRAL_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openSettings(){
        onPermissionDialogListener.interactActivityForPermissions();
    }

    private void setMapInFragment(){
        mapController = mapView.getController();
        mapView.getTileProvider().setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMapOrientation(360);
        mapView.setMultiTouchControls(true);
        mapController.setZoom(9);
        GeoPoint startpoint = new GeoPoint(34.0479,100.6197);
        mapController.setCenter(startpoint);
    }


    @Override
    public void moveMarkerToCurrentLocation(double myLat, double myLng) {
        pointMyLocationInMap(myLat,myLng);
    }

    // this part is skipped as multiple permission dialog has to be shown.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        setMapInFragment();
                    }
                }
            }
        }
    }



    public interface OnHospitalButtonListener{
        void interactActivityForHospitals();
    }

    public interface OnPermissionDialogListener{
        void interactActivityForPermissions();
    }


}
