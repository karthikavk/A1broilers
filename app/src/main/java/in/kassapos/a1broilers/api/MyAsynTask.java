package in.kassapos.a1broilers.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;


public class MyAsynTask extends AsyncTask<URLConnection, Integer, String> {

	protected StringBuffer sb = new StringBuffer();
	protected ProgressDialog dialog;
	protected Context context;
	protected ResponseInfo res;
	URLConnection conn;

	public MyAsynTask(Activity context) {
		//this.dialog =  ProgressDialog.show(context,"","Loading");
		this.context = context;
	}
	public MyAsynTask(Fragment context) {
		//this.dialog =  ProgressDialog.show(context,"","Loading");
		this.context = context.getActivity();
	}

	@Override
	protected void onPreExecute() {
		if(dialog!=null){
			dialog.show();
		}
	}

	@Override
	protected String doInBackground(URLConnection... params) {
		conn = params[0];
		
		BufferedReader rd;
		try {
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sb.append("Network Error");
			
			e.printStackTrace();
			return null;
		}
		
		res=new Gson().fromJson(sb.toString(), ResponseInfo.class);
		if(res.getIsError()){
			
			return null;
		}
		return res.getOutput();
	}
	
	
	protected void onPostExecute(String result) {
		
		if(dialog!=null){
			try{
			dialog.dismiss();
			
			}catch(Throwable t){}
		}
		if(sb.toString().equalsIgnoreCase("Network Error")){
			Toast.makeText(context, "NetWork Error!.Please Check your Internet Connection.", Toast.LENGTH_LONG).show();
		}else if(res.getIsError()){
			Toast.makeText(context, res.getErrors().get(0), Toast.LENGTH_LONG).show();
		}
	}
}
