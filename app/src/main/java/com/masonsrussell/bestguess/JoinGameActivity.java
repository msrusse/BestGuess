package com.masonsrussell.bestguess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoinGameActivity extends AppCompatActivity
{
	Button joinGameButton;
	EditText gameID;
	String gameToJoin;
	TextView returnHomeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game);
		setTitle("Join Game");
		loadActivity();
	}

	private void loadActivity()
	{
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

			}
		});
	}
}
