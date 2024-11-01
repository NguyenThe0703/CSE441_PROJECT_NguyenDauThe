package com.example.cse441_project.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.cse441_project.Login_Logout.IntroActivity;
import com.example.cse441_project.R;

public class HomeUser extends AppCompatActivity {
    private Button logOut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_user);
        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.remove("password");
            editor.apply();


            startActivity(new Intent(HomeUser.this, IntroActivity.class));
        });
    }
}