package com.masonsrussell.bestguess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	TextView welcomeText, logoutButton;
	String username;
	Button joinGameButton, hostGameButton, editProfileButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		welcomeText = findViewById(R.id.welcomeText);
		joinGameButton = findViewById(R.id.playGameButton);
		hostGameButton = findViewById(R.id.hostGameButton);
		editProfileButton = findViewById(R.id.editUserButton);
		logoutButton = findViewById(R.id.logoutButton);
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		username = user.getDisplayName();
		String welcome = "Welcome, " + username;
		welcomeText.setText(welcome);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mAuth.signOut();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});
		joinGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
				startActivity(intent);
			}
		});
		hostGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), HostGameActivity.class);
				startActivity(intent);
			}
		});
		editProfileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
