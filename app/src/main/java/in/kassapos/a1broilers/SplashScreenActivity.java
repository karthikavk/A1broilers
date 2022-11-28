package in.kassapos.a1broilers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import com.rey.material.app.ThemeManager;
import com.rey.material.widget.ProgressView;

import in.kassapos.chickenshop.api.User;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.db.MyDbHelper;
import in.kassapos.a1broilers.service.ServiceCall;


public class SplashScreenActivity extends AppCompatActivity {
    public static final String GCMREGID ="gcmregid" ;
    private static int SPLASH_TIME_OUT = 2000;

    public static SharedPreferences getSettings() {
        return settings;
    }

    private static SharedPreferences settings;



    private static MyDbHelper db;
    public static String companyid;
    public static User userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ThemeManager.init(this, 1, 0, null);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            db=new MyDbHelper(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ProgressView pv_circular = (ProgressView) findViewById(R.id.progress_pv_circular);
        pv_circular.start();
        new BaseActivity(this);
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("gcmregistered"));
        companyid=settings.getString("companyid",null);
        if(companyid==null){
            SPLASH_TIME_OUT=10;
        }
       new Handler().postDelayed(new Runnable() {


           @Override
           public void run() {
                companyid=settings.getString("companyid",null);
               if(companyid!=null){
                   nextActivity();
               }else{
                   ResponseInfo res = ServiceCall.getCompanyid("mydomain");
                   if(res!=null){
                        if(res.getIsError()){
                            new SnackBar(SplashScreenActivity.this, res.getErrors().get(0),"OK",null).show();
                        }else{
                            companyid=res.getOutput();
                            SharedPreferences.Editor edit = settings.edit();
                            edit.putString("companyid",companyid);
                            edit.apply();
                            nextActivity();
                        }
                   }else{
                       new SnackBar(SplashScreenActivity.this,"Please Check your Internet Connection","OK", new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                           }
                       }).show();
                   }
               }


           }
       }, SPLASH_TIME_OUT);
    }
    private void nextActivity(){
        ConnectionDetector cd = new ConnectionDetector(this);
        if(!cd.isConnectingToInternet()){
            new AlertDialog.Builder(this).setTitle("Please Check your Internet Connection and try again!").setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create().show();
            return;
        }
        if(settings.contains("userinfo")&&settings.getString("userinfo",null)!=null){
            userinfo=new Gson().fromJson(settings.getString("userinfo",null),User.class);
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }


    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            try{
                String message = intent.getStringExtra("message");
                if(userinfo!=null)
                ServiceCall.updateRegid(userinfo,settings.getString(GCMREGID,null));
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
                Log.e("receiver", "Got message: " + message);
            }catch (Exception e) {
                // TODO: handle exception
            }
        }
    };

    public static MyDbHelper getDb() {
        return db;
    }
}
