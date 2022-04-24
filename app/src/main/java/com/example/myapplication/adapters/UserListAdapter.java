package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.UserModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private ArrayList<UserModel> usersList = new ArrayList<>();

    public UserListAdapter(ArrayList<UserModel> arrayList) {
        usersList = arrayList;
    }



    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(usersList.get(position).getName());
        holder.msg.setText(usersList.get(position).getMsg());


    }

    public void setList(ArrayList<UserModel> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView msg;
        CircleImageView image;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name =(TextView) itemView.findViewById(R.id.name);
            this.msg = (TextView) itemView.findViewById(R.id.msg);
            this.image = (CircleImageView) itemView.findViewById(R.id.profile_image);
        }
    }
}
