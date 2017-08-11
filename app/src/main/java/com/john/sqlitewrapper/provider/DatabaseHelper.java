package com.john.sqlitewrapper.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.john.sqlitewrapper.constants.ProviderMetadata;

/**
 * Created by john on 3/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public DatabaseHelper(Context context, int databaseVersion)
    {
        super(context, ProviderMetadata.DATABASE_NAME, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ProviderMetadata.CREATE_USER_TABLE_STATEMENT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " +  ProviderMetadata.TABLE_USER);
        onCreate(db);
    }
}
