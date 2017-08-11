package com.john.sqlitewrapper.provider;

import android.content.Context;
import com.john.dbwrapperlib.ProviderAccess;
import com.john.sqlitewrapper.models.User;

import java.util.List;

/**
 * Created by johns on 8/10/2017.
 */

public class DataManager
{
    private ProviderAccess access;

    public DataManager(Context context)
    {
        access = new ProviderAccess(context, new ProviderHelper());
    }

    public List<User> getAllUsers()
    {
        return (List<User>) access.getAll(new User());
    }

    public void addUser(User user)
    {
        if(user != null)
        {
            access.insert(user);
        }
    }

    public void updateUser(User user)
    {
        if(user != null)
        {
            access.update(user);
        }
    }

    public void removeUser(User user)
    {
        if(user != null)
        {
            access.delete(user);
        }
    }

}
