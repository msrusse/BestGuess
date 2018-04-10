package com.masonsrussell.bestguess;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity
{
	private DatabaseReference mDatabase;
	ListView scoresList;
	ArrayList<String> users = new ArrayList<>();
	ArrayList<String> usersWithScores = new ArrayList<>();
	String gameID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_board);
		onLoad();
	}

	private void onLoad()
	{
		mDatabase = FirebaseDatabase.getInstance().getReference();
		scoresList = findViewById(R.id.usersList);
		gameID = getIntent().getStringExtra("GameID");
		getUsers();

	}

	private void getUsers()
	{
		DatabaseReference usersRef = mDatabase.child("games").child(gameID).child("players");

		usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				users.clear();
				HashMap<String, Object> text = (HashMap) dataSnapshot.getValue();
				users.addAll(text.keySet());
				getScores();
			}

			@Override
			public void onCancelled(DatabaseError databaseError)
			{

			}
		});
	}

	private void getScores()
	{
		DatabaseReference usersRef = mDatabase.child("games").child(gameID).child("players");
		for (final String name : users)
		{
			usersRef.child(name).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					Object score = dataSnapshot.getValue(Object.class);
					String userAndScore = name + ": " + String.valueOf(score);
					usersWithScores.add(userAndScore);
					setListView();
				}

				@Override
				public void onCancelled(DatabaseError databaseError)
				{

				}
			});
		}
	}

	private void setListView()
	{
		ListAdapter listAdapter = new CustomListAdapter(ScoreBoardActivity.this, R.layout.lobby_player_list_item, usersWithScores);
		scoresList.setAdapter(listAdapter);
	}

	private class CustomListAdapter extends ArrayAdapter<String>
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
