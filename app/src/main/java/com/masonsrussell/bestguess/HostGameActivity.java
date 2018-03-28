package com.masonsrussell.bestguess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class HostGameActivity extends AppCompatActivity
{
	Button startGameButton;
	Spinner deckSelectionSpinner;
	TextView gameIDTextView, returnHomeButton;
	String gameID;

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
				//Intent intent = new Intent(getApplicationContext(), );
				//startActivity(intent);
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
}
