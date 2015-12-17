package com.plan_it.mobile.plan_it;

/**
 * Created by Kevin on 30-Nov-2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class FriendsListAdapter extends ArrayAdapter<FriendListModel> {
    ArrayList<FriendListModel> friendsList;
    Context context;
    int resource;
    int i = 0;
    FriendHolder friend;
    boolean isFromEditEvent;
    int eventID;
    View view;

    public FriendsListAdapter(Context context, int resource, ArrayList<FriendListModel> friendsList, int eventID, boolean isFromEditEvent)
    {
        super(context, resource, friendsList);
        this.eventID = eventID;
        this.isFromEditEvent = isFromEditEvent;
        this.context = context;
        this.resource = resource;
        this.friendsList = friendsList;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        this.view = view;
        View rowView = view;
        i++;
        if(rowView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            rowView = inflater.inflate(resource, parent, false);

            friend = new FriendHolder();

            friend.txtFriendID = (TextView) rowView.findViewById(R.id.friends_list_friend_id);
            friend.imgProfilePic = (ImageView) rowView.findViewById(R.id.friends_list_profile_image);
            friend.removeFriend = (ImageView)rowView.findViewById(R.id.friends_list_remove);
        }
        else
        {
            friend = (FriendHolder)rowView.getTag();
        }
        friend.txtFriendID.setTag(position);
        friend.txtFriendID.setText(friendsList.get(position).FriendID);
        friend.imgProfilePic.setImageResource(friendsList.get(position).ProfilePic);
        if(!isFromEditEvent) {

            friend.removeFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            context);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete record");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((FriendsListActivity) context).RemoveFriend(LoginActivity.userID, friendsList.get(position).UserID);
                            friendsList.remove(position);
                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                    notifyDataSetChanged();
                }
            });
        }
        else{
            friend.removeFriend.setImageResource(R.drawable.ic_add_circle_blue_24dp);
            friend.removeFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((FriendsListActivity) context).inviteFriend(friendsList.get(position).UserID, friendsList.get(position).FriendID);
                        friendsList.remove(position);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();
                }
            });

        }
        rowView.setTag(friend);
        return rowView;
    }
    static class FriendHolder
    {
        ImageView imgProfilePic;
        TextView txtFriendID;
        ImageView removeFriend;
    }
}
