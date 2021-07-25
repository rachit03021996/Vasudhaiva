package com.apkglobal.vasudhaiva.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apkglobal.vasudhaiva.Adapters.UsersApater;
import com.apkglobal.vasudhaiva.Models.Users;
import com.apkglobal.vasudhaiva.databinding.FragmentChatsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {



    public ChatsFragment() {
        // Required empty public constructor
    }
    FragmentChatsBinding binding;
    ArrayList<Users> list= new ArrayList<>();
    FirebaseDatabase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentChatsBinding.inflate(inflater,container,false);

        database=FirebaseDatabase.getInstance();

        UsersApater adapter=new UsersApater(list,getContext());
        binding.chatRecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        binding.chatRecyclerview.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                Users users=dataSnapshot.getValue(Users.class);
                users.setUserid(dataSnapshot.getKey());
                list.add(users);

            }
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return binding.getRoot();
}
}