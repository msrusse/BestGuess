package com.masonsrussell.bestguess;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlayGameActivity extends AppCompatActivity
{
	ProgressBar countdownTimer;
	TextView questionView;
	EditText answerInput;
	Button submitButton;
	CountDownTimer mCountDownTimer;
	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;
	double answer;
	int i=0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		getSupportActionBar().hide();
		onLoad();
	}

	void onLoad()
	{
		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();
		countdownTimer = findViewById(R.id.countdownTimer);
		submitButton = findViewById(R.id.submitButton);
		questionView = findViewById(R.id.displayQuestion);
		answerInput = findViewById(R.id.answerInput);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (TextUtils.isEmpty(answerInput.getText()))
				{
					Toast.makeText(PlayGameActivity.this, "No answer submitted", Toast.LENGTH_SHORT).show();
				}
				else
				{
					answer = Double.parseDouble(answerInput.getText().toString());
				}
				mDatabase.child("games").child(getIntent().getStringExtra("GameID")).child("players").child(mAuth.getCurrentUser().getDisplayName()).child("answer").setValue(answer);
			}
		});
		countDown();
	}

	void countDown()
	{
		countdownTimer.setProgress(i);
		//sets a 15 second timer to move forward every second
		mCountDownTimer=new CountDownTimer(15000,100) {

			@Override
			public void onTick(long millisUntilFinished) {
				i++;
				countdownTimer.setProgress(i*100/(15000/100));

			}

			@Override
			public void onFinish() {
				//Do what you want
				i++;
				countdownTimer.setProgress(100);
				if (TextUtils.isEmpty(answerInput.getText()))
				{
					answer = 0;
				}
				else
				{
					answer = Double.parseDouble(answerInput.getText().toString());
				}
				mDatabase.child("games").child(getIntent().getStringExtra("GameID")).child("players").child(mAuth.getCurrentUser().getDisplayName()).child("answer").setValue(answer);
				Intent intent = new Intent(getApplicationContext(), ScoreBoardActivity.class);
				intent.putExtra("GameID", getIntent().getStringExtra("GameID"));
				startActivity(intent);
			}
		};
		mCountDownTimer.start();
	}
}
