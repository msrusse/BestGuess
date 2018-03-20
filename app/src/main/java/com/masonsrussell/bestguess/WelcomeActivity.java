package com.masonsrussell.bestguess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	TextView welcomeText;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		welcomeText = findViewById(R.id.welcomeText);
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		username = user.getDisplayName();
		String welcome = "Welcome, " + username;
		welcomeText.setText(welcome);
	}
}
