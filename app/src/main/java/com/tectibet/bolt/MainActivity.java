package com.tectibet.bolt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
    }

    public void goToLogin(View view) {
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void goToSignUp(View view) {
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
    }
}
