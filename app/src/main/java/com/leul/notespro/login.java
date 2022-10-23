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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText emailtext,passwordtext;
    Button loginbtn;
    ProgressBar progressBar;
    TextView createtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailtext=findViewById(R.id.email_text);
        passwordtext=findViewById(R.id.password_text);
        loginbtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        createtext=findViewById(R.id.moveto_create_text);

        loginbtn.setOnClickListener((v)-> loginuser() );
        createtext.setOnClickListener((v)->startActivity(new Intent(login.this,Signup.class)));
    }
    void loginuser(){
        String email=emailtext.getText().toString();
        String password=passwordtext.getText().toString();

        Boolean isValidated=validateDate(email,password);

        if(!isValidated){
            return;
        }
        logininFirebase(email,password);
    }
    void logininFirebase(String email, String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInprogress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInprogress(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(login.this,MainActivity.class));
                        finish();
                    }else{
                    Utility.showToast(login.this,"Email is not verified, please verify your email first");
                    }
                }else{
                    Utility.showToast(login.this,task.getException().getLocalizedMessage());

                }
            }
        });
    }

    void changeInprogress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateDate(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtext.setError("Email is invalid");
            return false;
        }
        if(password.length()<4){
            passwordtext.setError("password length is invalid");
            return false;
        }
        if(password.isEmpty() || email.isEmpty()){
            return false;
        }
        return true;
    }
}