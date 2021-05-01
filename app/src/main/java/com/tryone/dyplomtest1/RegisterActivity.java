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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tryone.dyplomtest1.constants.Constants;
import com.tryone.dyplomtest1.views.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText etRegName, etRegSecName, etRegEmail, etRegPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        etRegName=findViewById(R.id.etRegName);
        etRegSecName=findViewById(R.id.etRegSecName);
        etRegEmail=findViewById(R.id.etRegEmail);
        etRegPassword=findViewById(R.id.etRegPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference=FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY);
        mAuth=FirebaseAuth.getInstance();
    }

    private boolean isEmpty(){
        return (TextUtils.isEmpty(etRegName.getText().toString()) || TextUtils.isEmpty(etRegSecName.getText().toString()) || TextUtils.isEmpty(etRegEmail.getText().toString()) || TextUtils.isEmpty(etRegPassword.getText().toString()));
    }

    public void onClickRegister(View view) {
        if (!isEmpty()){
            mAuth.createUserWithEmailAndPassword(etRegEmail.getText().toString(),etRegPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Регистрация выполнена успешно", Toast.LENGTH_SHORT).show();
                        User user= new User(etRegName.getText().toString(),etRegSecName.getText().toString(),task.getResult().getUser().getUid(),etRegEmail.getText().toString());
                        databaseReference.child(task.getResult().getUser().getUid()).push().setValue(user);
                        Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Reg Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}