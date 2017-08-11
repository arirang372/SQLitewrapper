package com.john.sqlitewrapper.constants;

import android.net.Uri;

/**
 * Created by johns on 8/10/2017.
 */

public class ProviderMetadata {
    public static final String DATABASE_NAME = "MyUser";
    public static final String TABLE_USER = "user";

    public static final int DATABASE_VERSION = 1;

    public static final String PROVIDER_NAME = "com.john.sqlitewrapper.provider.UserProvider";

    public static final String URL_USER = "content://" + PROVIDER_NAME + "/user";

    public static final Uri CONTENT_URI_USER = Uri.parse(URL_USER);

    public static final String CREATE_USER_TABLE_STATEMENT =   " CREATE TABLE " + TABLE_USER +
            " (id            INTEGER    PRIMARY KEY AUTOINCREMENT, " +
            " firstName      TEXT       NOT NULL, " +
            " lastName       TEXT       NOT NULL, " +
            " email          TEXT       NOT NULL, " +
            " password       TEXT       NOT NULL);";

}
