package com.hikeathon.maraudersmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Share
  extends Activity
{
  protected void onCreate(final Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    final EditText edText = (EditText)findViewById(R.id.et_to);
    final TextView localTextView = (TextView)findViewById(R.id.tv_msg_fixed);
    final EditText localEditText = (EditText)findViewById(R.id.et_user_msg);
    Button localButton1 = (Button)findViewById(R.id.btn_sms);
    Button localButton2 = (Button)findViewById(R.id.btn_back);
    final AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
    localAlertDialog.requestWindowFeature(1);
    localAlertDialog.setMessage("SHare This App Details With Ur Friends!!!!!\nWomenSafety..Be Confident,Be Safe!!!");
    localAlertDialog.setButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        localAlertDialog.cancel();
      }
    });
    localAlertDialog.show();
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
    	  String text=null;
        text = localTextView.getText().toString() + " \n " + localEditText.getText().toString();
        
        String str = edText.getText().toString();
        if ((str.equals(null)) || (str.equals("")))
        {
          Toast.makeText(Share.this.getApplicationContext(), "No Number Entered!!!!", 1).show();
          return;
        }
        SmsManager.getDefault().sendTextMessage(str, null, text, null, null);
        Toast.makeText(Share.this.getApplicationContext(), "SMS Sent!", 1).show();
        Intent newint = new Intent(Share.this, MainActivity.class);
        Share.this.startActivity(newint);
      }
    });
    localButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent intent = new Intent(Share.this, MainActivity.class);
        Share.this.startActivity(intent);
      }
    });
  }
}
