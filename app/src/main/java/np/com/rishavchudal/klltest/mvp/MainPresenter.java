package np.com.rishavchudal.klltest.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;

import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.volleyMechanism.OverpassAPIVolleyConnection;

/**
 * Created by hrishav on 5/27/17.
 */
public class MainPresenter {
    private Context context;
    public MainView mainView;
    OverpassAPIVolleyConnection overpassVolleyConnection;

    public MainPresenter(Context context1,MainView mainView1){
        this.context = context1;
        this.mainView = mainView1;
    }

    public void initializeMapFragment(Fragment fragment,String TAG) {
        mainView.showMapFragment(fragment,TAG);
    }

    public void startVolleyConnectionUsingAPI() {
        overpassVolleyConnection = new OverpassAPIVolleyConnection(context);
        overpassVolleyConnection.startAPIVolley();
        overpassVolleyConnection.setOnOverpassAPIListener(new OverpassAPIVolleyConnection.OnOverpassAPIListener() {
            @Override
            public void onAPIResponse(String responseType) {
                if(responseType.contains(StringCredentials.VOLLEY_SUCCESS_RESPONSE)){
                    mainView.showHospitalFragment();
                }
                else if(responseType.contains(StringCredentials.VOLLEY_FAILURE_RESPONSE)){
                    mainView.showVolleyAPIErrorResponse();
                }
            }
        });
    }
}
