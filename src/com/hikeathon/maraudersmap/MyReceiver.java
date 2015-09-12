package com.hikeathon.maraudersmap;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.io.PrintStream;
import java.util.List;

public class MyReceiver
  extends BroadcastReceiver
{
  static int REQUEST_VIDEO_CAPTURE = 0;
  static int countPowerOff;
  static long currTime;
  static long prevTime;
  public static boolean wasScreenOn = true;
  int contacts;
  int i = 0;
  String phn;
  List<String> phone;
  
  static
  {
    prevTime = 0L;
    currTime = 0L;
    countPowerOff = 0;
  }
  
  private void smsSend(List<String> paramList)
  {
    SmsManager localSmsManager = SmsManager.getDefault();
    int j = 0;
    for (;;)
    {
      if (j >= paramList.size()) {
        return;
      }
      String str = (String)paramList.get(j);
      System.out.print((String)paramList.get(j));
      try
      {
        localSmsManager.sendTextMessage(str, null, "I am in trouble !!! I Need Help!!! My Location: " + MainActivity.curlocation + " Map Location : " + "https://maps.google.com/maps?q=" + MainActivity.latitude_sms.toString() + "," + MainActivity.longitude_sms.toString(), null, null);
        j += 1;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          localException.printStackTrace();
        }
      }
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    DatabaseHandler localDatabaseHandler = new DatabaseHandler(paramContext, "womensafety", null, 1);
   /* if (paramIntent.getAction().equals("android.intent.action.SCREEN_OFF"))
    {
      prevTime = System.currentTimeMillis();
      wasScreenOn = false;
    }*/
    for (;;)
    {
      if ((paramIntent.getAction().equals("android.intent.action.SCREEN_ON")) && (countPowerOff >= 2))
      {
        Toast.makeText(paramContext, "Power button clicked thrice,continuosly will trigger sos!!!!!", 1).show();
        if ((currTime <= 0L) || (prevTime <= 0L) || (currTime - prevTime >= 1000L) || (currTime - prevTime <= -1000L)) {}
      }
      try
      {
        String telno = MainActivity.emergencynumber;
        paramIntent = new Intent("android.intent.action.CALL", Uri.parse("tel://" + telno));
        paramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        paramContext.startActivity(paramIntent);
        this.contacts = localDatabaseHandler.getContactsCount();
        if (this.contacts <= 0) {
          Toast.makeText(paramContext, "No Emergency Contact Added!!!!", 1).show();
        }
        for (;;)
        {
          countPowerOff = 0;
          currTime = 0L;
          prevTime = 0L;
          return;
       /*   if (!paramIntent.getAction().equals("android.intent.action.SCREEN_ON")) {
            break;
          }
          currTime = System.currentTimeMillis();
          countPowerOff += 1;
          wasScreenOn = true;
          break;
          this.phone = localDatabaseHandler.getAllNumber();
          smsSend(this.phone);*/
        }
      }
      catch (ActivityNotFoundException paramContextEx)
      {
        for (;;) {}
      }
    }
  }
}

