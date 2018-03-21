package com.masonsrussell.bestguess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinGameActivity extends AppCompatActivity
{
	Button joinGameButton;
	EditText gameID;
	String gameToJoin;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game);
		loadActivity();
	}

	private void loadActivity()
	{
		joinGameButton = findViewById(R.id.joinGameButton);
		gameID = findViewById(R.id.gameIDEditText);
		joinGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				gameToJoin = gameID.getText().toString();
				setContentView(R.layout.activity_loading_screen);

				loadActivity();
			}
		});
	}
}
