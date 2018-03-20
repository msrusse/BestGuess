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

public class LoginActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	public FirebaseUser user;
	String email, password, displayName;
	EditText emailText, passwordText;
	Button loginButton;
	TextView createAccount;
	private static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mAuth = FirebaseAuth.getInstance();
		emailText = findViewById(R.id.input_email);
		passwordText = findViewById(R.id.input_password);
		loginButton = findViewById(R.id.btn_login);
		createAccount = findViewById(R.id.link_signup);
		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(),CreateAccountActivity.class);
				startActivity(intent);
				finish();
			}
		});
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				email = emailText.getText().toString();
				password = passwordText.getText().toString();
				login();
			}
		});
	}

	void login()
	{
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithEmail:success");
							user = mAuth.getCurrentUser();
							displayName = user.getDisplayName();
							Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
							startActivity(intent);
							finish();
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithEmail:failure", task.getException());
							Toast.makeText(LoginActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

}
