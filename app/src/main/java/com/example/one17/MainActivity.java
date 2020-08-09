package com.example.one17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<BlogPost, BlogPostHolder> firebaseRecyclerAdapter;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef;



        FirebaseRecyclerOptions<BlogPost> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<BlogPost>()
                        .setQuery(query, new SnapshotParser<BlogPost>() {
                            @NonNull
                            @Override
                            public BlogPost parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new BlogPost(snapshot.child("itemName").getValue().toString(),
                                        snapshot.child("storeID").getValue().toString(),
                                        snapshot.child("itemType").getValue().toString(),
                                        snapshot.child("itemRating").getValue().toString());
                            }
                        })
                        .build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BlogPost, BlogPostHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BlogPostHolder blogPostHolder, final int position, @NonNull final BlogPost blogPost) {
                blogPostHolder.setBlogPost(blogPost);
                blogPostHolder.imageThumbtextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public BlogPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

                return new BlogPostHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private class BlogPostHolder extends RecyclerView.ViewHolder {
        private TextView imageThumbtextView, userIdTextView, imageUrlTextView, descTextView;

        BlogPostHolder(View itemView) {
            super(itemView);
            imageThumbtextView = itemView.findViewById(R.id.image_thumb_text_view);
            userIdTextView = itemView.findViewById(R.id.user_id_text_view);
            imageUrlTextView = itemView.findViewById(R.id.image_url_text_view);
            descTextView = itemView.findViewById(R.id.desc_text_view);
        }

        void setBlogPost(BlogPost blogPost) {
            String imageThumb = blogPost.getImageThumb();
            imageThumbtextView.setText(imageThumb);
            String userId = blogPost.getUserId();
            userIdTextView.setText(userId);
            String imageUrl = blogPost.getImageUrl();
            imageUrlTextView.setText(imageUrl);
            String desc = blogPost.getDesc();
            descTextView.setText(desc);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseRecyclerAdapter!= null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

}