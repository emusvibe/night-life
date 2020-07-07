package com.cheda.skysevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cheda.skysevents.adapters.UserListAdapter;
import com.cheda.skysevents.model.User;
import com.cheda.skysevents.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    List<User> users = new ArrayList<User>();
    UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        adapter = new UserListAdapter(this, users);

        UserService.getInstance(this).getUserList(getUserListListener);

        ListView userListView = (ListView) findViewById(android.R.id.list);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(UserListActivity.this,AccountActivity.class);
            intent.putExtra("user",adapter.getItem(i));
            startActivity(intent);
        }
    };

    UserService.GetUserListListener getUserListListener = new UserService.GetUserListListener() {
        @Override
        public void onResponce(boolean success, String message, List<User> userList) {
            if(success)
            {
                users.clear();
                adapter.notifyDataSetChanged();
                users.addAll(userList);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
