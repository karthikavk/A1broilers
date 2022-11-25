package in.kassapos.a1broilers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import in.kassapos.a1broilers.api.EmailSend;


public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.an3));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Feedback");
        findViewById(R.id.email_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialEditText text= (MaterialEditText) findViewById(R.id.text);
                String s=text.getText().toString().trim();
                if(s.length()==0){
                    Toast.makeText(FeedbackActivity.this,"Please enter your feedbcak",Toast.LENGTH_LONG).show();
                }else{
                    new EmailSend("a1broilers.cmc@gmail.com","Feedback From Customer",s).start();
                    text.setText("");
                    Toast.makeText(FeedbackActivity.this,"Feedback Sent Successfully",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
