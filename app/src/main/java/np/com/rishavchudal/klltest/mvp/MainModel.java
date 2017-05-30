package np.com.rishavchudal.klltest.mvp;

/**
 * Created by hrishav on 5/27/17.
 */
public class MainModel {
    private MainModel mainModel;
    private static MainModel mainModelInstance;
    public static MainModel getGetMainModelInstance(){
        if(mainModelInstance == null){
            mainModelInstance = new MainModel();
        }
        return mainModelInstance;
    }

    private String currentLat,currentLng;
    private String[] hospitalName,hospitalLat,hositalLng;

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }

    public String[] getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String[] hospitalName) {
        this.hospitalName = hospitalName;
    }


    public String[] getHospitalLat() {
        return hospitalLat;
    }

    public void setHospitalLat(String[] hospitalLat) {
        this.hospitalLat = hospitalLat;
    }

    public String[] getHositalLng() {
        return hositalLng;
    }

    public void setHositalLng(String[] hositalLng) {
        this.hositalLng = hositalLng;
    }

}
