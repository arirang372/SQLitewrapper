package com.john.sqlitewrapper.provider;


import com.john.dbwrapperlib.CustomContentProvider;
import com.john.sqlitewrapper.constants.ProviderMetadata;


/**
 * Created by john on 10/13/2015.
 */
public class UserProvider extends CustomContentProvider
{
    private static DatabaseHelper helper;
    @Override
    public boolean onCreate()
    {
        super.onCreate();
        helper = new DatabaseHelper(getContext(), ProviderMetadata.DATABASE_VERSION);
        db = helper.getWritableDatabase();

        return db != null;
    }

}
