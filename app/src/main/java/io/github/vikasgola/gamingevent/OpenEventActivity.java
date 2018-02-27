package io.github.vikasgola.gamingevent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OpenEventActivity extends AppCompatActivity {

    ImageView imageView;
    ScrollView scrollView;
    DatabaseReference databaseReference,teamsB,schedule;
    NonScroll scheduleList,teamLis;
    FirebaseListAdapter firebaseListAdapter,firebaseListAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_event);

        Intent intent = getIntent();
        if (intent.getStringExtra("modelId") == null || intent.getStringExtra("tab") == null) {
            Toast.makeText(this, "Some error occurred!", Toast.LENGTH_SHORT).show();
            finish();
        }


        scrollView = findViewById(R.id.scrollView);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        final String modelId =getIntent().getStringExtra("modelId");

        scheduleList = findViewById(R.id.listView);

        teamLis =findViewById(R.id.recyleView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(intent.getStringExtra("tab"));

        imageView = findViewById(R.id.image);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot = dataSnapshot.child(modelId);
                Glide.with(getApplicationContext()).load(dataSnapshot.child("image").getValue()).into(imageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error",databaseError.getMessage());
            }
        });

        teamsB = databaseReference.child(modelId).child("teams");
        schedule = databaseReference.child(modelId).child("schedules");

        teamsB.keepSynced(true);
        schedule.keepSynced(true);

        FirebaseListOptions<Schedule> firebaseListOptions = new FirebaseListOptions.Builder<Schedule>()
                .setLayout(R.layout.schedule_item)
                .setQuery(schedule,Schedule.class)
                .build();

        firebaseListAdapter = new FirebaseListAdapter<Schedule>(firebaseListOptions){
            @Override
            protected void populateView(View v, final Schedule model, int position) {
                ((TextView)v.findViewById(R.id.teamVS)).setText(model.getTeam1() + " VS " + model.getTeam2());
                ((TextView)v.findViewById(R.id.sch)).setText(" ");
                if(model.getSchedule()!=null)
                    ((TextView)v.findViewById(R.id.sch)).setText(model.getSchedule());
                    v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(getApplicationContext(),ScheduleDetails.class);
                        intent2.putExtra("modelId",modelId);
                        intent2.putExtra("team1",model.getTeam1());
                        intent2.putExtra("team2",model.getTeam2());
                        intent2.putExtra("teams",model.getTeam1() + " VS " + model.getTeam2());
                        startActivity(intent2);
                    }
                });
            }
        };

        scheduleList.setAdapter(firebaseListAdapter);

        FirebaseListOptions<Team> firebaseListOptions2 = new FirebaseListOptions.Builder<Team>()
                .setQuery(teamsB,Team.class)
                .setLayout(R.layout.team)
                .build();

        firebaseListAdapter2 = new FirebaseListAdapter<Team>(firebaseListOptions2){
            @Override
            protected void populateView(View v, final Team model, int position) {
                ((TextView)v.findViewById(R.id.teams)).setText(model.getTeamName());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getApplicationContext(),TeamDetails.class);
                        intent1.putExtra("modelId",modelId);
                        intent1.putExtra("teamName",model.getTeamName());
                        startActivity(intent1);
                    }
                });
            }
        };


        teamLis.setAdapter(firebaseListAdapter2);

    }
        @Override
        public void onStart() {
            super.onStart();
            firebaseListAdapter.startListening();
            firebaseListAdapter2.startListening();
            scrollView.smoothScrollTo(0,0);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onStop() {
            super.onStop();
            firebaseListAdapter.stopListening();
            firebaseListAdapter2.stopListening();
        }

}
