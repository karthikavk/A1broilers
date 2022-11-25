package in.kassapos.a1broilers;


import java.sql.Timestamp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import in.kassapos.a1broilers.api.EmailSend;

public class BaseActivity{
	
	public BaseActivity(final Activity context){
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() 
			{
				@Override
				public void uncaughtException(Thread paramThread, Throwable e) 
					{
						//com.reddyice.util.Constants.masterSyncedStatus=true;
						
						final String DOUBLE_LINE_SEP = "\r\n\r\n";
						final String SINGLE_LINE_SEP = "\r\n";
						StackTraceElement[] arr = e.getStackTrace();
						final StringBuffer report = new StringBuffer(e.toString());
						final String lineSeperator = "-------------------------------\n\n";
						report.append(DOUBLE_LINE_SEP);
						report.append("--------- Time:" +new Timestamp(System.currentTimeMillis())+" ---------\n\n");
						report.append("--------- Stack trace ---------\n\n");
						for (int i = 0; i < arr.length; i++)
							{
								report.append("    ");
								report.append(arr[i].toString());
								report.append(SINGLE_LINE_SEP);
							}
						
						// If the exception was thrown in a background thread inside
						// AsyncTask, then the actual exception can be found with
						// getCause
						Throwable cause = e.getCause();
						
						if (cause != null) 
							{
								report.append(lineSeperator);
								report.append("--------- Cause ---------\n\n");
								report.append(cause.toString());
								report.append(DOUBLE_LINE_SEP);
								arr = cause.getStackTrace();
								
								for (int i = 0; i < arr.length; i++) 
									{
										report.append("    ");
										report.append(arr[i].toString());
										report.append(SINGLE_LINE_SEP);
									}
							}
						// Getting the Device brand,model and sdk verion details.
        
						String id ="";
						String tnumber="";
						
						try
							{
								TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
								
								id=tManager.getDeviceId();
								tnumber=tManager.getLine1Number();
								/*  id = Secure.getString(getContentResolver(),
								Secure.ANDROID_ID);*/
							}
						catch(Exception d)
							{
							
							}
						
						report.append(lineSeperator);
						report.append("--------- Device ---------\n\n");
						report.append("IMEI: ");
						report.append(id);
						report.append(SINGLE_LINE_SEP);
						report.append("Brand: ");
						report.append(Build.BRAND);
						report.append(SINGLE_LINE_SEP);
						report.append("Device: ");
						report.append(Build.DEVICE);
						report.append(SINGLE_LINE_SEP);
						report.append("Model: ");
						report.append(Build.MODEL);
						report.append(SINGLE_LINE_SEP);
						report.append("Metric: ");
						
						int density = context.getResources().getDisplayMetrics().densityDpi;

						switch (density) 
							{
								case DisplayMetrics.DENSITY_LOW:
									report.append("LDPI ");
								break;
								
								case DisplayMetrics.DENSITY_MEDIUM:
									report.append("MDPI ");
								break;
      
								case DisplayMetrics.DENSITY_HIGH:
									report.append("HDPI ");
								break;
								
								case DisplayMetrics.DENSITY_XHIGH:
									report.append("XHDPI ");
								break;
							}

						DisplayMetrics dm = new DisplayMetrics();
						context.getWindowManager().getDefaultDisplay().getMetrics(dm);
						
						report.append(String.valueOf(dm.widthPixels) + "x" + String.valueOf(dm.heightPixels) + "  " + String.valueOf(dm.densityDpi) + "dpi") ;
						report.append(SINGLE_LINE_SEP);
						report.append("Id: ");
						report.append(Build.ID);
						report.append(SINGLE_LINE_SEP);
						report.append("Product: ");
						report.append(Build.PRODUCT);
						report.append(SINGLE_LINE_SEP);
						report.append(lineSeperator);
						report.append("--------- Firmware ---------\n\n");
						report.append("SDK: ");
						report.append(Build.VERSION.SDK);
						report.append(SINGLE_LINE_SEP);
						report.append("Release: ");
						report.append(Build.VERSION.RELEASE);
						report.append(SINGLE_LINE_SEP);
						report.append("Incremental: ");
						report.append(Build.VERSION.INCREMENTAL);
						report.append(SINGLE_LINE_SEP);
						report.append(lineSeperator);
						
						if(true)
							{
								System.out.println(":::::::::::::::::::::::::::: Base Actvity Called ");
							}	
						//getCrashIntent(report.toString());
						
						Editor edit=SplashScreenActivity.getSettings().edit();
						edit.putString("error", report.toString());
						edit.commit();
						new EmailSend("palanibe91@gmail.com", context.getString(R.string.app_name), report.toString()).run();
//						try {
//							wait(60000);
//						} catch (InterruptedException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(10);
						
					}
			});

	}
	
    /*	@Override
    	protected void onCreate(Bundle savedInstanceState) 
    		{
    			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() 
    				{
    					@Override
    					public void uncaughtException(Thread paramThread, Throwable e) 
    						{
    							//com.reddyice.util.Constants.masterSyncedStatus=true;
    							
    							final String DOUBLE_LINE_SEP = "\r\n\r\n";
    							final String SINGLE_LINE_SEP = "\r\n";
    							StackTraceElement[] arr = e.getStackTrace();
    							final StringBuffer report = new StringBuffer(e.toString());
    							final String lineSeperator = "-------------------------------\n\n";
    							report.append(DOUBLE_LINE_SEP);
    							report.append("--------- Time:" +new Timestamp(System.currentTimeMillis())+" ---------\n\n");
								report.append("--------- Stack trace ---------\n\n");
    							for (int i = 0; i < arr.length; i++)
    								{
    									report.append("    ");
    									report.append(arr[i].toString());
    									report.append(SINGLE_LINE_SEP);
    								}
    							
    							// If the exception was thrown in a background thread inside
    							// AsyncTask, then the actual exception can be found with
    							// getCause
    							Throwable cause = e.getCause();
    							
    							if (cause != null) 
    								{
    									report.append(lineSeperator);
    									report.append("--------- Cause ---------\n\n");
    									report.append(cause.toString());
    									report.append(DOUBLE_LINE_SEP);
    									arr = cause.getStackTrace();
    									
    									for (int i = 0; i < arr.length; i++) 
    										{
    											report.append("    ");
    											report.append(arr[i].toString());
    											report.append(SINGLE_LINE_SEP);
    										}
    								}
    							// Getting the Device brand,model and sdk verion details.
                
    							String id ="";
    							String tnumber="";
    							
    							try
    								{
    									TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    									
    									id=tManager.getDeviceId();
    									tnumber=tManager.getLine1Number();
    									  id = Secure.getString(getContentResolver(),
										Secure.ANDROID_ID);
    								}
    							catch(Exception d)
    								{
    								
    								}
    							
    							report.append(lineSeperator);
    							report.append("--------- Device ---------\n\n");
    							report.append("IMEI: ");
    							report.append(id);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Brand: ");
    							report.append(Build.BRAND);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Device: ");
    							report.append(Build.DEVICE);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Model: ");
    							report.append(Build.MODEL);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Metric: ");
    							
    							int density = getResources().getDisplayMetrics().densityDpi;
 
    							switch (density) 
    								{
    									case DisplayMetrics.DENSITY_LOW:
    										report.append("LDPI ");
    									break;
    									
    									case DisplayMetrics.DENSITY_MEDIUM:
    										report.append("MDPI ");
    									break;
              
    									case DisplayMetrics.DENSITY_HIGH:
    										report.append("HDPI ");
    									break;
    									
    									case DisplayMetrics.DENSITY_XHIGH:
    										report.append("XHDPI ");
    									break;
    								}
 
    							DisplayMetrics dm = new DisplayMetrics();
    							getWindowManager().getDefaultDisplay().getMetrics(dm);
    							
    							report.append(String.valueOf(dm.widthPixels) + "x" + String.valueOf(dm.heightPixels) + "  " + String.valueOf(dm.densityDpi) + "dpi") ;
    							report.append(SINGLE_LINE_SEP);
    							report.append("Id: ");
    							report.append(Build.ID);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Product: ");
    							report.append(Build.PRODUCT);
    							report.append(SINGLE_LINE_SEP);
    							report.append(lineSeperator);
    							report.append("--------- Firmware ---------\n\n");
    							report.append("SDK: ");
    							report.append(Build.VERSION.SDK);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Release: ");
    							report.append(Build.VERSION.RELEASE);
    							report.append(SINGLE_LINE_SEP);
    							report.append("Incremental: ");
    							report.append(Build.VERSION.INCREMENTAL);
    							report.append(SINGLE_LINE_SEP);
    							report.append(lineSeperator);
    							
    							if(true)
    								{
    									System.out.println(":::::::::::::::::::::::::::: Base Actvity Called ");
    								}	
    							//getCrashIntent(report.toString());
    							
    							Editor edit=FirstActivity.getSettings().edit();
    							edit.putString("error", report.toString());
    							edit.commit();
    							new EmailSend("palanibe91@gmail.com", "agasthya", report.toString()).run(); 
//    							try {
//									wait(60000);
//								} catch (InterruptedException e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
    							android.os.Process.killProcess(android.os.Process.myPid());
    							System.exit(10);
    							
    						}
    				});
    		}
    	*/
	}

