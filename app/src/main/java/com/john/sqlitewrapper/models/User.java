package com.john.sqlitewrapper.models;

import android.net.Uri;
import com.john.dbwrapperlib.BaseModel;
import com.john.sqlitewrapper.constants.ProviderMetadata;

import java.io.Serializable;

/**
 * Created by johns on 8/10/2017.
 */

public class User extends BaseModel implements Serializable
{
    public transient Uri CONTENT_URI = ProviderMetadata.CONTENT_URI_USER;

    public String [] ALL_Columns = {"id", "firstName", "lastName", "email", "password"};

    public int id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;


    public User()
    {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
    }


    public User( String first, String last, String email, String password)
    {
        super();
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String first)
    {
        this.firstName = first;
    }

    public void setLastName(String last)
    {
        this.lastName = last;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }


}
