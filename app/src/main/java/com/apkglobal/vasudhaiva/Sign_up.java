package com.apkglobal.vasudhaiva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apkglobal.vasudhaiva.Models.Users;
import com.apkglobal.vasudhaiva.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(Sign_up.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are Creating your account");


        binding.btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.emailSignup.getText().toString(),
                        binding.passwordSignup.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                       progressDialog.dismiss();

                        if(task.isSuccessful()){

                             Users user= new Users(binding.nameSignup.getText().toString(),
                                    binding.emailSignup.getText().toString(),
                                    binding.passwordSignup.getText().toString());

                            String id= task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                         Toast.makeText(Sign_up.this, "User Created Succesfully",
                                 Toast.LENGTH_LONG).show();
                        }
                     else{
                         Toast.makeText(Sign_up.this,task.getException().getMessage(),
                                 Toast.LENGTH_LONG).show();
                     }

                    }
                });
            }
        });
        binding.already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_up.this,Sign_in.class);
                startActivity(intent);
            }
        });
    }
}