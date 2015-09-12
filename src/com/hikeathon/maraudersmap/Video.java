package com.hikeathon.maraudersmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class Video
  extends Activity
{
  int REQUEST_VIDEO_CAPTURE = 1;
  private VideoView mVideoView;
  
  public void dispatchTakeVideoIntent()
  {
    Intent localIntent = new Intent("android.media.action.VIDEO_CAPTURE");
    if (localIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(localIntent, this.REQUEST_VIDEO_CAPTURE);
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == this.REQUEST_VIDEO_CAPTURE) && (paramInt2 == -1)) {}
    for (;;)
    {
      try
      {
        Object localObject = paramIntent.getData();
        this.mVideoView.setVideoURI((Uri)localObject);
        localObject = new Intent("android.intent.action.MAIN");
        ((Intent)localObject).addCategory("android.intent.category.HOME");
        ((Intent)localObject).setFlags(268435456);
        startActivity((Intent)localObject);
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        continue;
      }
     /* Intent localIntent = new Intent("android.intent.action.MAIN");
      localIntent.addCategory("android.intent.category.HOME");
      localIntent.setFlags(268435456);
      startActivity(localIntent);*/
    }
  }
  
  public void onBackPressed()
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.HOME");
    localIntent.setFlags(268435456);
    startActivity(localIntent);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    dispatchTakeVideoIntent();
  }
}
