package com.john.dbwrapperlib;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by john on 3/10/2016.
 */
public class CustomContentProvider extends ContentProvider
{
    protected SQLiteDatabase db;

    @Override
    public boolean onCreate()
    {
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Uri newUri = null;
        ContentResolver resolver = getContext().getContentResolver();

        long row;

        values.remove("id");
        values.remove("serialVersionUID");
        values.remove("PARCELABLE_WRITE_RETURN_VALUE");
        values.remove("CREATOR");
        values.remove("CONTENTS_FILE_DESCRIPTOR");
        values.remove("CONTENT_URI");
        values.remove("ALL_Columns");
        row = db.insertOrThrow( uri.toString().substring(uri.toString().lastIndexOf("/") + 1), "", values);
        if (row > 0)
        {
            newUri = ContentUris.withAppendedId(uri, row);
        }

        resolver.notifyChange(newUri, null, false);
        return newUri;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {

        String queryStatement = "";

        String tableName =  uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
        Cursor cursor = null;

        if(TextUtils.isEmpty(selection))
        {
            queryStatement = "SELECT * FROM " + tableName; //select all
            cursor = db.rawQuery(queryStatement , null);
        }
        else
        {
            //if selection expression is json string...
            try
            {
                queryStatement = "SELECT * FROM " + tableName + " WHERE ";
                JSONObject obj = new JSONObject(selection);

                List<Object> argumentValues = new ArrayList<>();
                List<String> columnNames = new ArrayList<>();

                int selectionArgumentSize = 0;
                boolean selected;
                for (int i = 0; i < projection.length; i++)
                {
                    String column = projection[i];
                    selected = false;
                    try
                    {
                        if (obj.has(column))
                        {
                            if (obj.getString(column).contains("^"))
                            {
                                queryStatement += column + "!=?";
                                String colVal = obj.getString(column);
                                colVal = colVal.replace("^", "");
                                argumentValues.add(colVal);
                            }
                            else if (obj.getString(column).contains("%"))
                            {
                                queryStatement += column + " LIKE ?";
                                String colVal = obj.getString(column);
                                argumentValues.add(colVal);

                                columnNames.add(column+" LIKE ?");
                            }
                            else
                            {
                                queryStatement += column + "=?";
                                argumentValues.add(obj.getString(column));

                                columnNames.add(column+" =?");
                            }

                            selectionArgumentSize++;
                            selected = true;
                        }
                    }
                    catch (Exception ex)
                    {
                        if (obj.has(column))
                        {
                            if (obj.getString(column).contains("^"))
                            {
                                queryStatement += column + "!=?";
                                String colVal = obj.getString(column);
                                colVal = colVal.replace("^", "");
                                argumentValues.add(colVal);
                            }
                            else if (obj.getString(column).contains("%"))
                            {
                                queryStatement += column + " LIKE ?";
                                String colVal = obj.getString(column);
                                argumentValues.add(colVal);
                                columnNames.add(column+" LIKE ?");
                            }
                            else
                            {
                                queryStatement += column + "=?";
                                argumentValues.add(obj.getString(column));
                            }

                            selectionArgumentSize++;
                            selected = true;
                        }

                    }

                    if (selected) {
                        queryStatement += " AND ";
                    }

                }

                String queryLocal = queryStatement;
                char[] split = queryLocal.toCharArray();
                for (int j = split.length - 1; j > 0; j--) {
                    char partial = queryStatement.charAt(j);
                    if (partial == ' ')
                        split[j] = '\0';
                    if (partial == 'D')
                        split[j] = '\0';
                    if (partial == 'N')
                        split[j] = '\0';
                    if (partial == 'A') {
                        split[j] = '\0';
                        break;
                    }
                }

                StringBuilder sb = new StringBuilder();

                for (char c : split) {
                    sb.append(c);
                }

                queryStatement = new String(sb.toString());

                if (!TextUtils.isEmpty(sortOrder))
                    queryStatement = queryStatement + " " + sortOrder;

                String[] selectionArguments = new String[selectionArgumentSize];
                for (int i = 0; i < selectionArguments.length; i++) {
                    try {
                        selectionArguments[i] = (String) argumentValues.get(i);
                    } catch (ClassCastException ex) {
                        selectionArguments[i] = String.valueOf((int) argumentValues.get(i));
                    }
                }

                if (queryStatement.contains("LIKE"))
                {
                    String likeStatement = "";

                    for(int i = 0; i < columnNames.size(); i++)
                    {
                        if(i != columnNames.size() - 1 )
                        {
                            likeStatement += columnNames.get(i) + " AND ";
                        }
                        else {
                            likeStatement += columnNames.get(i);
                        }
                    }

                    cursor = db.query(tableName,
                            projection,
                            likeStatement,
                            selectionArguments,
                            null,
                            null,
                            sortOrder);
                }
                else
                {
                    cursor = db.rawQuery(queryStatement, selectionArguments);
                }
            }
            catch (JSONException ex) {

                if (selection.contains("=")) {
                    queryStatement = "SELECT * FROM " + tableName + " WHERE " + selection.split("=")[0] + "=?"; //select with an argument...
                    cursor = db.rawQuery(queryStatement, new String[]{selection.split("=")[1]});
                } else if (selection.contains("LIKE")) {
                    StringTokenizer st = new StringTokenizer(selection, "LIKE");
                    String columnName = st.nextToken();
                    String contentName = st.nextToken();

                    if (!TextUtils.isEmpty(sortOrder)) {

                        cursor = db.query(tableName,
                                projection,
                                columnName + " LIKE ?",
                                new String[]{contentName},
                                null,
                                null,
                                sortOrder);

                    }
                    else
                    {
                        cursor = db.query(tableName,
                                projection,
                                columnName + " LIKE ?",
                                new String[]{contentName},
                                null,
                                null,
                                null);


                    }
                }
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if(values.containsKey("ALL_Columns"))
        {
            values.remove("ALL_Columns");
        }

        int count = db.update( uri.toString().substring(uri.toString().lastIndexOf("/") + 1), values, selection, selectionArgs);

        if(getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int count = db.delete(uri.toString().substring(uri.toString().lastIndexOf("/") + 1), selection, selectionArgs);

        if(getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
