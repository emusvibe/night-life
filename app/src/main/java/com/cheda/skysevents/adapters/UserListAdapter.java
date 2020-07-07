package com.cheda.skysevents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheda.skysevents.R;
import com.cheda.skysevents.model.User;
import com.cheda.skysevents.service.UserService;

import java.util.List;


/**
 * Created by Varavut on 8/23/2014.
 */
public class UserListAdapter extends BaseAdapter {

    Context mContext;
    List<User> mItems;
    public UserListAdapter(Context context, List<User> users)
    {
        mContext = context;
        mItems = users;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public User getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_user,null);
        }
        User user = getItem(i);
        ImageView profileImageView = (ImageView)view.findViewById(R.id.imageview_profile);
        TextView usernameTextView = (TextView)view.findViewById(R.id.textview_username);

        profileImageView.setImageBitmap(UserService.getInstance(mContext).getProfileImage(user));
        usernameTextView.setText(user.getUsername());
        return view;
    }
}
