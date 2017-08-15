# SQLitewrapper

SQLitewrapper would help you to be able to store the field of the data in the form of Plain Object Jave Object(POJO). 
You don't have to generate the boilerplate data access layer by using this wrapper. You don't have to keep writing the 
same ContentProvider over again.

```java

    /**
     *   This is the sql script to create the table User
     *
     */

    public static final String CREATE_USER_TABLE_STATEMENT =   " CREATE TABLE " + TABLE_USER +
            " (id            INTEGER    PRIMARY KEY AUTOINCREMENT, " +
            " firstName      TEXT       NOT NULL, " +
            " lastName       TEXT       NOT NULL, " +
            " email          TEXT       NOT NULL, " +
            " password       TEXT       NOT NULL);";

/**
*   User class must inherit the `BaseModel` class from the library.
* 
*/

public class User extends BaseModel implements Serializable
{
    public transient Uri CONTENT_URI = ProviderMetadata.CONTENT_URI_USER;

    /**
     *   make sure to name this field ALL_Columns and inside this array, include all of the fields that you need for this object...
     *
     */
    public String [] ALL_Columns = {"id", "firstName", "lastName", "email", "password"};

    /**
     *   Each field id, firstName,... needs to be 'public' field so that the wrapper should be able to see it...
     *   If you don't want one of these fields to be visible by the wrapper, make it private.
     */

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

```

Once you set up the basics, you need to implement the interface called `IProviderAccessHelper` for the SQLitewrapper to 
get the content_uri to point to the right table like below.

```java

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

```

Once the ProviderHelper method is complete, you can start storing the real data to the SQLite database in your app like below.

```java
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

```



