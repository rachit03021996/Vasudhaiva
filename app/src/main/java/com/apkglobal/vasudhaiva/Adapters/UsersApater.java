package com.apkglobal.vasudhaiva.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apkglobal.vasudhaiva.ChatdetailActivity;
import com.apkglobal.vasudhaiva.Models.Users;
import com.apkglobal.vasudhaiva.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UsersApater extends RecyclerView.Adapter<UsersApater.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersApater(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsersApater.ViewHolder holder, int position) {

        Users users=list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_person).into(holder.image);
        holder.userName.setText(users.getUsername());

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserid())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                     if (snapshot.hasChildren()){
                         for(DataSnapshot snapshot1: snapshot.getChildren()){
                             holder.lastmessage.setText(snapshot1.child("message")
                             .getValue().toString());
                         }
                     }
                    }

                    @Override

                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                                                        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatdetailActivity.class);

                intent.putExtra("userid",users.getUserid());
                intent.putExtra("profilepic",users.getProfilepic());
                intent.putExtra("username",users.getUsername());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName,lastmessage;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.username_list);
            lastmessage=itemView.findViewById(R.id.lastmessage);
        }
    }
}
