package np.com.rishavchudal.klltest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import np.com.rishavchudal.klltest.R;
import np.com.rishavchudal.klltest.StringCredentials;
import np.com.rishavchudal.klltest.adapter.RecycleViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalFragment extends Fragment {


    @Bind(R.id.hospital_fragment_recycler)
    RecyclerView recyclerView;
    RecycleViewAdapter recylerAdapter;
    public HospitalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recylerAdapter = new RecycleViewAdapter(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View hospitalView = inflater.inflate(R.layout.fragment_hospital, container, false);
        ButterKnife.bind(this,hospitalView);
        return hospitalView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showRecyclerView();
    }

    private void showRecyclerView() {
        if(recyclerView == null){
            Toast.makeText(getActivity(), StringCredentials.NULL_RECYCLERVIEW_MESSAGE, Toast.LENGTH_SHORT).show();
        }
        else{
            recyclerView.setLayoutManager(initRecyclerView());
            recyclerView.setAdapter(recylerAdapter);
        }
    }

    public RecyclerView.LayoutManager initRecyclerView(){
        return new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    }

}
