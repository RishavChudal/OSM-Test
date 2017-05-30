package np.com.rishavchudal.klltest.mvp;

import android.support.v4.app.Fragment;

/**
 * Created by hrishav on 5/27/17.
 */
public interface MainView {
    void showMapFragment(Fragment fragment,String TAG);
    void showHospitalFragment();
    void showVolleyAPIErrorResponse();
}
