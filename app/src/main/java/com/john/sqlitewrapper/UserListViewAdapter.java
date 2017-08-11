package com.john.sqlitewrapper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.john.sqlitewrapper.models.User;
import java.util.List;
import butterknife.ButterKnife;

/**
 * Created by johns on 8/10/2017.
 */

public class UserListViewAdapter extends ArrayAdapter<User> implements ListAdapter
{
    private int selectedPos = -1;
    public UserListViewAdapter(Context ctx, List<User> users)
    {
        super(ctx, R.layout.single_user_lv_item, users);
    }

    public void setSelectedPosition(int pos)
    {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        ViewHolder holder;
        if(v == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.single_user_lv_item, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }


        holder.viewMain.setBackgroundResource( (selectedPos == position)?
                R.color.selected : R.color.color_white);

        User user = getItem(position);
        if(user != null)
        {
            holder.txtFirstName.setText("First name : " + user.firstName);
            holder.txtLastName.setText("Last name : " + user.lastName);
        }

        return v;
    }


    static class ViewHolder
    {
        LinearLayout viewMain;
        TextView txtFirstName;
        TextView txtLastName;

        public ViewHolder(View v)
        {
            txtFirstName = v.findViewById(R.id.txtFirstName);
            txtLastName = v.findViewById(R.id.txtLastName);
            viewMain = v.findViewById(R.id.viewMain);
        }
    }
}
