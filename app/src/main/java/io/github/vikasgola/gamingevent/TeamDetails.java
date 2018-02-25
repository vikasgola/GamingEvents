package io.github.vikasgola.gamingevent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeamDetails extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference databaseReference;
    ListView listView;
    FirebaseListAdapter firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();

        if(intent.getStringExtra("teamName")==null ){
            Log.d("error","null intent to teamdetails");
            finish();
        }

        listView = findViewById(R.id.teamMembers);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("teamdetails")
                .child(intent.getStringExtra("modelId")).child(intent.getStringExtra("teamName"));

        toolbar.setTitle(intent.getStringExtra("teamName"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseListOptions<String> firebaseListOptions2 = new FirebaseListOptions.Builder<String>()
                .setLayout(R.layout.team)
                .setQuery(databaseReference,String.class)
                .build();

        firebaseListAdapter = new FirebaseListAdapter<String>(firebaseListOptions2){
            @Override
            protected void populateView(View v, String model, int position) {
                String hmm =Integer.toString(position+1)+":  " + model;
                ((TextView)v.findViewById(R.id.teams)).setText(hmm);
            }
        };

        listView.setAdapter(firebaseListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();
    }
}
