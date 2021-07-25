package com.apkglobal.vasudhaiva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apkglobal.vasudhaiva.Adapters.ChatAdapter;
import com.apkglobal.vasudhaiva.Fragments.ChatsFragment;
import com.apkglobal.vasudhaiva.Models.MessageModel;
import com.apkglobal.vasudhaiva.databinding.ActivityChatdetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ChatdetailActivity extends AppCompatActivity {
    ActivityChatdetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();

        auth=FirebaseAuth.getInstance();

       final String senderId=auth.getUid();
        String recieveId= getIntent().getStringExtra("userid");
        String userName=getIntent().getStringExtra("username");
        String profilePic=getIntent().getStringExtra("profilepic");

        binding.usernamechatdetail.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_person).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatdetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
     final ArrayList<MessageModel> messageModels=new ArrayList<MessageModel>();

     final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);
     binding.chatRecyclerview.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclerview.setLayoutManager(layoutManager);

        final String senderRoom=senderId + recieveId;
        final String receiverRoom= recieveId +senderId;

       database.getReference().child("chats")
               .child(senderRoom)
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                       messageModels.clear();
                       for(DataSnapshot snapshot1: snapshot.getChildren()){
                       MessageModel model=snapshot1.getValue(MessageModel.class);
                       messageModels.add(model);
                   }
                   chatAdapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                   }
               });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String message = binding.etMessage.getText().toString();
              final MessageModel model=new MessageModel(senderId,message);
              model.setTimestamp(new Date().getTime());
              binding.etMessage.setText("");

              database.getReference().child("chats").child(senderRoom).push().setValue(model).
                      addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
                          database.getReference().child("chats").child(receiverRoom)
                          .push()
                          .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {

                              }
                          });
                          }
                      });


            }
        });
    }
}