package com.masonsrussell.bestguess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyActivity extends AppCompatActivity
{

	ArrayList<String> usersInGame = new ArrayList<>();
	private DatabaseReference mDatabase;
	ListView userListView;
	String gameID;
	Button startGameButton;
	TextView gameIDTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_lobby);
		onLoad();
	}

	void onLoad()
	{
		userListView = findViewById(R.id.usersList);
		gameIDTextView = findViewById(R.id.gameIDTextView);
		startGameButton = findViewById(R.id.startGameButton);
		mDatabase = FirebaseDatabase.getInstance().getReference();
		gameID = getIntent().getStringExtra("GameID");
		gameIDTextView.setText(gameID);
		DatabaseReference games = mDatabase.child("games");
		DatabaseReference currentGame = games.child(gameID);
		startGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mDatabase.child("games").child(gameID).child("gameInfo").child("isGameActive").setValue(true);
				Intent intent = new Intent(getApplicationContext(), PlayGameActivity.class);
				intent.putExtra("GameID", gameID);
				startActivity(intent);
			}
		});

		try
		{
			currentGame.child("players").addValueEventListener(new ValueEventListener()
			{
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					usersInGame.clear();
					HashMap<String, Object> text = (HashMap) dataSnapshot.getValue();
					usersInGame.addAll(text.keySet());
					//ArrayAdapter<String> adapter = new ArrayAdapter<>(LobbyActivity.this, android.R.layout.simple_list_item_1, usersInGame);
					ListAdapter listAdapter = new CustomListAdapter(LobbyActivity.this, R.layout.lobby_player_list_item, usersInGame);
					userListView.setAdapter(listAdapter);
				}

				@Override
				public void onCancelled(DatabaseError databaseError)
				{
					Log.d(CreateAccountActivity.TAG, databaseError.getDetails());
				}
			});
		}
		catch (Exception ex)
		{
			Log.d(CreateAccountActivity.TAG, ex.getMessage());
		}
	}

	public class CustomListAdapter extends ArrayAdapter <String>
	{

		private Context mContext;
		private int id;
		private List<String> items;

		public CustomListAdapter(Context context, int textViewResourceId, List<String> list)
		{
			super(context, textViewResourceId, list);
			mContext = context;
			id = textViewResourceId;
			items = list;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent)
		{
			View mView = v;
			if (mView == null)
			{
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mView = vi.inflate(id, null);
			}

			TextView text = mView.findViewById(R.id.usernameText);

			if (items.get(position) != null)
			{
				text.setText(items.get(position));
				text.setTextColor(Color.WHITE);
				text.setTextSize(24);
			}

			return mView;
		}
	}
}
