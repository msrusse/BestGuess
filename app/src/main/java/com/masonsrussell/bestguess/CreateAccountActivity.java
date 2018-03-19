package com.masonsrussell.bestguess;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	public FirebaseUser user;
	EditText emailText, passwordText, passwordVerifyText, usernameText;
	Button createAccountButton;
	TextView loginButton;
	private static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		mAuth = FirebaseAuth.getInstance();
		emailText = findViewById(R.id.input_email);
		passwordText = findViewById(R.id.input_password);
		usernameText = findViewById(R.id.input_name);
		passwordVerifyText = findViewById(R.id.input_reEnterPassword);
		createAccountButton = findViewById(R.id.btn_signup);
		loginButton = findViewById(R.id.link_login);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		createAccountButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				String email = emailText.getText().toString();
				String password = passwordText.getText().toString();
				String username = usernameText.getText().toString();
				String passwordVerify = passwordVerifyText.getText().toString();
				if (passwordVerify.equals(password))
				{
					createAccount(email, password);
				}
				else
				{
					Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void createAccount(String email,String password)
	{
		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "createUserWithEmail:success");
							user = mAuth.getCurrentUser();
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "createUserWithEmail:failure", task.getException());
							Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}

						// ...
					}
				});
		login(email, password);
	}

	void login(String email,String password)
	{
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithEmail:success");
							user = mAuth.getCurrentUser();
							Toast.makeText(CreateAccountActivity.this, String.valueOf(user), Toast.LENGTH_LONG).show();
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithEmail:failure", task.getException());
							Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}

						// ...
					}
				});
	}
}
