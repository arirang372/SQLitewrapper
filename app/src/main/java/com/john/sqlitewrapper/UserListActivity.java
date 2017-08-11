package com.john.sqlitewrapper;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.john.sqlitewrapper.models.User;
import com.john.sqlitewrapper.provider.DataManager;
import java.util.List;


public class UserListActivity extends AppCompatActivity 
{
    Toolbar toolbar;
    ListView lv_users;
    TextView txtNoUsers;
    android.support.design.widget.FloatingActionButton btnAddUser;

    DataManager manager;
    private List<User> users;

    MenuItem btnDelete;

    private ActionBar ab;

    private User selected;
    private int positionTobeDeleted = -1;

    UserListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_users = (ListView) findViewById(R.id.lv_users);
        txtNoUsers = (TextView) findViewById(R.id.txtNoUsers);

        btnAddUser = (android.support.design.widget.FloatingActionButton) findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserListActivity.this, UserDetailsActivity.class);
                startActivity(i);
            }
        });

        manager = new DataManager(this);
        setupToolBar();
        setupUI();
    }

    private void setupToolBar()
    {
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        btnDelete = menu.findItem(R.id.btnDelete);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.btnDelete:
                manager.removeUser(selected);
                if(!users.isEmpty())
                    users.remove(positionTobeDeleted);

                adapter = new UserListViewAdapter(this, users);
                lv_users.setAdapter(adapter);

                resetActionBar();
                break;
            case android.R.id.home:
                //reinstate everything...
                resetActionBar();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void resetActionBar()
    {
        ab.setDisplayHomeAsUpEnabled(false);
        btnDelete.setVisible(false);
        adapter.setSelectedPosition(-1);
    }

    private void setupUI()
    {
        users = manager.getAllUsers();

        if(users.isEmpty())
        {
            lv_users.setVisibility(View.GONE);
            txtNoUsers.setVisibility(View.VISIBLE);
        }
        else
        {
            lv_users.setVisibility(View.VISIBLE);
            txtNoUsers.setVisibility(View.GONE);
            adapter = new UserListViewAdapter(this, users);
            adapter.setSelectedPosition(-1);
            lv_users.setAdapter(adapter);
        }

        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                User user = users.get(position);
                if(user != null)
                {
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    Intent i = new Intent(UserListActivity.this, UserDetailsActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

        lv_users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                adapter.setSelectedPosition(position);
                btnDelete.setVisible(true);
                ab.setDisplayHomeAsUpEnabled(true);
                selected = users.get(position);
                positionTobeDeleted = position;
                return true;
            }
        });
    }

    @Override
    public void onDestroy()
    {

        super.onDestroy();
    }


}
