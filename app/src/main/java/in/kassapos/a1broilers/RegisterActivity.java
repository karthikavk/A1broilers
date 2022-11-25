package in.kassapos.a1broilers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;



import in.kassapos.chickenshop.api.User;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.api.Servicearea;
import in.kassapos.a1broilers.service.ServiceCall;


public class RegisterActivity extends AppCompatActivity {
    private EditText name,email,contact,password,address1,address2,landmark,city;
    private Spinner area;
    Servicearea[] serviceareas;
    EditText[] edittextlist;
    User olduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        new BaseActivity(this);
        if(SplashScreenActivity.userinfo!=null){
            olduser=SplashScreenActivity.userinfo;
        }
        findViewById(R.id.signUpTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        area = (Spinner)findViewById(R.id.pincode);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.emailid);
        contact=(EditText)findViewById(R.id.contactno);
        password=(EditText)findViewById(R.id.password);
        address1=(EditText)findViewById(R.id.address1);
        address2=(EditText)findViewById(R.id.address2);
        landmark=(EditText)findViewById(R.id.landmark);
        city=(EditText)findViewById(R.id.city);
        edittextlist=new EditText[]{name,email,contact,password,address1,address2,landmark,city};
        ResponseInfo res = ServiceCall.getActiveServiceArea(SplashScreenActivity.companyid);
        if(res!=null&&!res.getIsError()){
            serviceareas=new Gson().fromJson(res.getOutput(),Servicearea[].class);

            ArrayAdapter<Servicearea> adapter = new ArrayAdapter<Servicearea>(this, R.layout.row_spn, serviceareas);
            adapter.setDropDownViewResource(R.layout.row_spn);
            area.setAdapter(adapter);
        }
        ButtonRectangle loginButton = (ButtonRectangle) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin();
            }
        });
        if(olduser!=null){
            setUser();
        }
    }

    private void setUser() {
        int index=0;
        for(Servicearea servicearea:serviceareas){
            if(olduser.serviceareaid!=null&&olduser.serviceareaid.equalsIgnoreCase(servicearea.id)){
                break;
            }
            index++;
        }
        area.setSelection(index);
        name.setText(olduser.name);
        email.setText(olduser.userid);
        contact.setText(olduser.contactno);
        password.setText(olduser.password);
        address1.setText(olduser.address1);
        address2=(EditText)findViewById(R.id.address2);
        landmark.setText(olduser.landmark);
        city.setText(olduser.city);
        password.setVisibility(View.GONE);
        ((TextView)findViewById(R.id.textView2)).setText("Update");
        com.gc.materialdesign.views.ButtonRectangle buttonRectangle=( com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.email_sign_in_button);
        com.gc.materialdesign.views.ButtonRectangle update=( com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.update_button);
        buttonRectangle.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLogin();
            }
        });
        (findViewById(R.id.signUpTextView)).setVisibility(View.GONE);
    }

    Boolean isloading=false;
    public  void reset(EditText[] el){
        for(EditText e:el){
            e.setError(null);
        }
    }
    private boolean isEmailValid(String email) {
        //add your own logic
        return email.contains("@");
    }

    public void initLogin() {
        if (isloading ) {
            return;
        }

       reset(edittextlist);
        User u=new User();
        u.name=name.getText().toString().trim();
        u.userid=email.getText().toString().trim();
        u.contactno=contact.getText().toString().trim();
        u.password=password.getText().toString().trim();
        u.address1=address1.getText().toString().trim();
        u.address2=address2.getText().toString().trim();
        u.landmark=landmark.getText().toString().trim();
        u.city=city.getText().toString().trim();
        u.priv="gu";
        u.companyid=SplashScreenActivity.companyid;
      if(area.getSelectedItem()==null){
          return;
      }
       u.serviceareaid=((Servicearea)area.getSelectedItem()).id;
       boolean cancelLogin = false;
        View focusView = null;

        if (TextUtils.isEmpty(u.name)) {
            name.setError(getString(R.string.entername));
            focusView = name;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(u.contactno)) {
            contact.setError(getString(R.string.entercontactno));
            focusView = contact;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(u.userid)) {
            email.setError(getString(R.string.invalid_email));
            focusView = email;
            cancelLogin = true;
        } else if (!isEmailValid(u.userid)) {
            email.setError(getString(R.string.invalid_email));
            focusView = email;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(u.password)&&olduser==null) {
            password.setError(getString(R.string.enterpassword));
            focusView = password;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(u.address1)) {
            address1.setError(getString(R.string.enteraddress1));
            focusView = address1;
            cancelLogin = true;
        }
       /* if (TextUtils.isEmpty(u.address2)) {
            address2.setError(getString(R.string.enteraddress2));
            focusView = address2;
            cancelLogin = true;
        }*/
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
            // show progress spinner, and start background task to login
          //  showProgress(true);
            if(olduser==null) {
                u.created_by = "Mobile";

                ServiceCall.register(this, u);
            }else {
                u.modified_by="Mobile";
                u.id=olduser.id;
                u.active=1;
                ServiceCall.updateUser(this, u);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
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

    public void registerSuccess(User u) {
        SplashScreenActivity.userinfo=u;
        SharedPreferences.Editor edit=SplashScreenActivity.getSettings().edit();
        edit.putString("userinfo",new Gson().toJson(u));
        edit.apply();
        Intent i = new Intent(this, SplashScreenActivity.class);
        startActivity(i);
        finish();
    }
}
