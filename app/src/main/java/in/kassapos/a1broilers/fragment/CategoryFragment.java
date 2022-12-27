package in.kassapos.a1broilers.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;


import com.bumptech.glide.Glide;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;

import com.rey.material.widget.RadioButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

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
    CarouselView carouselView;
    List<String> sampleImages;
    private AppCompatImageView adimageview;

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
            carouselView=v.findViewById(R.id.slider);
            adimageview=v.findViewById(R.id.imageView3);
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


        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
         categoryAdapter = new CategoryAdapter(Arrays.asList(list), getActivity()){

            @Override
            public void onClicked(Category orderMaster) {
                ((MainActivity)CategoryFragment.this.getActivity()).showProducts(orderMaster);
            }
        };
        recyclerView.setAdapter(categoryAdapter);
/*
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
        rb2.setOnCheckedChangeListener(listener);*/



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

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            String[] imgary= sampleImages.toArray(new String[0]);
            //imageView.setImageResource(imgary[position]);
            Glide.with(getActivity()).load(imgary[position]).into(imageView);
        }
    };

    public void advertismentSuccess(Advertisment[] p) {
        if(p!=null&&p.length>0){
            sampleImages=new ArrayList<>();

            Glide.with(getActivity()).load(ServiceCall._ImagePath + p[0].imagepath).into(adimageview);
           // showMessageOnceDay(p[1].imagepath);
          //  refreshOrder();
            for (int i = 1; i <= p.length-1; i++) {

                // DefaultSliderView sliderView = new DefaultSliderView (this);
                // sliderView.setImageUrl(ServiceCall._ImagePath+p[i].imagepath);

                sampleImages.add(ServiceCall._ImagePath+p[i].imagepath);

                        /*switch (i) {
                            case 0:
                                sliderView.setImageUrl(ServiceCall._ImagePath+p[0].imagepath);
                                break;
                            case 1:
                                sliderView.setImageUrl(ServiceCall._ImagePath+p[1].imagepath);
                                break;
                            case 2:
                                sliderView.setImageUrl(ServiceCall._ImagePath+p[2].imagepath);
                                break;
                            case 3:
                                sliderView.setImageUrl(ServiceCall._ImagePath+p[3].imagepath);
                                break;
                        }*/

                // sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                //sliderView.setDescription("setDescription " + (i + 1));
                final int finalI = i;


                //at last add this view in your layout :
                //sliderLayout.addSliderView(sliderView);
               // mShimmerViewContainer.stopShimmerAnimation();
                //mShimmerViewContainer.setVisibility(View.GONE);
            }
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(sampleImages.size());
          //  categoryAdapter.setAdvertisments(Arrays.asList(p));
          //  categoryAdapter.notifyDataSetChanged();
        }

    }
}
