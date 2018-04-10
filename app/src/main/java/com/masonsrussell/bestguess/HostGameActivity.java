package com.masonsrussell.bestguess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Calendar;
import java.util.TimeZone;

public class HostGameActivity extends AppCompatActivity
{
	Button startGameButton;
	Spinner deckSelectionSpinner;
	TextView gameIDTextView, returnHomeButton;
	public String gameID;
	ArrayList<String> decks = new ArrayList<>();
	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host_game);
		setTitle("Host Game");
		onLoad();
	}

	void onLoad()
	{
		startGameButton = findViewById(R.id.startGameButton);
		deckSelectionSpinner = findViewById(R.id.chooseDeckSpinner);
		gameIDTextView = findViewById(R.id.gamePassword);
		returnHomeButton = findViewById(R.id.returnHomeButton);
		gameID = generateRandom();
		gameIDTextView.setText(gameID);
		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();

		try
		{
			mDatabase.child("decks").addListenerForSingleValueEvent(new ValueEventListener()
			{
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					HashMap<String, Object> text = (HashMap)dataSnapshot.getValue();
					decks.addAll(text.keySet());
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(HostGameActivity.this, R.layout.spinner_style, decks);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					deckSelectionSpinner.setAdapter(dataAdapter);
				}

				@Override
				public void onCancelled(DatabaseError databaseError)
				{
					Log.d(CreateAccountActivity.TAG, "Try something else");
				}
			});
		}
		catch (Exception ex)
		{
			Log.d(CreateAccountActivity.TAG, ex.getMessage());
		}

		returnHomeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
				startActivity(intent);
			}
		});

		startGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				createGame(deckSelectionSpinner.getSelectedItem().toString());
				Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
				intent.putExtra("GameID", gameID);
				startActivity(intent);
			}
		});
	}

	static String generateString(Random rng, String characters)
	{
		char[] text = new char[4];
		for (int i = 0; i < 4; i++)
		{
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	String generateRandom()
	{
		Random random = new Random();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		return generateString(random, chars);
	}

	private void createGame(String category)
	{
		DatabaseReference games = mDatabase.child("games");
		DatabaseReference game = games.child(gameID);
		DatabaseReference gameInfoRef = game.child("gameInfo");
		DatabaseReference playersRef = game.child("players").child(mAuth.getCurrentUser().getDisplayName());

		Map<String, Object> gameInfo = new HashMap<>();
		gameInfo.put("creator", mAuth.getCurrentUser().getUid());
		gameInfo.put("creatorName", mAuth.getCurrentUser().getDisplayName());
		gameInfo.put("category", category);
		gameInfo.put("currentQuestion", "");
		gameInfo.put("currentAnswer", "");
		gameInfo.put("startedAt", returnSeconds());
		gameInfo.put("isGameActive", false);
		gameInfoRef.updateChildren(gameInfo);

		Map<String, Object> playerInfo = new HashMap<>();
		playerInfo.put("answer", -1);
		playerInfo.put("score", 0);
		playersRef.updateChildren(playerInfo);
	}

	public long returnSeconds() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		calendar.set(2011, Calendar.OCTOBER, 1);
		return calendar.getTimeInMillis() / 1000L;
	}
}
