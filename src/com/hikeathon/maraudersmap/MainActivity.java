package com.hikeathon.maraudersmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


@SuppressLint({"UnlocalizedSms"})
public class MainActivity extends Activity
{
  static int KEY_UP_LONG_PRESS = 0;
  public static final int PLAYBACK_STREAM = 1;
  static final int REQUEST_VIDEO_CAPTURE = 1;
  public static String curlocation;
  static String emergencynumber = "100";
  public static Double latitude_sms;
  public static Double longitude_sms;
  String _lat = null;
  String _lon = null;
  AppLocationService appLocationService;
  String country;
  DatabaseHandler db = new DatabaseHandler(this, "womensafety", null, 1);
  int i = 0;
  IntentFilter intentfliter;
  ImageView iv_sos;
  Double lat = Double.valueOf(0.0D);
  String latLongString = "";
  Double lng = Double.valueOf(0.0D);
  private AudioManager mAudioManager = null;
  BroadcastReceiver mReceiver = null;
  private VideoView mVideoView;
  Integer maxVolume;
  TextView myLocationDtls;
  TextView myLocationText;
  Integer newVolume;
  String phn;
  List<String> phone;
  RelativeLayout rl_main;
  TextView tv_sos;

  static
  {
    KEY_UP_LONG_PRESS = 0;
    curlocation = "More Accurate Location Couldn't be fetch..";
  }

  private void updateWithNewLocation(Location paramLocation, int paramInt)
    throws IOException
  {
    if (paramLocation != null)
    {
      this.lat = Double.valueOf(paramLocation.getLatitude());
      this.lng = Double.valueOf(paramLocation.getLongitude());
      latitude_sms = this.lat;
      longitude_sms = this.lng;
      this.latLongString = ("Lat:" + this.lat.toString() + ", Long:" + this.lng.toString());
    }
    
      if (paramInt == 1)
        this.myLocationDtls.setText("More Accurate Location Couldn't be fetch!!! \nConnection UnAvailable!!! / GPS not Enabled!!!");
      try
      {
        Geocoder localGeocoder = new Geocoder(this, Locale.getDefault());
        if ((!this.lat.toString().equals(null)) && (!this.lng.toString().equals(null)))
        {
          List localList = localGeocoder.getFromLocation(this.lat.doubleValue(), this.lng.doubleValue(), 1);
          if (localList.size() > 0)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(((Address)localList.get(0)).getAddressLine(0));
            localStringBuilder.append("\n");
            localStringBuilder.append(((Address)localList.get(0)).getAddressLine(1));
            localStringBuilder.append(", ");
            localStringBuilder.append(((Address)localList.get(0)).getCountryName());
            if ((!localStringBuilder.toString().equals("")) && (!localStringBuilder.toString().equals(null)))
            {
              this.myLocationDtls.setText(localStringBuilder.toString());
              curlocation = ((Address)localList.get(0)).getAddressLine(0) + ((Address)localList.get(0)).getPostalCode();
            }
            this.country = ((Address)localList.get(0)).getCountryName();
          }
        }
        this.myLocationText.setText(this.latLongString);
        if ((paramInt == 0) && (this.myLocationDtls.getText().equals(" ")))
          this.myLocationDtls.setText("GPS not Enabled!!!!!");
        emergencynumber = "100";

        
        
        
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  

  public void dispatchTakeVideoIntent()
  {
    Intent localIntent = new Intent("android.media.action.VIDEO_CAPTURE");
    if (localIntent.resolveActivity(getPackageManager()) != null)
      startActivityForResult(localIntent, 1);
  }

  public final boolean isInternetOn()
  {
    int j = 0;
    ConnectivityManager localConnectivityManager = (ConnectivityManager)getSystemService("connectivity");
    if ((localConnectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) || (localConnectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED))
      j = 1;
    while (true)
    {
    	
      if ((localConnectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED) || (localConnectivityManager.getNetworkInfo(1).getState() != NetworkInfo.State.DISCONNECTED))
        continue;
      if(j==0)
  		return false;
  	else
  		return true;
    }
  }
 
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
      switch (item.getItemId()) {
      case R.id.hospitals:
    	  
    	  Uri uri1 = Uri.parse("https://www.google.co.in/search?q=hospitals+near+me");
    	  Intent launchBrowser1 = new Intent(Intent.ACTION_VIEW, uri1); 
    	  startActivity(launchBrowser1);
            break;
      case R.id.policeStations :
    	  
    		  Uri uri2 = Uri.parse("https://www.google.co.in/search?q=police%20stations+near+me");
    	  Intent launchBrowser2 = new Intent(Intent.ACTION_VIEW, uri2); 
    	  startActivity(launchBrowser2);
          break;
      case R.id.cabs :
    	  PackageManager pm = getPackageManager();
    	  Intent intent = pm.getLaunchIntentForPackage("com.scootapp");
    	  startActivity(intent);
               break;
      case R.id.safeLocation:
               break;
      case R.id.info:
    	       break;
      }
      return true;
  }
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1) && (paramInt2 == -1));
    try
    {
      Uri localUri = paramIntent.getData();
      this.mVideoView.setVideoURI(localUri);
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void onBackPressed()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.HOME");
        localIntent.setFlags(PendingIntent.FLAG_CANCEL_CURRENT);
        MainActivity.this.startActivity(localIntent);
      }
    }).setNegativeButton("No", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    localBuilder.create().show();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    Button localButton1 = (Button)findViewById(R.id.btn_contact);
    Button localButton2 = (Button)findViewById(R.id.btn_share);
    Button localButton3 = (Button)findViewById(R.id.btn_info);
    Button localButton4 = (Button)findViewById(R.id.btn_video);
    Button localButton5 = (Button)findViewById(R.id.map);
    this.myLocationText = ((TextView)findViewById(R.id.tv_addrs));
    this.iv_sos = ((ImageView)findViewById(R.id.iv_sos));
    this.rl_main = ((RelativeLayout)findViewById(R.id.rl_main));
    this.tv_sos = ((TextView)findViewById(R.id.tv_sos));
    this.myLocationDtls = ((TextView)findViewById(R.id.tv_details));
    LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.ll_video);
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.ll_info);
    this.mAudioManager = ((AudioManager)getSystemService("audio"));
    this.tv_sos.setText("Click On SOS To Send Ur Location!!");
    startService(new Intent(this, AppLocationService.class));
    this.appLocationService = new AppLocationService(this);
    localLinearLayout1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          MainActivity.this.dispatchTakeVideoIntent();
          return;
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
        }
      }
    });
    localButton4.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          MainActivity.this.dispatchTakeVideoIntent();
          return;
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
        }
      }
    });
    try
    {
      Location localLocation1 = this.appLocationService.getLocation("gps");
      if (localLocation1 != null)
        updateWithNewLocation(localLocation1, 0);
      while (true)
      {
        this.iv_sos.setOnClickListener(new View.OnClickListener()
        {
          private int contacts;

          public void onClick(View paramView)
          {
            Vibrator localVibrator = (Vibrator)MainActivity.this.getSystemService("vibrator");
            long[] arrayOfLong = new long[19];
            arrayOfLong[1] = 200;
            arrayOfLong[2] = 200;
            arrayOfLong[3] = 200;
            arrayOfLong[4] = 200;
            arrayOfLong[5] = 200;
            arrayOfLong[6] = 500;
            arrayOfLong[7] = 500;
            arrayOfLong[8] = 200;
            arrayOfLong[9] = 500;
            arrayOfLong[10] = 200;
            arrayOfLong[11] = 500;
            arrayOfLong[12] = 500;
            arrayOfLong[13] = 200;
            arrayOfLong[14] = 200;
            arrayOfLong[15] = 200;
            arrayOfLong[16] = 200;
            arrayOfLong[17] = 200;
            arrayOfLong[18] = 1000;
            localVibrator.vibrate(arrayOfLong, -1);
            this.contacts = MainActivity.this.db.getContactsCount();
            if (this.contacts == 0)
              Toast.makeText(MainActivity.this, "You Haven't Added Any Emergency Contact Yet!!!!", 0).show();
            while (true)
            {
              
              MainActivity.this.smsSend();
              return;
            }
          }
        });
        localLinearLayout2.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            //Intent localIntent = new Intent(MainActivity.this, Info.class);
            //MainActivity.this.startActivity(localIntent);
          }
        });
        localButton3.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
           // Intent localIntent = new Intent(MainActivity.this, Info.class);
            //MainActivity.this.startActivity(localIntent);
        	  openOptionsMenu();
          }
        });
        localButton1.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            Intent localIntent = new Intent(MainActivity.this, Contacts.class);
            MainActivity.this.startActivity(localIntent);
          }
        });
        localButton2.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            Intent localIntent = new Intent(MainActivity.this, Share.class);
            MainActivity.this.startActivity(localIntent);
          }
        });
        localButton5.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            MainActivity.this._lat = MainActivity.this.lat.toString();
            MainActivity.this._lon = MainActivity.this.lng.toString();
            try
            {
              if ((MainActivity.this._lat.equals("")) || (MainActivity.this._lon.equals("")))
              {
                Toast.makeText(MainActivity.this, "GPS not enabled or SIM card not inserted !!!", 0).show();
              }
              else
              {
                Toast.makeText(MainActivity.this, "Go to Location And Security Settings \n  and check either wireless network or gps satellite..", 0).show();
                new MainActivity.LoadMap(MainActivity.this).execute(new Void[0]);
              }
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
          }
        });
        return;
        /* localLocation2 = this.appLocationService.getLocation("network");
        if (localLocation2 == null)
          continue;
        updateWithNewLocation(localLocation2, 1);*/
      }
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public boolean onCreateOptionsMenu(Menu menu)
  {
	  super.onCreateOptionsMenu(menu);
  getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    int j = 1;
    if (26 == paramKeyEvent.getKeyCode());
    while (true)
    {
      
      if ((24 == paramKeyEvent.getKeyCode()) && (paramKeyEvent.isLongPress()))
      {
        KEY_UP_LONG_PRESS = j;
        try
        {
          dispatchTakeVideoIntent();
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        continue;
      }
      KEY_UP_LONG_PRESS = 0;
      
      return (super.onKeyDown(paramInt, paramKeyEvent));
      
     
    }
  }

  protected void onPause()
  {
    if (this.mReceiver != null)
    {
      getApplicationContext().registerReceiver(this.mReceiver, this.intentfliter);
      this.mAudioManager.registerMediaButtonEventReceiver(getCallingActivity());
    }
    super.onPause();
  }

  protected void onResume()
  {
    startService(new Intent(this, AppLocationService.class));
    super.onResume();
  }

  protected void onStop()
  {
    super.onStop();
  }

  protected void smsSend()
  {
    this.phone = this.db.getAllNumber();
    SmsManager localSmsManager = SmsManager.getDefault();
    int j = 0;
    while (true)
    {
      if (j >= this.phone.size())
      {
        Toast.makeText(this, "Sms Sent To Emergency Contacts!!!!", 0).show();
        return;
      }
      String str = (String)this.phone.get(j);
      try
      {
        localSmsManager.sendTextMessage(str, null, "I am in trouble !!! Need Help!!! My Location: " + curlocation + " Map Location : " + "https://maps.google.com/maps?q=" + this.lat.toString() + "," + this.lng.toString(), null, null);
        j++;
      }
      catch (Exception localException)
      {
        while (true)
          localException.printStackTrace();
      }
    }
  }

  
     

  public class LoadMap extends AsyncTask<Void, Void, Void>
  {
    Uri uri;

    LoadMap(Activity act)
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", this.uri));
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
    }

    protected void onPreExecute()
    {
      this.uri = Uri.parse("geo:" + MainActivity.this._lat + "," + MainActivity.this._lon);
      super.onPreExecute();
    }
  }
}