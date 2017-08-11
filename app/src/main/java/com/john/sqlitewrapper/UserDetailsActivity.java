package com.john.sqlitewrapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.john.sqlitewrapper.models.User;
import com.john.sqlitewrapper.provider.DataManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailsActivity extends AppCompatActivity
{
    Toolbar toolbar;

    @BindView(R.id.txtFirstName)
    EditText txtFirstName;

    @BindView(R.id.txtLastName)
    EditText txtLastName;

    @BindView(R.id.txtEmail)
    EditText txtEmail;

    @BindView(R.id.txtPassword)
    EditText txtPassword;

    @BindView(R.id.btnSave)
    Button btnSave;

    User user;
    MenuItem btnDelete;

    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_fields);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnSave = (Button) findViewById(R.id.btnSave);
        Bundle arg = getIntent().getExtras();
        if(arg != null)
            init(arg);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupUI();

    }


    private void init(Bundle b)
    {
        if(b.containsKey("user"))
        {
            user = (User) b.getSerializable("user");
        }
    }

    private void setupUI()
    {
        if(user != null)//initially save user
        {
            loadData();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager manager = new DataManager(UserDetailsActivity.this);
                if(user == null) {
                    manager.addUser(new User(txtFirstName.getText().toString(),
                            txtLastName.getText().toString(),
                            txtEmail.getText().toString(),
                            txtPassword.getText().toString()));
                }
                else
                {
                    manager.updateUser(new User(txtFirstName.getText().toString(),
                            txtLastName.getText().toString(),
                            txtEmail.getText().toString(),
                            txtPassword.getText().toString()));
                }

                Intent i = new Intent(UserDetailsActivity.this, UserListActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadData()
    {
        txtFirstName.setText(user.firstName);
        txtLastName.setText(user.lastName);
        txtEmail.setText(user.email);
        txtPassword.setText(user.password);
    }


}
