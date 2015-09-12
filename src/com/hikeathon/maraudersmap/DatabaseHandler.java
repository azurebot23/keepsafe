package com.hikeathon.maraudersmap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler
  extends SQLiteOpenHelper
{
  private static final String ContactName = "name";
  private static final String ContactPhone = "phone";
  private static final String DATABASE_NAME = "womensafety";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_MEMBERS = "emergencycontacts";
  
  public DatabaseHandler(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
  {
    super(paramContext, "womensafety", null, 1);
  }
  
  public void addContact(String paramString1, String paramString2)
  {
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramString1);
    localContentValues.put("phone", paramString2);
    localSQLiteDatabase.insert("emergencycontacts", null, localContentValues);
    localSQLiteDatabase.close();
  }
  
  public void deleteMember(String paramString)
  {
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    localSQLiteDatabase.delete("emergencycontacts", "name = ?", new String[] { String.valueOf(paramString) });
    localSQLiteDatabase.close();
  }
  
  public List<String> getAllName()
  {
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = getWritableDatabase().rawQuery("SELECT name FROM emergencycontacts", null);
    if (localCursor.moveToFirst()) {
      do
      {
        localArrayList.add(localCursor.getString(localCursor.getColumnIndex("name")));
      } while (localCursor.moveToNext());
    }
    return localArrayList;
  }
  
  public List<String> getAllNumber()
  {
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = getWritableDatabase().rawQuery("SELECT phone FROM emergencycontacts", null);
    if (localCursor.moveToFirst()) {
      do
      {
        localArrayList.add(localCursor.getString(localCursor.getColumnIndex("phone")));
      } while (localCursor.moveToNext());
    }
    return localArrayList;
  }
  
  public int getContactsCount()
  {
    return getReadableDatabase().rawQuery("SELECT  * FROM emergencycontacts", null).getCount();
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE emergencycontacts(name TEXT,phone TEXT)");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    try
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS emergencycontacts");
      onCreate(paramSQLiteDatabase);
      return;
    }
    catch (SQLException paramSQLiteDatabaseEx)
    {
      Log.e("getting exception " + paramSQLiteDatabaseEx.getLocalizedMessage().toString(), null);
    }
  }
}

