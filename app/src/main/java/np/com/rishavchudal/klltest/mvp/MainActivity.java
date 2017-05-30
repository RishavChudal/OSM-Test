package np.com.rishavchudal.klltest.mvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import np.com.rishavchudal.klltest.R;
import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.fragments.HospitalFragment;
import np.com.rishavchudal.klltest.fragments.MapDisplayFragment;
import np.com.rishavchudal.klltest.volleyMechanism.OverpassAPIVolleyConnection;

public class MainActivity extends AppCompatActivity implements MainView,MapDisplayFragment.OnHospitalButtonListener,MapDisplayFragment.OnPermissionDialogListener{

    @Bind(R.id.main_activity_toolbar)
    Toolbar toolbar;
    FragmentManager fragManager;
    Fragment fragment;
    String CURRENT_FRAGMENT_TAG;
    MainPresenter mainPresenter;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("OpenStreeMap View");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragManager = getSupportFragmentManager();
        mainPresenter = new MainPresenter(this,this);
        ButterKnife.bind(this);
        initializeToolbar();
        checkFragmentAvailability(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String fragmentTag = fragManager.findFragmentById(R.id.main_activity_frame).getTag();
        outState.putString(CURRENT_FRAGMENT_TAG,fragmentTag);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    private void checkFragmentAvailability(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            String TAG = savedInstanceState.getString(CURRENT_FRAGMENT_TAG);
            fragment = fragManager.findFragmentByTag(TAG);
        }
        else {
            fragment = new MapDisplayFragment();
            mainPresenter.initializeMapFragment(fragment,"MAP FRAGMENT");
        }
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(StringCredentials.MAP_FRAGMENT_TITLE);
        }
    }

    @Override
    public void showMapFragment(Fragment fragment,String TAG) {
        fragManager.beginTransaction().replace(R.id.main_activity_frame,fragment,TAG).commit();
    }

    @Override
    public void showHospitalFragment() {
        progressDialog.dismiss();
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(StringCredentials.HOSPITAL_FRAGMENT_TITLE);
        }
        fragment = new HospitalFragment();
        fragManager.beginTransaction().replace(R.id.main_activity_frame,fragment,"Hospital Fragment").addToBackStack(null).commit();
    }

    @Override
    public void showVolleyAPIErrorResponse() {
        progressDialog.dismiss();
        Toast.makeText(MainActivity.this, "Couldnot Fetch data from the Overpass API", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void interactActivityForHospitals() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(StringCredentials.PROGRESS_DIALOG_MESSAGE);
        progressDialog.show();
        mainPresenter.startVolleyConnectionUsingAPI();
    }

    @Override
    public void interactActivityForPermissions() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
