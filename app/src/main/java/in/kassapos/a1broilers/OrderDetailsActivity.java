package in.kassapos.a1broilers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.chickenshop.api.Offer;
import in.kassapos.chickenshop.api.User;
import in.kassapos.a1broilers.adapter.OrderAdapter;
import in.kassapos.a1broilers.api.Deliveryshedule;
import in.kassapos.a1broilers.api.Order;
import in.kassapos.a1broilers.api.OrderGroup;
import in.kassapos.a1broilers.api.Servicearea;
import in.kassapos.a1broilers.service.ServiceCall;


public class OrderDetailsActivity extends AppCompatActivity  implements PaymentResultListener {

    private static Toolbar toolbar;
    public CompoundButton rb1 ;
    public CompoundButton rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        new BaseActivity(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
      //  toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");
        isconfirmpage=false;
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        OrderAdapter adapter1 = new OrderAdapter(MainActivity.orderGroup, this){

            @Override
            public void onClicked(Order product) {
                if(MainActivity.orderGroup.orders.isEmpty()){
                    finish();
                }
                setOrderCount(new BigDecimal(MainActivity.orderGroup.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            }
        };
        recyclerView.setAdapter(adapter1);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressConform();
            }
        });
        setOrderCount(new BigDecimal(MainActivity.orderGroup.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        ServiceCall.getOffer(this, SplashScreenActivity.companyid, "12345");
    }
    Boolean isconfirmpage=Boolean.TRUE;
    private void addressConform() {
        setContentView(R.layout.deliverydetails);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((TextView) toolbar.findViewById(R.id.actionbar_notifcation_textview)).setText(new BigDecimal(MainActivity.orderGroup.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        setTitle("Delivery Details");
        isconfirmpage=true;
        ResponseInfo res1 = ServiceCall.getActiveDeliverySchedule(SplashScreenActivity.companyid);
        if(res1!=null&&!res1.getIsError()){
            Deliveryshedule[] deliveryshedules = new Gson().fromJson(res1.getOutput(), Deliveryshedule[].class);
            MainActivity.deliveryshedules=deliveryshedules;
            List<Deliveryshedule> tmp=new ArrayList<>();
            tmp.add(new Deliveryshedule("Select Delivery Time"));
            tmp.addAll(Arrays.asList(deliveryshedules));
            Spinner area1 = (Spinner) findViewById(R.id.choosetime);
            ArrayAdapter<Deliveryshedule> adapter1 = new ArrayAdapter<Deliveryshedule>(OrderDetailsActivity.this, R.layout.row_spn, tmp);
            adapter1.setDropDownViewResource(R.layout.row_spn);
            area1.setAdapter(adapter1);
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

        final RadioButton checkBox_cash=(RadioButton) findViewById(R.id.checkbox_cod);
        final RadioButton checkBox_paytm=(RadioButton)findViewById(R.id.checkbox_paytm);

        final CompoundButton rb1 = (com.rey.material.widget.RadioButton) findViewById(R.id.switches_rb1);
        final CompoundButton rb2 = (com.rey.material.widget.RadioButton) findViewById(R.id.switches_rb2);

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
        setaddress();
        findViewById(R.id.order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_cash.isChecked()){
                    MainActivity.orderGroup.paymenttype=0;
                    orderAction();
                } else if(checkBox_paytm.isChecked()){
                    MainActivity.orderGroup.paymenttype=1;
                    orderAction();
                }else{
                    showSnackBar("Please Select Payment Type");
                    return;
                }
                //orderAction();
            }
        });
        findViewById(R.id.txt_changedelivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddress();
            }
        });
    }
    private void showSnackBar(String data){
        new SnackBar(this,data).show();
    }

    private void orderAction() {
        if(MainActivity.orderGroup.deliveryscheduleid==null){
            showSnackBar("Please choose delivery time");
        }else if(MainActivity.orderGroup.serviceareaid==null){
            showSnackBar("Please choose delivery area");
        }else if(MainActivity.orderGroup.getAmount()<=0){
            showSnackBar("Order Amount Can't be Zero");
        }else if(MainActivity.orderGroup.getAmount()<100){
            showSnackBar("Minimum Order Amount Rs.100!!! Please add some more products");
            new AlertDialog.Builder(this).setTitle("Delivery Charge").setMessage("Minimum Order Amount Rs.100!!! Please add some more products").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(MainActivity.orderGroup.paymenttype==0){
                        // TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                       /* OrderGroup om=MainActivity.orderGroup;
                        om.user=SplashScreenActivity.userinfo;
                        //om.imei=telephonyManager.getDeviceId();
                        om.additional_info=((EditText)findViewById(R.id.additional_info)).getText().toString().trim();
                        om.paymenttype=0;
                        ServiceCall.PlaceOrder(OrderDetailsActivity.this,om);*/

                    }else{
                       /* final Checkout co = new Checkout();
                        final Activity activity = OrderDetailsActivity.this;
                        try {
                            OrderGroup om=MainActivity.orderGroup;
                            om.user=SplashScreenActivity.userinfo;
                            JSONObject options = new JSONObject();
                            options.put("name", "Bismillah Proteins");
                            options.put("description", "Online Payment");
                            //You can omit the image option to fetch the image from dashboard
                            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                            options.put("currency", "INR");
                            options.put("amount",new BigDecimal((MainActivity.orderGroup.getAmount()*100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                            JSONObject preFill = new JSONObject();
                            preFill.put("email", om.user.userid);
                            preFill.put("contact",  om.user.contactno);

                            options.put("prefill", preFill);
                            options.put("theme", "#1fa4ff");

                            co.open(activity, options);
                        } catch (Exception e) {
                            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }*/
                    }
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            }).create().show();
        }else if(MainActivity.orderGroup.paymenttype==0){
           // TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            OrderGroup om=MainActivity.orderGroup;
            om.user=SplashScreenActivity.userinfo;
            //om.imei=telephonyManager.getDeviceId();
            om.additional_info=((EditText)findViewById(R.id.additional_info)).getText().toString().trim();
            om.paymenttype=0;
           ServiceCall.PlaceOrder(this,om);

        }else if(MainActivity.orderGroup.paymenttype==1){
            final Checkout co = new Checkout();
            final Activity activity = this;
            try {
                OrderGroup om=MainActivity.orderGroup;
                om.user=SplashScreenActivity.userinfo;
                JSONObject options = new JSONObject();
                options.put("name", "A1 Broilers ");
                options.put("description", "Online Payment");
                //You can omit the image option to fetch the image from dashboard
                //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("currency", "INR");
                options.put("amount",new BigDecimal(MainActivity.orderGroup.getAmount()*100).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                JSONObject preFill = new JSONObject();
                preFill.put("email", om.user.userid);
                preFill.put("contact",  om.user.contactno);

                options.put("prefill", preFill);
                options.put("theme", "#1fa4ff");

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    public void placeOrderSuccess(String id){
            new AlertDialog.Builder(this).setTitle("Order Stored Successfully").setMessage("Order Reference Number : "+id).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.orderGroup.orders = new ArrayList<Order>();
                    MainActivity.orderGroup.offer_id=null;
                    MainActivity.orderGroup.offer_amount=null;
                    finish();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MainActivity.orderGroup.orders = new ArrayList<Order>();
                    MainActivity.orderGroup.offer_id=null;
                    MainActivity.orderGroup.offer_amount=null;
                    finish();
                }
            }).create().show();
    }
    private void changeAddress(){
        setContentView(R.layout.changeaddress);
       final EditText contact = (EditText) findViewById(R.id.contactno);
         final EditText address1=(EditText)findViewById(R.id.address1);
        final  EditText address2=(EditText)findViewById(R.id.address2);
        final  EditText landmark=(EditText)findViewById(R.id.landmark);
        final EditText city=(EditText)findViewById(R.id.city);
        User user=SplashScreenActivity.userinfo;
        contact.setText(user.contactno);
        address1.setText(user.address1);
        address2.setText(user.address2);
        landmark.setText(user.landmark);
        city.setText(user.city);


        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressConform();
            }
        });
        findViewById(R.id.email_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=SplashScreenActivity.userinfo;
                User u=new User();
                u.contactno=contact.getText().toString().trim();
                u.address1=address1.getText().toString().trim();
                u.address2=address2.getText().toString().trim();
                u.landmark=landmark.getText().toString().trim();
                u.city=city.getText().toString().trim();
                EditText focusView=null;
                Boolean cancelLogin=false;
                if (TextUtils.isEmpty(u.contactno)) {
                    contact.setError(getString(R.string.entercontactno));
                    focusView = contact;
                    cancelLogin = true;
                }


                if (TextUtils.isEmpty(u.address1)) {
                    address1.setError(getString(R.string.enteraddress1));
                    focusView = address1;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(u.address2)) {
                    address2.setError(getString(R.string.enteraddress2));
                    focusView = address2;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(u.city)) {
                    city.setError(getString(R.string.entercity));
                    focusView = city;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(u.landmark)) {
                    landmark.setError(getString(R.string.enterlandmark));
                    focusView = landmark;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    user.contactno=u.contactno;
                    user.address1=u.address1;
                    user.address2=u.address2;
                    user.city=u.city;
                    user.landmark=u.landmark;
                    addressConform();
                }
            }
        });
    }


    public  void setOrderCount(String count){
        TextView txt=(TextView) findViewById(R.id.total);
        TextView txtoffer=(TextView) findViewById(R.id.total_offer);
        TextView txtnet=(TextView) findViewById(R.id.total_net);
        TextView txt1=(TextView) toolbar.findViewById(R.id.actionbar_notifcation_textview);
        txtnet.setText("Net Amount : " + new BigDecimal(MainActivity.orderGroup.getAmountOffer()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        txt1.setText("Total : " + count.toString());
        if(MainActivity.orderGroup.offer_amount!=null&&MainActivity.orderGroup.offer_amount>0){
            txt.setVisibility(View.VISIBLE);
            txtoffer.setVisibility(View.VISIBLE);
            txt.setText("Total :" + new BigDecimal(MainActivity.orderGroup.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            txtoffer.setText("Offer :" + new BigDecimal(MainActivity.orderGroup.offer_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        }else{
            txt.setVisibility(View.GONE);
            txtoffer.setVisibility(View.GONE);
        }
    }
    private void setaddress() {
        User user=SplashScreenActivity.userinfo;
                ((TextView) findViewById(R.id.txt_name)).setText(user.name+"\n"+user.contactno+"\n"+user.address1+","+user.address2+"\n"+user.city+user.landmark);
                ((TextView) findViewById(R.id.txt_mobile)).setText(user.contactno);
                ((TextView) findViewById(R.id.txt_address)).setText(user.address1);
                ((TextView) findViewById(R.id.txt_address2)).setText(user.address2);
                ((TextView) findViewById(R.id.txt_city)).setText(user.city);
                ((TextView) findViewById(R.id.txt_landmark)).setText(user.landmark);
        if(MainActivity.orderGroup.day==0) {
            ((TextView) findViewById(R.id.txt_date)).setText("Today");
        }else{
            ((TextView) findViewById(R.id.txt_date)).setText("Tomorrow");
        }
        List<Servicearea> tmp1=new ArrayList<>();
        tmp1.add(new Servicearea("Select Delivery Locality"));
        tmp1.addAll(Arrays.asList(MainActivity.serviceareas));
        Spinner area = (Spinner) findViewById(R.id.pincode);
        ArrayAdapter<Servicearea> adapter = new ArrayAdapter<Servicearea>(this, R.layout.row_spn,tmp1 );
        adapter.setDropDownViewResource(R.layout.row_spn);
        area.setAdapter(adapter);
        area.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.orderGroup.serviceareaid = ((Servicearea) adapterView.getSelectedItem()).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        if(MainActivity.orderGroup.serviceareaid!=null){
            for(int i=0;i<tmp1.size();i++){
                if(tmp1.get(i).id!=null&&tmp1.get(i).id.equalsIgnoreCase(MainActivity.orderGroup.serviceareaid)){
                    area.setSelection(i);
                }
            }
        }
     //   Deliveryshedule[] deliveryshedules = MainActivity.deliveryshedules;
      //  List<Deliveryshedule> tmp=new ArrayList<>();
      //  tmp.add(new Deliveryshedule("Select Delivery Time"));
       // tmp.addAll(Arrays.asList(deliveryshedules));
      //  Spinner area1 = (Spinner) findViewById(R.id.choosetime);



        // area1.setEnabled(false);
        //area.setEnabled(false);
        // ((TextView) findViewById(R.id.txt_pincode)).setText(user.serviceareaid);
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

    public void offerSuccess(Offer[] p) {
        Offer currentoffer=null;
        for(Offer o:p){
            if(o.offereligibleamount<=MainActivity.orderGroup.getAmount()){
                if(currentoffer==null){
                    currentoffer=o;
                }else if(o.offeramount>currentoffer.offeramount){
                    currentoffer=o;
                }
            }
        }
        if(currentoffer!=null){
            MainActivity.orderGroup.offer_id=currentoffer.id;
            MainActivity.orderGroup.offer_amount=currentoffer.offeramount;
        }
        setOrderCount(new BigDecimal(MainActivity.orderGroup.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

    }

    @Override
    public void onBackPressed() {
        if(isconfirmpage){
            Intent intent=new Intent(this,OrderDetailsActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
        OrderGroup om = MainActivity.orderGroup;
        om.user=SplashScreenActivity.userinfo;
       // om.payment_status=1;
        //om.txtid=s;
        om.additional_info=((EditText)findViewById(R.id.additional_info)).getText().toString().trim();
        ServiceCall.PlaceOrder(this,om);
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Payment Error", "Exception in onPaymentError", e);
        }
    }
}
