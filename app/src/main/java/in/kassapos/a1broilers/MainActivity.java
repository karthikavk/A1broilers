package in.kassapos.a1broilers;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;

import com.rey.material.app.ThemeManager;

import java.math.BigDecimal;
import java.util.ArrayList;

//import in.kassapos.chickenshop.api.Category;
import in.kassapos.a1broilers.api.Category;
import in.kassapos.a1broilers.adapter.MyAdapter;
import in.kassapos.a1broilers.api.Deliveryshedule;
import in.kassapos.a1broilers.api.Order;
import in.kassapos.a1broilers.api.OrderGroup;
import in.kassapos.a1broilers.api.Product;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.api.Servicearea;
import in.kassapos.a1broilers.fragment.CategoryFragment;
import in.kassapos.a1broilers.fragment.ProductFragment;
import in.kassapos.a1broilers.myinterface.ProductSelected;
import in.kassapos.a1broilers.service.ServiceCall;


public class MainActivity extends AppCompatActivity implements ProductSelected {
    static Toolbar toolbar;
    public static OrderGroup orderGroup;
    public Button amountbtn;
    public ButtonRectangle orderbtn;
    public CategoryFragment catfrag;
    private final Integer ORDERDETAILACTIVITY=10;
    public static Servicearea[] serviceareas;
    public static Deliveryshedule[] deliveryshedules;
    String TITLES[] =null;
    int ICONS[] = null;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!BuildConfig.DEBUG) {
            new BaseActivity(this);
        }
        if(SplashScreenActivity.userinfo==null){
            Intent i = new Intent(this, SplashScreenActivity.class);
            startActivity(i);
            finish();
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.annav));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ThemeManager.init(this, 1, 0, null);
        if(orderGroup ==null) {
            orderGroup = new OrderGroup();
            orderGroup.companyid=SplashScreenActivity.companyid;
        }
        TextView txt=(TextView) toolbar.findViewById(R.id.actionbar_notifcation_textview);
        amountbtn=(Button)findViewById(R.id.amount_button);
        orderbtn=(ButtonRectangle)findViewById(R.id.order_button);
        /*com.rey.material.widget.Button orderbtn1 = (com.rey.material.widget.Button) findViewById(R.id.back_button1);
        orderbtn1.setText("test");*/
        if( orderbtn.getTextView()!=null) {
            orderbtn.getTextView().setText("Palani");
        }
        orderbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OrderAction();
            }
        });
        toolbar.findViewById(R.id.cartimg).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                OrderAction();
            }
        });
        showCategory();
        navdrawer();
    }
    Boolean isopen=false;
    private void navdrawer() {
        TITLES =new String[] {getString(R.string.profile),getString(R.string.orderhistory),getString(R.string.canceledorders),getString(R.string.feedback),getString(R.string.terms),getString(R.string.logout)};
        ICONS=new int[]{R.drawable.profile,R.drawable.view,R.drawable.delete,R.drawable.feedback,R.drawable.terms,R.drawable.logout};
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,SplashScreenActivity.userinfo.name,SplashScreenActivity.userinfo.userid,R.drawable.an,this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){



            @Override
            public void onDrawerOpened(View drawerView) {
                isopen=true;
               super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isopen=false;
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }

    public void OrderAction(){
        if(orderGroup.deliveryscheduleid==null){
            showSnackBar("Please select Delivery Time");
        }else if(orderGroup.serviceareaid==null){
            showSnackBar("Please select Area");
        }else{
           if(ProductFragment.list!=null&&ProductFragment.list.length>0){
               for(final Product p:ProductFragment.list){
                   if(p.ischanged){
                       new AlertDialog.Builder(this).setTitle("Do You Want to Add "+p.name +" To Your Cart?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               p.ischanged=Boolean.FALSE;
                                addProduct(p,p.cutsize);
                               OrderAction();
                           }
                       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                p.ischanged=Boolean.FALSE;
                               OrderAction();
                           }
                       }).setCancelable(false).create().show();
                       return;
                   }
               }
           }
            if(orderGroup.orders.size()==0){
                showSnackBar("Order is Empty");
            }else if(orderGroup.getAmount()==0){
                showSnackBar("Order Amount Can't be zero");
            }else{
                Intent intent=new Intent(this, OrderDetailsActivity.class);
                startActivityForResult(intent, ORDERDETAILACTIVITY);
            }
        }
    }
    private void showSnackBar(String data){
        new SnackBar(this,data).show();
    }
    public static void setOrderCount(Integer count){
        TextView txt=(TextView) toolbar.findViewById(R.id.actionbar_notifcation_textview);
        txt.setText(count.toString());
    }
    public  void showCategory(){
        isitempage=false;
        setTitle("Category");
        findViewById(R.id.back_button).setVisibility(View.GONE);
        if(catfrag!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, catfrag).commit();
            return;
        }
        ResponseInfo res1 = ServiceCall.getActiveCategory(SplashScreenActivity.companyid);
        if(res1!=null){
            catfrag=CategoryFragment.newInstance((res1.getOutput()));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, catfrag).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CategoryFragment.newInstance()).commit();
        }
    }
    Boolean isitempage=false;
    public void showProducts(Category category){
        if(orderGroup.deliveryscheduleid==null){
            showSnackBar("Please select Delivery Time");
        }else if(orderGroup.serviceareaid==null){
            showSnackBar("Please select Area");
        }else{
            isitempage=true;
            setTitle(category.name);
            findViewById(R.id.back_button).setVisibility(View.VISIBLE);
            findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCategory();
                }
            });
            ResponseInfo res1 = ServiceCall.getActiveProduct(category.id);
            if(res1!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ProductFragment.newInstance((res1.getOutput()))).commit();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ProductFragment.newInstance()).commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
      if(isopen){
          Drawer.closeDrawers();
          return;
      }

        if(isitempage){
            showCategory();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void addProduct(Product product,int size) {
      if(product.qty==0){
          showSnackBar("Quantity Can't be Zero");
          return;
      }
       orderGroup.addProduct(product,size);
        refreshOrder();

    }
    private  void refreshOrder(){
        if(orderGroup ==null){
            orderGroup = new OrderGroup();
            orderGroup.companyid=SplashScreenActivity.companyid;
            orderGroup.orders=new ArrayList<Order>();
        }
        setOrderCount(orderGroup.orders.size());
        amountbtn.setText(new BigDecimal(orderGroup.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ORDERDETAILACTIVITY){
            refreshOrder();
        }
    }

    public void OnPageClicked(String pageName) {
        Drawer.closeDrawers();
        switch (pageName){
            case "Profile":{
                Intent in=new Intent(this,RegisterActivity.class);
                startActivity(in);
                finish();
                break;
            }
            case "My Orders":{
                Intent in=new Intent(this,OrderHistory.class);
                startActivity(in);
                break;
            }
            case "Cancelled Orders":{
                Intent in=new Intent(this,OrderHistory.class);
                in.putExtra("canceled",true);
                startActivity(in);
                break;
            }
            case "Feedback":{
                Intent in=new Intent(this,FeedbackActivity.class);
                startActivity(in);
                break;
            }
            case "Terms & Conditions":{
                Intent in=new Intent(this,TermsActivity.class);
                startActivity(in);
                break;
            }
            case "Logout":{
                Intent in=new Intent(this,LoginActivity.class);
                SharedPreferences.Editor edit=SplashScreenActivity.getSettings().edit();
                edit.remove("userinfo");
                edit.apply();
                SplashScreenActivity.userinfo=null;
                startActivity(in);
                finish();
                break;
            }
        }
    }


    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

  /*  @Override
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
    }*/
 }

