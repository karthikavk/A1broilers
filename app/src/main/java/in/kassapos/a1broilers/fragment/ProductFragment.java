package in.kassapos.a1broilers.fragment;

import static in.kassapos.a1broilers.service.ServiceCall.gsondateonly;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;

import java.util.Arrays;

import in.kassapos.a1broilers.R;
import in.kassapos.a1broilers.adapter.ProductAdapter;
import in.kassapos.a1broilers.api.Product;
import in.kassapos.a1broilers.myinterface.ProductSelected;

public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean delivryAddressstatus=false;
    private Double distance=Double.valueOf(0);
    private ProductSelected productselected;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
      * @return A new instance of fragment DriverRequest.
     */
    // TODO: Rename and change types and number of parameters
    public  static ProductFragment newInstance(String param1) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public static ProductFragment newInstance() {
        return newInstance(null);
    }

    public ProductFragment() {
        // Required empty public constructor
    }
    public static Product[] list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            if(mParam1!=null) {
                //list = new Gson().fromJson(mParam1, Product[].class);
                list =  gsondateonly.fromJson(mParam1, Product[].class);
            }else{
                list=new Product[]{};
            }
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            productselected = (ProductSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement productselected");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.product, container, false);



        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ProductAdapter adapter1 = new ProductAdapter(Arrays.asList(list), getActivity()){

            @Override
            public void onClicked(Product product,int size) {
                product.ischanged=Boolean.FALSE;
                productselected.addProduct(product,size);
            }
        };
        recyclerView.setAdapter(adapter1);


        return  v;



    }


    void showsnackBar(String s){
        new SnackBar(this.getActivity(), s, "OK", null).show();
    }


}
