package np.com.rishavchudal.klltest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import np.com.rishavchudal.klltest.R;
import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.mvp.MainModel;

/**
 * Created by hrishav on 5/29/17.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CustomView> {

    private Context context;
    MainModel mainModel = MainModel.getGetMainModelInstance();
    public RecycleViewAdapter(Context context){
        this.context = context;

    }

    @Override
    public RecycleViewAdapter.CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item,parent,false);
        return new CustomView(layoutView);
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.CustomView holder, int position) {

        holder.name.setText(mainModel.getHospitalName()[holder.getAdapterPosition()]);

        holder.lat.setText(context.getString(R.string.latitude).concat(StringCredentials.WHITE_SPACE).concat(StringCredentials.SEMICOLUMNS).concat(StringCredentials.WHITE_SPACE).concat(mainModel.getHospitalLat()[holder.getAdapterPosition()]));

        holder.lng.setText(context.getString(R.string.longitude).concat(StringCredentials.WHITE_SPACE).concat(StringCredentials.SEMICOLUMNS).concat(StringCredentials.WHITE_SPACE).concat(mainModel.getHositalLng()[holder.getAdapterPosition()]));
    }

    @Override
    public int getItemCount() {
        return mainModel.getHospitalName().length;
    }

    class CustomView extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView lat;
        public TextView lng;

        public CustomView(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.hospital_name);
            lat = (TextView) itemView.findViewById(R.id.hospital_Latitude);
            lng = (TextView) itemView.findViewById(R.id.hospital_Longitude);
        }
    }
}
