

package com.john.dbwrapperlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by john on 11/2/2015.
 */
public class ProviderAccess
{
    private Context context;
    private IProviderAccessHelper helper;

    public ProviderAccess(Context ctx, IProviderAccessHelper helper)
    {
        context = ctx;
        this.helper = helper;
    }

    public long insert(BaseModel model)
    {
        long inserted_id = 0;
        ContentValues values = new ContentValues();
        try
        {
            Field[] fields = model.getClass().getFields();
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length; i++)
            {
                String key = fields[i].getName();
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if (fields[i].getName().contains("CONTENT_URI"))
                        content_uri = (Uri) fieldValue;
                    else if(fields[i].getName().contains("ALL_Columns"))
                    {

                    }
                    else
                    {
                        if (!fields[i].getType().equals(String.class))
                        {
                            values.put(key, String.valueOf(fieldValue));
                        } else
                            values.put(key, (String) fieldValue);
                    }
                }
            }

            Uri uri = context.getContentResolver().insert(content_uri, values);
            inserted_id = Long.parseLong(uri.getLastPathSegment());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return inserted_id;
    }

    public boolean update(BaseModel model)
    {
        boolean updated = false;
        ContentValues values = new ContentValues();

        try
        {
            Field[] fields = model.getClass().getFields();
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri_address = helper.getUri();

            for (int i = 0; i < fields.length; i++)
            {
                String key = fields[i].getName();
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(!fields[i].getType().equals(String.class))
                    {
                        values.put(key, String.valueOf(fieldValue));
                    }
                    else
                        values.put(key, (String) fieldValue);
                }
            }
            values.remove("serialVersionUID");
            values.remove("PARCELABLE_WRITE_RETURN_VALUE");
            values.remove("CREATOR");
            values.remove("CONTENTS_FILE_DESCRIPTOR");
            values.remove("CONTENT_URI");
            values.remove("ALL_Columns");
            updated = context.getContentResolver().update(content_uri_address, values, "id=" + values.getAsString("id"), null) > 0;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return updated;
    }


    public BaseModel getByJSONExpression(BaseModel model,  JSONObject expression)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        BaseModel newModel = null;
        String [] columnNames;
        try
        {
            newModel =(BaseModel) className.newInstance();
            Map<String, Field> map = new HashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }

            Cursor cursor = context.getContentResolver().query(content_uri,
                                                                columnNames,
                                                                expression.toString(),
                                                                null,
                                                                null);

            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    //decryption needs to happen here..
                                    String valueReturned = cursor.getString(i);
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }
                    }
                    while(cursor.moveToNext());
                }
            }

            if(cursor != null && !cursor.isClosed())
                cursor.close();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return newModel;
    }


    public BaseModel getByColumnName(BaseModel model, String columnName, Object columnData)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        BaseModel newModel = null;
        String [] columnNames;
        try
        {
            newModel =(BaseModel) className.newInstance();
            Map<String, Field> map = new HashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }

            Cursor cursor = context.getContentResolver().query(content_uri,
                    columnNames,
                    columnName + "=" + columnData,
                    null,
                    null);

            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    //decryption needs to happen here..
                                    String valueReturned = cursor.getString(i);
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }
                    }
                    while(cursor.moveToNext());
                }
            }


            if(cursor != null && !cursor.isClosed())
                cursor.close();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return newModel;
    }


    public boolean delete(BaseModel model)
    {
        boolean result = false;
        try
        {
            Field[] fields = model.getClass().getFields();
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            int id = 0;
            for (int i = 0; i < fields.length; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().equalsIgnoreCase("id"))
                        id = (int) fieldValue;
                }
            }

            result =  context.getContentResolver().delete(content_uri, "id="+ id, null) > 0;

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     *
     *
     * @param model
     * @param expression - json expression {"id":"5","acc_no":"4"}
     *
     * @return
     */

    public List<? extends BaseModel> getAllByColumnName(BaseModel model, JSONObject expression)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        List<BaseModel> models = new ArrayList<>();

        String [] columnNames;
        try
        {

            Map<String, Field> map = new LinkedHashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length ; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }


            Cursor cursor = context.getContentResolver().query(content_uri, columnNames,
                    expression.toString(),
                    null,
                    null);


            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        BaseModel newModel =(BaseModel) className.newInstance();
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);

                                    String valueReturned = cursor.getString(i);
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }

                        models.add(newModel);
                    }
                    while(cursor.moveToNext());
                }

            }

            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        return models;
    }


    public List<? extends BaseModel> getAllByColumnName(BaseModel model, String columnName, String queryHelperString, Object content)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        List<BaseModel> models = new ArrayList<>();

        String [] columnNames;
        try
        {

            Map<String, Field> map = new LinkedHashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length ; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }


            Cursor cursor = context.getContentResolver().query(content_uri, columnNames,
                    columnName + queryHelperString + content,
                    null,
                    null);


            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        BaseModel newModel =(BaseModel) className.newInstance();
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);

                                    String valueReturned = cursor.getString(i);
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }

                        models.add(newModel);
                    }
                    while(cursor.moveToNext());
                }

            }

            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        return models;
    }


    public List<? extends BaseModel> getAllByColumnName(BaseModel model, String columnName, String queryHelperString, Object content, String sortOrder)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        List<BaseModel> models = new ArrayList<>();

        String [] columnNames;
        try
        {

            Map<String, Field> map = new LinkedHashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length ; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }


            Cursor cursor = context.getContentResolver().query(content_uri, columnNames,
                    columnName + queryHelperString + content,
                    null,
                    sortOrder);


            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        BaseModel newModel =(BaseModel) className.newInstance();
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);

                                    String valueReturned = cursor.getString(i);
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }

                        models.add(newModel);
                    }
                    while(cursor.moveToNext());
                }

            }

            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        return models;
    }

    public List<? extends BaseModel> getAll(BaseModel model)
    {
        //need to know the name of the columns in the content provider to get the values..
        Class<?> className = model.getClass();
        List<BaseModel> models = new ArrayList<>();

        String [] columnNames;
        try
        {

            Map<String, Field> map = new LinkedHashMap<>();
            Field[] fields = model.getClass().getFields();
            columnNames = new String[fields.length];
            helper.setContentUri(model.getClass().getSimpleName());
            Uri content_uri = helper.getUri();

            for (int i = 0; i < fields.length; i++)
            {
                Object fieldValue = fields[i].get(model);
                if (fieldValue != null)
                {
                    if(fields[i].getName().contains("ALL_Columns"))
                    {
                        columnNames = (String []) fieldValue;
                    }
                    else
                    {
                        map.put(fields[i].getName(), fields[i]);
                    }
                }
            }

            Cursor cursor = context.getContentResolver().query(content_uri, columnNames,
                    null,
                    null,
                    null);

            int columnLength = cursor.getColumnCount();

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        BaseModel newModel =(BaseModel) className.newInstance();
                        for(int i = 0; i < columnLength; i++)
                        {
                            String columnNameOnCursor = cursor.getColumnName(i);
                            try
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);
                                    value.setInt(newModel, cursor.getInt(i));
                                }
                            }
                            catch (Exception ex)
                            {
                                if (map.containsKey(columnNameOnCursor))
                                {
                                    Field value = map.get(columnNameOnCursor);

                                    //decryption needs to happen here..
                                    String valueReturned = cursor.getString(i);
                                    //decryption needs to happen here..
                                    value.set(newModel, valueReturned);
                                }
                            }
                        }

                        models.add(newModel);
                    }
                    while(cursor.moveToNext());
                }
            }


            if(cursor != null && !cursor.isClosed())
                cursor.close();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return models;
    }


}
