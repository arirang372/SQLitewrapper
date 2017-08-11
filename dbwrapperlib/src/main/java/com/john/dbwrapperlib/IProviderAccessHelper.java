package com.john.dbwrapperlib;

import android.net.Uri;

/**
 * Created by john on 11/29/2016.
 */

public interface IProviderAccessHelper
{
    void setContentUri(String className);
    Uri getUri();
}
