package com.karimtimer.everyboardymotivate;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.karimtimer.everyboardymotivate.models.*;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mQuoteList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseRecyclerAdapter<Quote, QuoteViewHolder> adapter;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuoteList = (RecyclerView) findViewById(R.id.quote_list);
        mQuoteList.setLayoutManager(new LinearLayoutManager(this));
        mQuoteList.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mQuoteList.setLayoutManager(mLayoutManager);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Board");
        mDatabase.keepSynced(true);

        DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Board");
        Query query = personRef.orderByKey();

        FirebaseRecyclerOptions<Quote> options =
                new FirebaseRecyclerOptions.Builder<Quote>()
                        .setQuery(query, Quote.class)
                        .build();


        adapter =  new FirebaseRecyclerAdapter<Quote, MainActivity.QuoteViewHolder>(options) {
            @NonNull
            @Override
            public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.quote_list, parent, false);

                return new QuoteViewHolder(view);
            }

            //
            @Override
            protected void onBindViewHolder(@NonNull QuoteViewHolder holder, int position, @NonNull Quote model) {

                final String post_key = getRef(position).getKey().toString();
                holder.setDesc(model.getDesc());
                holder.setName(model.getName());

            }
        };
        mQuoteList.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //we need a viewholder to set up the Recycler  view.
    public static class QuoteViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setName(String name) {
            TextView post_name = mView.findViewById(R.id.post_name);
            post_name.setText(name);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options:
                // Red item was selected
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
                return true;

            case R.id.add_post:
                //add_post was selected
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}