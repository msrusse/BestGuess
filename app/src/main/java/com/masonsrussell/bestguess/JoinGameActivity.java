package com.masonsrussell.bestguess;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class JoinGameActivity extends AppCompatActivity
{
	Button joinGameButton;
	EditText gameID;
	String gameToJoin;
	TextView returnHomeButton;
	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;
	ArrayList<String> games = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		loadActivity();
	}

	private void loadActivity()
	{
		setContentView(R.layout.activity_join_game);
		setTitle("Join Game");
		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();
		joinGameButton = findViewById(R.id.joinGameButton);
		gameID = findViewById(R.id.gameIDEditText);
		returnHomeButton = findViewById(R.id.returnHomeButton);
		returnHomeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		joinGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				gameToJoin = gameID.getText().toString();
				setContentView(R.layout.activity_loading_screen);
				mDatabase.child("games").addValueEventListener(new ValueEventListener()
				{

					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						try
						{
							HashMap<String, Object> text = (HashMap) dataSnapshot.getValue();
							for (String key : text.keySet())
							{
								for (String x : games)
								{
									if (x.equals(key))
									{
										continue;
									}
									else
									{
										games.add(key);
									}
								}
							}
							checkForGame();
						}
						catch (Exception ex)
						{
							Toast.makeText(JoinGameActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError)
					{
						Log.d(CreateAccountActivity.TAG, "Try something else");
					}
				});
				reloadActivity();
			}
		});
	}

	private void reloadActivity()
	{
		Toast.makeText(getApplicationContext(),"Invalid Game ID", Toast.LENGTH_SHORT).show();
		setContentView(R.layout.activity_join_game);
		setTitle("Join Game");
		mDatabase = FirebaseDatabase.getInstance().getReference();
		joinGameButton = findViewById(R.id.joinGameButton);
		gameID = findViewById(R.id.gameIDEditText);
		returnHomeButton = findViewById(R.id.returnHomeButton);
		returnHomeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		joinGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				gameToJoin = gameID.getText().toString();
				setContentView(R.layout.activity_loading_screen);
				mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener()
				{

					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						try
						{
							HashMap<String, Object> text = (HashMap) dataSnapshot.getValue();
							for (String key : text.keySet())
							{
								games.add(key);
							}
							checkForGame();
						}
						catch (Exception ex)
						{
							Toast.makeText(JoinGameActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError)
					{
						Log.d(CreateAccountActivity.TAG, "Try something else");
					}
				});
				loadActivity();
			}
		});
	}

	private void checkForGame()
	{
		for(String x : games)
		{
			if (x.equalsIgnoreCase(gameToJoin))
			{
				gameExists(x);
			}
		}
	}

	private void gameExists(String game)
	{
		mDatabase.child("games").child(game).child("players").setValue(mAuth.getCurrentUser().getDisplayName());
		Intent intent = new Intent(getApplicationContext(), PlayGameActivity.class);
		startActivity(intent);
		finish();
	}
}
