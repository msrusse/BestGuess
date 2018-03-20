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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity
{
	private AdView mAdView;
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
		MobileAds.initialize(this, "ca-app-pub-7808223192356121~9980677209");
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-7808223192356121/5466717124");
		mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when when the user is about to return
				// to the app after tapping on an ad.
			}
		});
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
							Toast.makeText(LoginActivity.this, displayName, Toast.LENGTH_SHORT).show();
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
