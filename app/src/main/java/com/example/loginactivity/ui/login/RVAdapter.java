package com.example.loginactivity.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.GetItem;
import com.example.loginactivity.R;
import com.example.loginactivity.ViewItem;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    Context context;
    ArrayList<GetItem> userArrayList;

    public RVAdapter(Context context, ArrayList<GetItem> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetItem user = userArrayList.get(position);

        holder.iName.setText(user.getItemName());
        holder.iLocation.setText(user.getItemLocation());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewItem.class);
                intent.putExtra("name", user.getItemName());
                intent.putExtra("user", user.getUserEmail());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView iName, iLocation;
        ConstraintLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iName = itemView.findViewById(R.id.item_name);
            iLocation = itemView.findViewById(R.id.item_location);
            item = itemView.findViewById(R.id.constraint_layout);
        }

    }
}
