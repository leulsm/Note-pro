package com.leul.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

        EditText emailtext,passwordtext,nametext;
        Button creataccount;
        ProgressBar progressBar;
        TextView logintext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailtext=findViewById(R.id.email_text);
        passwordtext=findViewById(R.id.password_text);
        nametext=findViewById(R.id.name_text);
        creataccount=findViewById(R.id.creat_btn);
        progressBar=findViewById(R.id.progress_bar);
        logintext=findViewById(R.id.moveto_login_text);

        creataccount.setOnClickListener(v-> creatAccount());
        logintext.setOnClickListener((v)->startActivity(new Intent(Signup.this,login.class)));
    }
    void creatAccount(){
        String email=emailtext.getText().toString();
        String name=nametext.getText().toString();
        String password=passwordtext.getText().toString();

        Boolean isValidated=validateDate(name,email,password);

        if(!isValidated){
            return;
        }
        creataccountinFirebase(name,email,password);
    }

    void creataccountinFirebase(String name, String email, String password){
        changeInprogress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInprogress(false);
                if(task.isSuccessful()){
                    // creat account is done
                    Utility.showToast(Signup.this,"Successfully registered, check your email to varify");
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    // failure
                    Utility.showToast(Signup.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInprogress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            creataccount.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            creataccount.setVisibility(View.VISIBLE);
        }
    }

    boolean validateDate(String name,String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtext.setError("Email is invalid");
            return false;
        }
        if(password.length()<4){
            passwordtext.setError("password length is invalid");
            return false;
        }
        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            return false;
        }
        return true;
    }
}