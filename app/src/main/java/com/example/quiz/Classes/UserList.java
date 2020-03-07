package com.example.quiz.Classes;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.R;

import java.util.List;

public class UserList extends ArrayAdapter<User1> {

    private Activity context;
    //list of users
    List<User1>Users;

    public UserList(Activity context, List<User1> Users) {
        super(context, R.layout.layout_user_list, Users);
        this.context = context;
        this.Users = Users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);
        //initialize
        TextView textViewRollno = (TextView) listViewItem.findViewById(R.id.ViewRollno);
        TextView textviewName= (TextView) listViewItem.findViewById(R.id.ViewName);
        TextView textviewnEmail= (TextView) listViewItem.findViewById(R.id.ViewEmail);
        TextView textviewPhone= (TextView) listViewItem.findViewById(R.id.ViewPhone);
        TextView textviewnPassword= (TextView) listViewItem.findViewById(R.id.ViewPassword);

        //getting user at position
        User1 user = Users.get(position);
        //set user name
        textViewRollno.setText(user.getUserid());
        Toast.makeText(context, "val"+user.getUserid(), Toast.LENGTH_SHORT).show();
        //set user email
        textviewName.setText(user.getUsername());
        //set user mobilenumber
        textviewnEmail.setText(user.getUseremail());

        //set user phone
        textviewPhone.setText(user.getUsermobileno());
        //set user password
        textviewnPassword.setText(user.getPassword());

        return listViewItem;
    }
}