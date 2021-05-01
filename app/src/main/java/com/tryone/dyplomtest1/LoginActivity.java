package com.tryone.dyplomtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fUser = mAuth.getCurrentUser();
        if (fUser != null) {
            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Выполните вход или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRegister(View view) {
        /*if (!TextUtils.isEmpty(etLoginEmail.getText().toString()) && !TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(etLoginEmail.getText().toString(),etLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Регистрация выполнена успешно", Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginActivity.this, "Reg Error: ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();*/
        Intent i =new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void onClickSignUp(View view) {
        if (!TextUtils.isEmpty(etLoginEmail.getText().toString()) && !TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            mAuth.signInWithEmailAndPassword(etLoginEmail.getText().toString(),etLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else{
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
        }
    }
}