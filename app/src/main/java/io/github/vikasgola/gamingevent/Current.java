package io.github.vikasgola.gamingevent;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Current extends Fragment {
    RecyclerView listView;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    View v;
    SwipeRefreshLayout swipeRefreshLayout;
    String tab="";
    TextView noEvent;

    public Current(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_current, container, false);
        if(getArguments()!=null)
            tab = getArguments().getString("key");

        noEvent = v.findViewById(R.id.noEvents);
        noEvent.setVisibility(View.GONE);

        listView = v.findViewById(R.id.listView);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(tab);
        databaseReference.keepSynced(true);

        FirebaseRecyclerOptions<Event> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(databaseReference, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Event event =new Event();
                        event.setId(snapshot.getKey());
                        event.setImage((String) snapshot.child("image").getValue());
                        event.setStatus((String) snapshot.child("status").getValue());
                        event.setTime((String) snapshot.child("time").getValue());
                        event.setTotalTeams((Long)snapshot.child("totalTeams").getValue());
                        return event;
                    }
                })
                .build();

        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        listView.setLayoutManager(llm);
        setHasOptionsMenu(true);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event,eventViewHolder>(firebaseRecyclerOptions) {
            @Override
            public eventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(viewType,parent,false);
                return new eventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull eventViewHolder holder, final int position, @NonNull final Event model) {
                holder.time.setText(model.getTime());
                String tot = "Total Teams: "+model.getTotalTeams();
                holder.totalTeams.setText(tot);
                Glide.with(v.getContext()).load(model.getImage()).into(holder.imageGame);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getContext(),OpenEventActivity.class);
                        intent.putExtra("modelId",model.getId());
                        intent.putExtra("tab",tab);
                        startActivity(intent);
                    }
                });
                noEvent.setVisibility(View.GONE);
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.event;
            }
        };

        listView.setAdapter(firebaseRecyclerAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return v;
    }

    public void refresh(){
        swipeRefreshLayout.setRefreshing(true);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        if(App.haveNetworkConnection(getContext())){
            noEvent.setText("No Events!");
            if(firebaseRecyclerAdapter.getItemCount()==0){
                noEvent.setVisibility(View.VISIBLE);
            }else {
                noEvent.setVisibility(View.GONE);
            }
        }else {
            noEvent.setText("Check you Internet Connection..!");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public static class eventViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView imageGame;
        private TextView time,totalTeams;

        public eventViewHolder(final View itemView) {
            super(itemView);
            imageGame = itemView.findViewById(R.id.imageGame);
            time = itemView.findViewById(R.id.time);
            totalTeams = itemView.findViewById(R.id.totalTeams);
        }

    }
}
