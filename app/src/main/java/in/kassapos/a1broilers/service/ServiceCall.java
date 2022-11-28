package in.kassapos.a1broilers.service;


import android.app.Activity;
import android.app.ProgressDialog;
import androidx.fragment.app.Fragment;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//import in.kassapos.chickenshop.api.Advertisment;
import in.kassapos.a1broilers.api.Advertisment;
import in.kassapos.chickenshop.api.Offer;
import in.kassapos.chickenshop.api.OrderMasterBean;
import in.kassapos.chickenshop.api.User;
import in.kassapos.a1broilers.OrderDetailsActivity;
import in.kassapos.a1broilers.OrderHistory;
import in.kassapos.a1broilers.RegisterActivity;
import in.kassapos.a1broilers.api.MyAsynTask;
import in.kassapos.a1broilers.api.OrderGroup;
import in.kassapos.a1broilers.api.ResponseInfo;
import in.kassapos.a1broilers.fragment.CategoryFragment;
import com.google.gson.*;

public class ServiceCall {
  // public static String _SERVER = "bismillahproteins.in";
    public static String _SERVER = "a1broilers.in";
    public static String _URL;
    public static String _ImagePath;
    private static Gson gsondate=new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
    private static Gson gsondateonly=new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    static {
        switch (3){
            case 1:
                _SERVER = "192.168.1.31:8080";
                break;
            case 2:
                _SERVER = "kassaserver.dyndns.org:8480";
                break;
            case 3:
                _SERVER="a1broilers.in";
        }
        _URL="http://"+_SERVER+"/kassachicken/ws/";
        _ImagePath="http://"+_SERVER;
    }

    public static String loginaction(String selecteduser, String password) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "userlogin");
            URLConnection conn = url.openConnection();

            conn.addRequestProperty("password1",password);
            conn.addRequestProperty("username",selecteduser);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
    public static ResponseInfo getCompanyid(String domain) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getcompany");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("domain",domain);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static ResponseInfo getActiveServiceArea(String companyid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getactiveservicearea");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("companyid",companyid);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static ResponseInfo getOrders(String companyid,String userid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getordersbyuser");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("companyid",companyid);
            conn.addRequestProperty("userid",userid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static ResponseInfo updateRegid(User userinfo, String regid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "updateregid");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("info",new Gson().toJson(userinfo));
            conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static void PlaceOrder(final Activity activity,OrderGroup orderGroup) {

        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "placeorder");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("info", new Gson().toJson(orderGroup));
            new MyAsynTask(activity){
                protected void onPreExecute() {
                    dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                   if(result!=null){
                       if(activity instanceof OrderDetailsActivity){
                           ((OrderDetailsActivity)activity).placeOrderSuccess(result);
                       }
                   }
                    //AppConstants.curr_orderview=p.getOrderview();
                    //	First.curr_rtdo=p.getSelorder();
                    //return !p.getOrderstatus();


                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static ResponseInfo getActiveDeliverySchedule(String companyid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getactivedeliveryschedule");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("companyid",companyid);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static ResponseInfo getActiveProduct(String companyid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getactiveproduct");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("companyid",companyid);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static ResponseInfo getActiveCategory(String companyid) {
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL(_URL + "getactivecategory");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("companyid",companyid);
            //conn.addRequestProperty("regid",regid);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
           // ResponseInfo responseInfo=new Gson().fromJson(sb.toString(),ResponseInfo.class);
            ResponseInfo responseInfo=gsondateonly.fromJson(sb.toString(),ResponseInfo.class);

            return responseInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void getAdvertisment(final Fragment activity,String comid){
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL( _URL+"getactiveadvertisment");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("companyid", comid);
            new MyAsynTask(activity){
                protected void onPreExecute() {
                  //  dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    Advertisment[] p=new Gson().fromJson(result, Advertisment[].class);
                    //AppConstants.curr_orderview=p.getOrderview();
                    //	First.curr_rtdo=p.getSelorder();
                    //return !p.getOrderstatus();
                    if(activity instanceof CategoryFragment){
                        ((CategoryFragment)activity).advertismentSuccess(p);
                    }

                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void getOffer(final Activity activity, String comid, String deviceId){
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL( _URL+"getactiveofferformobile");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("companyid", comid);
            conn.addRequestProperty("imei", deviceId);
            new MyAsynTask(activity){
                protected void onPreExecute() {
                    //  dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    Offer[] p=gsondateonly.fromJson(result, Offer[].class);
                    //AppConstants.curr_orderview=p.getOrderview();
                    //	First.curr_rtdo=p.getSelorder();
                    //return !p.getOrderstatus();
                    if(activity instanceof OrderDetailsActivity){
                        ((OrderDetailsActivity)activity).offerSuccess(p);
                    }

                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void cancelOrder(final Activity activity,OrderMasterBean orderMasterBean){
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL( _URL+"rejectorder");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("info", gsondateonly.toJson(orderMasterBean));
            new MyAsynTask(activity){
                protected void onPreExecute() {
                    dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    Boolean p=Boolean.valueOf(result);
                    //AppConstants.curr_orderview=p.getOrderview();
                    //	First.curr_rtdo=p.getSelorder();
                    //return !p.getOrderstatus();
                    if(p){
                        if(activity instanceof OrderHistory){
                            ((OrderHistory)activity).rejectedOrder();
                        }
                    }else{
                        Toast.makeText(context,"Please Try Again",Toast.LENGTH_LONG).show();
                    }


                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void register(final Activity activity,User user){
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL( _URL+"adduser");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("info", new Gson().toJson(user));
            new MyAsynTask(activity){
                protected void onPreExecute() {
                    dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    User p=gsondate.fromJson(result, User.class);
                    //AppConstants.curr_orderview=p.getOrderview();
                    //	First.curr_rtdo=p.getSelorder();
                    //return !p.getOrderstatus();
                    if(activity instanceof RegisterActivity){
                        ((RegisterActivity)activity).registerSuccess(p);
                    }

                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void updateUser(final Activity activity,User user){
        StringBuilder sb=new StringBuilder();
        try{
            URL url = new URL( _URL+"updateuser");
            URLConnection conn = url.openConnection();
            // String tmp = new Gson().toJson(AppConstants.client.id);
            conn.addRequestProperty("info", new Gson().toJson(user));
            new MyAsynTask(activity){
                protected void onPreExecute() {
                    dialog= ProgressDialog.show(context, "Loading...", "Please Wait...");
                };
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if(result!=null){
                        User p=gsondate.fromJson(result, User.class);
                        //AppConstants.curr_orderview=p.getOrderview();
                        //	First.curr_rtdo=p.getSelorder();
                        //return !p.getOrderstatus();
                        if(activity instanceof RegisterActivity){
                            ((RegisterActivity)activity).registerSuccess(p);
                        }
                    }


                }
            }.execute(new URLConnection[]{conn});


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
