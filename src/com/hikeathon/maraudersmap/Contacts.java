package com.hikeathon.maraudersmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import android.view.LayoutInflater;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Contacts
  extends Activity
{
  public final int PICK_CONTACT = 1;
  private String cNumber;
  DatabaseHandler db = new DatabaseHandler(this, "womensafety", null, 1);
  ListView lv;
  List<String> name = new ArrayList();
  List<String> num = new ArrayList();
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    
    if (paramInt2 == -1)
    {
    	Uri getdata=null;
    	getdata = paramIntent.getData();
      Cursor localCursor = managedQuery(getdata, null, null, null, null);
      if (localCursor.moveToFirst())
      {
        String str = localCursor.getString(localCursor.getColumnIndexOrThrow("_id"));
        if (localCursor.getString(localCursor.getColumnIndex("has_phone_number")).equalsIgnoreCase("1"))
        {
          localCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + str, null, null);
          localCursor.moveToFirst();
          this.cNumber = localCursor.getString(localCursor.getColumnIndex("data1"));
        }
      }
      Cursor c = getContentResolver().query(getdata, null, null, null, null);
      if (c.moveToFirst())
      {
        String ch = c.getString(c.getColumnIndex("display_name"));
        this.db.addContact(ch,this.cNumber);
        Toast.makeText(this, "Contact Added Successfully!!!", 1).show();
      }
    }
    openShowContact();
    switch (paramInt1)
    {
    default: 
      return;
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    ((TextView)findViewById(R.id.tv_heading)).setText("Emergency Contacts");
    Button bToAddContact = (Button)findViewById(R.id.btn_back_to_addContact);
    openShowContact();
    bToAddContact.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(Button bToAddContact)
      {
        Intent paramAnonymousView = new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI);
        Contacts.this.startActivityForResult(paramAnonymousView, 1);
      }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
    });
  }
  
  public void openShowContact()
  {
    getWindow().setSoftInputMode(3);
    this.lv = ((ListView)findViewById(2131230740));
    this.name = this.db.getAllName();
    this.num = this.db.getAllNumber();
    if (this.name.size() == 0)
    {
      Toast.makeText(this, "You Haven't Added Any Emergency Contact Yet!!!! \nClick on Add Button Top to Add Contacts!!!", 1).show();
      return;
    }
    this.lv.setAdapter(new Adapter());
  }
  
  class Adapter
    extends BaseAdapter
  {
    Adapter() {}
    
    public int getCount()
    {
      return Contacts.this.name.size();
    }
    
    public Object getItem(int paramInt)
    {
      return null;
    }
    
    public long getItemId(int paramInt)
    {
      return 0L;
    }
    
    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = Contacts.this.getLayoutInflater().inflate(R.layout.listview_content, paramViewGroup, false);
      TextView tvname = (TextView)paramView.findViewById(R.id.tv_name);
      TextView localTextView = (TextView)paramView.findViewById(R.id.tv_number);
      Button localButton = (Button)paramView.findViewById(R.id.btn_delete);
      tvname.setText((CharSequence)Contacts.this.name.get(paramInt));
      localTextView.setText((CharSequence)Contacts.this.num.get(paramInt));
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(Builder paramAnonymousView)
        {
          paramAnonymousView = new AlertDialog.Builder(Contacts.this);
          paramAnonymousView.setMessage("Are you sure you want to delete?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              Contacts.this.db.deleteMember((String)Contacts.this.name.get(this.hashCode()));
              Contacts.Adapter.this.notifyDataSetChanged();
              Toast.makeText(Contacts.this, "Contact Deleted!!!!", 1).show();
              Contacts.this.openShowContact();
            }
          }).setNegativeButton("No", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              paramAnonymous2DialogInterface.cancel();
            }
          });
           paramAnonymousView.create().show();
        }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
      });
      return paramView;
    }
  }
}
