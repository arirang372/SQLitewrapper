package com.john.sqlitewrapper.provider;

import android.net.Uri;

import com.john.dbwrapperlib.IProviderAccessHelper;
import com.john.sqlitewrapper.constants.ProviderMetadata;

/**
 * Created by john on 11/29/2016.
 */

public class ProviderHelper implements IProviderAccessHelper
{
    private Uri content_uri;

    @Override
    public void setContentUri(String className)
    {
        switch (className)
        {
            case "User"://Name of the Object 'User'
                content_uri = Uri.parse( ProviderMetadata.URL_USER);
                break;
        }
    }

    @Override
    public Uri getUri()
    {
        return content_uri;
    }
}
