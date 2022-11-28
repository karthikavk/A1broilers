package in.kassapos.a1broilers;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.GsonBuilder;


import java.util.ArrayList;

import in.kassapos.chickenshop.api.ORDERSTATUS;
import in.kassapos.chickenshop.api.OrderMasterBean;
import in.kassapos.a1broilers.adapter.OrderHistoryAdapter;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.service.ServiceCall;


public class OrderHistory extends AppCompatActivity {
    private Boolean iscancelled=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Your Orders");
        iscancelled=getIntent().getBooleanExtra("canceled",false);
        ResponseInfo res = ServiceCall.getOrders(SplashScreenActivity.companyid, SplashScreenActivity.userinfo.id);
        if (res==null){
            finish();return;
        }
        OrderMasterBean[] list = new GsonBuilder().setDateFormat("dd/MM/yyyy").create().fromJson(res.getOutput(), OrderMasterBean[].class);
       if(iscancelled){
           setTitle("Cancelled Orders");
           ArrayList<OrderMasterBean> tmp=new ArrayList<>();
           for(OrderMasterBean om:list){
               if(om.orderstatus== ORDERSTATUS.REJECTED.getKey()){
                   tmp.add(om);
               }
           }
           list=tmp.toArray(new OrderMasterBean[tmp.size()]);
       }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        OrderHistoryAdapter adapter1 = new OrderHistoryAdapter(list, this){

            @Override
            public void cancelOrder(OrderMasterBean orderMasterBean) {
                ServiceCall.cancelOrder(OrderHistory.this,orderMasterBean);
            }
        };
        recyclerView.setAdapter(adapter1);
    }
    public void rejectedOrder(){
        finish();
        Intent in=new Intent(this,OrderHistory.class);
        startActivity(in);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
