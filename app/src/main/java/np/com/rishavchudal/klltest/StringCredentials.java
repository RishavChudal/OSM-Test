package np.com.rishavchudal.klltest;

/**
 * Created by hrishav on 5/28/17.
 */
public class StringCredentials {
    public StringCredentials(){

    }
    //basic dividers
    public static final String WHITE_SPACE = " ";
    public static final String SEMICOLUMNS = ":";

    //Toolbar titles
    public static final String MAP_FRAGMENT_TITLE = "OpenStreeMap View";
    public static final String HOSPITAL_FRAGMENT_TITLE = "Hospitals Nearby By You";

    //overpass query into API
    public static final String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=hospital](around:2000,";

    //volley response Strings
    public static final String VOLLEY_SUCCESS_RESPONSE = "success";
    public static final String VOLLEY_FAILURE_RESPONSE = "failure";

    //location messages
    public static final String LOCATION_ERROR = "error in location";
    public static final String OPEN_LOCATION_MESSAGE = "Open location in Settings";

    //permission dialog message
    public static final String PERMISSION_DIALOG_MESSAGE = "For proper functionality, You need to provide location & storage permissions.".concat( "Do you want to open permission settings?");

    //alertDialog buttons
    public static final String ALERTDIALOG_POSITIVE_BUTTON = "Open";
    public static final String ALERTDIALOG_NEGATIVE_BUTTON = "Cancel";
    public static final String ALERTDIALOG_NEUTRAL_BUTTON = "Done Already";

    //RecyclerView messages
    public static final String NULL_RECYCLERVIEW_MESSAGE = "Couldnt Load Hospitals";

    //progressDialog message
    public static final String PROGRESS_DIALOG_MESSAGE = "Loading";

}
