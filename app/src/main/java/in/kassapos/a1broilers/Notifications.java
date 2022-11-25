package in.kassapos.a1broilers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.kassapos.a1broilers.adapter.DiscussArrayAdapter;
import in.kassapos.a1broilers.api.OneComment;
import in.kassapos.a1broilers.api.ReceivedData;
import in.kassapos.a1broilers.db.MyDbHelper;


public class Notifications extends AppCompatActivity {
    MyDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<OneComment> list=new ArrayList<>();
        try {
             dbHelper=new MyDbHelper(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        List<ReceivedData> tl = dbHelper.getObject(ReceivedData.class);
        Collections.sort(tl);
        for(ReceivedData t:tl){
            OneComment o=new OneComment(true,t.data);
            list.add(o);
        }
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Your Notifications");
        ListView lv = (ListView) findViewById(R.id.listView1);

        DiscussArrayAdapter adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.chatlayout,list);

        lv.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,SplashScreenActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
