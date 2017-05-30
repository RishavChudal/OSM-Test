package np.com.rishavchudal.klltest.volleyMechanism;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import np.com.rishavchudal.klltest.LocationUpdater;
import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.mvp.MainModel;

/**
 * Created by hrishav on 5/28/17.
 */
public class OverpassAPIVolleyConnection {
    public Context context;
    static OnOverpassAPIListener onOverpassAPIListener;
    MainModel mainModel;
    public OverpassAPIVolleyConnection(Context context1){
        this.context = context1;
    }

    public void startAPIVolley(){
        try{
            mainModel = MainModel.getGetMainModelInstance();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    StringCredentials.OVERPASS_API_URL + mainModel.getCurrentLat() + "," + mainModel.getCurrentLng() + ");out;",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                            JSONArray elements = response.getJSONArray("elements");
                        String[] hospitalName = new String[elements.length()];
                        String[] hospitalLat = new String[elements.length()];
                        String[] hospitalLng = new String[elements.length()];
                            for(int j = 0; j < elements.length(); j++){
                                JSONObject node = (JSONObject) elements.get(j);
                                hospitalLat[j] = String.valueOf(node.getDouble("lat"));
                                hospitalLng[j] = String.valueOf(node.getDouble("lon"));
                                JSONObject tags = node.getJSONObject("tags");
                                try{
                                    hospitalName[j] = tags.getString("name");
                                }
                                catch (Exception ex){
                                    Log.e("ResponseFetchingError",ex.getMessage());
                                }
                            }
                            mainModel.setHospitalName(hospitalName);
                            mainModel.setHospitalLat(hospitalLat);
                            mainModel.setHositalLng(hospitalLng);
                            onOverpassAPIListener.onAPIResponse(StringCredentials.VOLLEY_SUCCESS_RESPONSE);

                    }
                    catch (JSONException ex){
                        Log.e(getClass().getSimpleName(),ex.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onOverpassAPIListener.onAPIResponse(StringCredentials.VOLLEY_FAILURE_RESPONSE);
                }
            });
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
        catch (Exception ex){
            Log.e(getClass().getSimpleName(),"Error in volley");
        }
    }

    public interface OnOverpassAPIListener{
        void onAPIResponse(String responseType);
    }

    public void setOnOverpassAPIListener(OnOverpassAPIListener onOverpassAPIListener1){
        OverpassAPIVolleyConnection.onOverpassAPIListener = onOverpassAPIListener1;
    }
}
