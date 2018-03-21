package com.masonsrussell.bestguess;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	TextView currentUsername, currentEmail;
	EditText editUsername, editEmail, editPassword, editReenterPassword;
	Button updateAccountButton;
	String usernameString, emailString, updatedUsername, updatedEmail, updatedPassword;
	int matchingPasswords;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
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
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		usernameString = user.getDisplayName();
		emailString = user.getEmail();
		String usernamePrompt = "Current Username: " + usernameString;
		String emailPrompt = "Current Email: " + emailString;
		currentUsername.setText(usernamePrompt);
		currentEmail.setText(emailPrompt);
		updateAccountButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (editPassword != null && editReenterPassword != null && editPassword.getText().toString().equals(editReenterPassword.getText().toString()))
				{
					matchingPasswords = 1;
				} else if (editPassword != null && editReenterPassword != null && !editPassword.getText().toString().equals(editReenterPassword.getText().toString()))
				{
					matchingPasswords = -1;
					Toast.makeText(EditProfileActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
				} else
				{
					matchingPasswords = 0;
				}
				if (editUsername != null && editEmail != null && matchingPasswords == 1)
				{
					updatedUsername = editUsername.getText().toString();
					updatedEmail = editEmail.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(1);
				} else if (editUsername == null && editEmail != null && matchingPasswords == 1)
				{
					updatedEmail = editEmail.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(2);
				} else if (editUsername.getText() == null && editEmail.getText() == null && matchingPasswords == 1)
				{
					updatedPassword = editPassword.getText().toString();
					updateProfile(3);
				} else if (editUsername.getText() != null && editEmail.getText() == null && matchingPasswords == 1)
				{
					updatedUsername = editUsername.getText().toString();
					updatedPassword = editPassword.getText().toString();
					updateProfile(4);
				} else if (editUsername.getText() != null && editEmail.getText() != null && matchingPasswords == 0)
				{
					updatedUsername = editUsername.getText().toString();
					updatedEmail = editEmail.getText().toString();
					updateProfile(5);
				} else if (editUsername.getText() != null && editEmail.getText() == null && matchingPasswords == 0)
				{
					updatedUsername = editUsername.getText().toString();
					updateProfile(6);
				} else if (editUsername.getText() == null && editEmail.getText() != null && matchingPasswords == 0)
				{
					updatedEmail = editEmail.getText().toString();
					updateProfile(7);
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
				break;
			case 2:
				newUser.updateEmail(updatedEmail);
				newUser.updatePassword(updatedPassword);
				break;
			case 3:
				newUser.updatePassword(updatedPassword);
				break;
			case 4:
				newUser.updateEmail(updatedEmail);
				break;
			case 5:
				newUser.updateEmail(updatedEmail);
				newUser.updatePassword(updatedPassword);
				break;
			case 7:
				newUser.updateEmail(updatedEmail);
				break;
		}
		if (option == 1 || option == 2 || option == 3 || option == 4)
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
							}
						}
					});
		}
	}
}
