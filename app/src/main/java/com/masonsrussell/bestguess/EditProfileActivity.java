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
import android.text.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class EditProfileActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	TextView currentUsername, currentEmail, returnHomeButton;
	EditText editUsername, editEmail, editPassword, editReenterPassword;
	Button updateAccountButton;
	String usernameString, emailString, updatedUsername, updatedEmail, updatedPassword;
	int matchingPasswords;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		setTitle("Edit Profile");
		onLoad();
	}

	void onLoad()
	{
		currentUsername = findViewById(R.id.currentUsername);
		currentEmail = findViewById(R.id.currentEmail);
		editUsername = findViewById(R.id.input_name);
		editEmail = findViewById(R.id.input_email);
		editPassword = findViewById(R.id.input_password);
		editReenterPassword = findViewById(R.id.input_reEnterPassword);
		updateAccountButton = findViewById(R.id.btn_signup);
		returnHomeButton = findViewById(R.id.btn_return_home);
		editUsername.setText("");
		editPassword.setText("");
		editEmail.setText("");
		editReenterPassword.setText("");
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		usernameString = user.getDisplayName();
		emailString = user.getEmail();
		String usernamePrompt = "Current Username: " + usernameString;
		String emailPrompt = "Current Email: " + emailString;
		currentUsername.setText(usernamePrompt);
		currentEmail.setText(emailPrompt);
		returnHomeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		updateAccountButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!TextUtils.isEmpty(editPassword.getText()) && !TextUtils.isEmpty(editReenterPassword.getText()) && editPassword.getText().toString().equals(editReenterPassword.getText().toString()))
				{
					matchingPasswords = 1;
				} else if (!TextUtils.isEmpty(editPassword.getText()) && !TextUtils.isEmpty(editReenterPassword.getText()) && !editPassword.getText().toString().equals(editReenterPassword.getText().toString()))
				{
					matchingPasswords = -1;
				} else
				{
					matchingPasswords = 0;
				}
				if (!TextUtils.isEmpty(editUsername.getText()) && !TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 1)
				{
					updatedUsername = editUsername.getText().toString();
					updatedEmail = editEmail.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(1);
					onLoad();
				} else if (TextUtils.isEmpty(editUsername.getText()) && !TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 1)
				{
					updatedEmail = editEmail.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(2);
					onLoad();
				} else if (TextUtils.isEmpty(editUsername.getText()) && TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 1)
				{
					updatedPassword = editPassword.getText().toString();
					updateProfile(3);
					onLoad();
				} else if (!TextUtils.isEmpty(editUsername.getText()) && TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 1)
				{
					updatedUsername = editUsername.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(4);
					onLoad();
				} else if (!TextUtils.isEmpty(editUsername.getText()) && !TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 0)
				{
					updatedUsername = editUsername.getText().toString();
					updatedEmail = editEmail.getText().toString();
					updateProfile(5);
					onLoad();
				} else if (!TextUtils.isEmpty(editUsername.getText()) && TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 0)
				{
					updatedUsername = editUsername.getText().toString();
					updateProfile(6);
					onLoad();
				} else if (TextUtils.isEmpty(editUsername.getText()) && !TextUtils.isEmpty(editEmail.getText()) && matchingPasswords == 0)
				{
					updatedEmail = editEmail.getText().toString();
					updateProfile(7);
					onLoad();
				}
				else
				{
					Toast.makeText(EditProfileActivity.this, "Opps, that shouldn't have happened", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	void updateProfile(int option)
	{
		FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();

		switch (option)
		{
			case 1:
				newUser.updateEmail(updatedEmail);
				newUser.updatePassword(updatedPassword);
				Toast.makeText(this, "Email Update Succesful. This may take a few minutes to process.", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				newUser.updateEmail(updatedEmail);
				newUser.updatePassword(updatedPassword);
				Toast.makeText(this, "Email Update Succesful. This may take a few minutes to process.", Toast.LENGTH_SHORT).show();
				onLoad();
				break;
			case 3:
				newUser.updatePassword(updatedPassword);
				onLoad();
				break;
			case 4:
				newUser.updateEmail(updatedEmail);
				Toast.makeText(this, "Email Update Succesful. This may take a few minutes to process.", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				newUser.updateEmail(updatedEmail);
				Toast.makeText(this, "Email Update Succesful. This may take a few minutes to process.", Toast.LENGTH_SHORT).show();
				newUser.updatePassword(updatedPassword);
				break;
			case 7:
				newUser.updateEmail(updatedEmail);
				Toast.makeText(this, "Email Update Succesful. This may take a few minutes to process.", Toast.LENGTH_SHORT).show();
				onLoad();
				break;
		}
		if (option == 1 || option == 4 || option == 5 || option == 6)
		{
			UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
					.setDisplayName(updatedUsername)
					.build();

			newUser.updateProfile(profileUpdates)
					.addOnCompleteListener(new OnCompleteListener<Void>()
					{
						@Override
						public void onComplete(@NonNull Task<Void> task)
						{
							if (task.isSuccessful())
							{
								Log.d(CreateAccountActivity.TAG, "User profile updated.");
								onLoad();
							}
						}
					});
		}
	}
}
