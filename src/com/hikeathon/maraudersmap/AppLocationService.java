package com.hikeathon.maraudersmap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class AppLocationService
  extends Service
  implements LocationListener
{
  private static final long MIN_DISTANCE_FOR_UPDATE = 10L;
  private static final long MIN_TIME_FOR_UPDATE = 120000L;
  Location location;
  protected LocationManager locationManager;
  private MyReceiver mReceiver;
  
  public AppLocationService() {}
  
  public AppLocationService(Context paramContext)
  {
    this.locationManager = ((LocationManager)paramContext.getSystemService("location"));
  }
  
  public Location getLocation(String paramString)
  {
    if (this.locationManager.isProviderEnabled(paramString))
    {
      this.locationManager.requestLocationUpdates(paramString, 120000L, 10.0F, this);
      if (this.locationManager != null)
      {
        this.location = this.locationManager.getLastKnownLocation(paramString);
        return this.location;
      }
    }
    return null;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  public void onCreate()
  {
    IntentFilter localIntentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
    localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
    this.mReceiver = new MyReceiver();
    registerReceiver(this.mReceiver, localIntentFilter);
    super.onCreate();
  }
  
  public void onLocationChanged(Location paramLocation) {}
  
  public void onProviderDisabled(String paramString) {}
  
  public void onProviderEnabled(String paramString) {}
  
  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
}

