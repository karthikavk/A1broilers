package in.kassapos.a1broilers.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;


import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;

import com.rey.material.widget.RadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import in.kassapos.chickenshop.api.Advertisment;
//import in.kassapos.chickenshop.api.Category;
import in.kassapos.a1broilers.api.Advertisment;
import in.kassapos.a1broilers.api.Category;

import in.kassapos.a1broilers.MainActivity;
import in.kassapos.a1broilers.R;
import in.kassapos.a1broilers.SplashScreenActivity;
import in.kassapos.a1broilers.adapter.CategoryAdapter;
import in.kassapos.a1broilers.api.Deliveryshedule;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.api.Servicearea;
import in.kassapos.a1broilers.service.ServiceCall;

public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean delivryAddressstatus=false;
    private Double distance=Double.valueOf(0);
    CategoryAdapter categoryAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
      * @return A new instance of fragment DriverRequest.
     */
    // TODO: Rename and change types and number of parameters
    public  static   CategoryFragment newInstance(String param1) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public static CategoryFragment newInstance() {
        return newInstance(null);
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    Category[] list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            if(mParam1!=null) {
                list = new Gson().fromJson(mParam1, Category[].class);
            }else{
                list=new Category[]{};
            }
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.category, container, false);

        ResponseInfo res = ServiceCall.getActiveServiceArea(SplashScreenActivity.companyid);
        if(res!=null&&!res.getIsError()){
            Servicearea[] serviceareas = new Gson().fromJson(res.getOutput(), Servicearea[].class);
            MainActivity.serviceareas=serviceareas;
            List<Servicearea> tmp1=new ArrayList<>();
            tmp1.add(new Servicearea("Select Delivery Locality"));
            tmp1.addAll(Arrays.asList(serviceareas));
            Spinner area = (Spinner) v.findViewById(R.id.pincode);
            ArrayAdapter<Servicearea> adapter = new ArrayAdapter<Servicearea>(this.getActivity(), R.layout.row_spn, tmp1);
            adapter.setDropDownViewResource(R.layout.row_spn);
            area.setAdapter(adapter);
            area.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.orderGroup.serviceareaid=((Servicearea)adapterView.getSelectedItem()).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
            if(serviceareas.length>0){
                area.setSelection(0);
                MainActivity.orderGroup.serviceareaid=tmp1.get(0).id;
            }
        }
        ResponseInfo res1 = ServiceCall.getActiveDeliverySchedule(SplashScreenActivity.companyid);
        if(res1!=null&&!res1.getIsError()){
            Deliveryshedule[] deliveryshedules = new Gson().fromJson(res1.getOutput(), Deliveryshedule[].class);
            MainActivity.deliveryshedules=deliveryshedules;
            List<Deliveryshedule> tmp=new ArrayList<>();
            tmp.add(new Deliveryshedule("Select Delivery Time"));
            tmp.addAll(Arrays.asList(deliveryshedules));
            Spinner area1 = (Spinner) v.findViewById(R.id.choosetime);
            ArrayAdapter<Deliveryshedule> adapter = new ArrayAdapter<Deliveryshedule>(this.getActivity(), R.layout.row_spn, tmp);
            adapter.setDropDownViewResource(R.layout.row_spn);
            area1.setAdapter(adapter);
            area1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.orderGroup.deliveryscheduleid=((Deliveryshedule)adapterView.getSelectedItem()).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
            if(tmp.size()>0){
                area1.setSelection(0);
                MainActivity.orderGroup.deliveryscheduleid=tmp.get(0).id;
            }
        }
       /* Spinner time = (Spinner) v.findViewById(R.id.choosetime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.row_spn, new String[]{"Today","Tomorrow"});
        adapter.setDropDownViewResource(R.layout.row_spn);
        time.setAdapter(adapter);*/

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
         categoryAdapter = new CategoryAdapter(Arrays.asList(list), getActivity()){

            @Override
            public void onClicked(Category orderMaster) {
                ((MainActivity)CategoryFragment.this.getActivity()).showProducts(orderMaster);
            }
        };
        recyclerView.setAdapter(categoryAdapter);

        final CompoundButton rb1 = (RadioButton) v.findViewById(R.id.switches_rb1);
       final CompoundButton rb2 = (RadioButton) v.findViewById(R.id.switches_rb2);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb1.setChecked(rb1 == buttonView);
                    rb2.setChecked(rb2 == buttonView);

                }
                if(rb1.isChecked()){
                    MainActivity.orderGroup.day=0;
                }else{
                    MainActivity.orderGroup.day=1;
                }
            }

        };

        rb1.setOnCheckedChangeListener(listener);
        rb2.setOnCheckedChangeListener(listener);



       ServiceCall.getAdvertisment(this, SplashScreenActivity.companyid);

        return  v;



    }


    void showsnackBar(String s){
        new SnackBar(this.getActivity(), s, "OK", null).show();
    }

    //advertismentSuccess()
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //  mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public void advertismentSuccess(Advertisment[] p) {
        if(p!=null&&p.length>0){
            categoryAdapter.setAdvertisments(Arrays.asList(p));
            categoryAdapter.notifyDataSetChanged();
        }

    }
}
