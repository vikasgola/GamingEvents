package io.github.vikasgola.gamingevent;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ScheduleDetails extends AppCompatActivity {

    NonScroll team1lis,team2lis,comments;
    DatabaseReference databaseReference,team1,team2,comm;
    FirebaseListAdapter team1adapter,team2adapter,commadapter;
    TextView team1name,team2name,team1total,team2total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        team1lis = findViewById(R.id.team1Lis);
        team2lis = findViewById(R.id.team2Lis);

        team1name = findViewById(R.id.team1);
        team2name = findViewById(R.id.team2);

        team1total = findViewById(R.id.team1total);
        team2total = findViewById(R.id.team2total);

        comments = findViewById(R.id.comments);

        Intent intent = getIntent();

        if(intent.getStringExtra("modelId")==null){
            finish();
        }

        team1name.setText(intent.getStringExtra("team1"));
        team2name.setText(intent.getStringExtra("team2"));

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("scoredetails").child(intent.getStringExtra("modelId")).child(intent.getStringExtra("teams"));

        team1 = databaseReference.child(intent.getStringExtra("team1"));
        team2 = databaseReference.child(intent.getStringExtra("team2"));
        comm = databaseReference.child("comments");
        Query query = comm.limitToFirst(5);

        FirebaseListOptions<String> commentsOption = new FirebaseListOptions.Builder<String>()
                .setLayout(R.layout.comment)
                .setQuery(query,String.class)
                .build();

        commadapter = new FirebaseListAdapter<String>(commentsOption) {
            @Override
            protected void populateView(View v, String model, int position) {
                ((TextView)v.findViewById(R.id.c)).setText(model);
            }
        };

        comments.setAdapter(commadapter);


        FirebaseListOptions<Player> firebaseListOptions = new FirebaseListOptions.Builder<Player>()
                .setLayout(R.layout.scores)
                .setQuery(team1, new SnapshotParser<Player>() {
                    @NonNull
                    @Override
                    public Player parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Player player = new Player();
                        player.setName(snapshot.getKey());
                        player.setScore((Long)snapshot.getValue());
                        return player;
                    }
                })
                .build();

        team1adapter = new FirebaseListAdapter<Player>(firebaseListOptions) {
            @Override
            protected void populateView(View v, Player model, int position) {
                String na = Integer.toString(position+1)+ ": " + model.getName();
                ((TextView)v.findViewById(R.id.name)).setText(na);
                ((TextView)v.findViewById(R.id.score)).setText(Long.toString(model.getScore()));
            }
        };

        team1lis.setAdapter(team1adapter);

        FirebaseListOptions<Player> firebaseListOptions2 = new FirebaseListOptions.Builder<Player>()
                .setLayout(R.layout.scores)
                .setQuery(team2, new SnapshotParser<Player>() {
                    @NonNull
                    @Override
                    public Player parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Player player = new Player();
                        player.setName(snapshot.getKey());
                        player.setScore((Long) snapshot.getValue());
                        return player;
                    }
                })
                .build();

        team2adapter = new FirebaseListAdapter<Player>(firebaseListOptions2) {
            @Override
            protected void populateView(View v, Player model, int position) {
                String n = Integer.toString(position+1)+ ": " + model.getName();
                ((TextView)v.findViewById(R.id.name)).setText(n);
                ((TextView)v.findViewById(R.id.score)).setText(Long.toString(model.getScore()));
            }
        };

        team2lis.setAdapter(team2adapter);

        team1adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                long total = 0;
                for(int i=0;i<team1adapter.getCount();i++){
                    total = total + ((Player)team1adapter.getItem(i)).getScore();
                }
                String t = Long.toString(total);
                team1total.setText(t);
            }
        });

        team2adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                long total = 0;
                for(int i=0;i<team2adapter.getCount();i++){
                    total = total + ((Player)team2adapter.getItem(i)).getScore();
                }
                String t = Long.toString(total);
                team2total.setText(t);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        team1adapter.startListening();
        team2adapter.startListening();
        commadapter.startListening();

    }
}
