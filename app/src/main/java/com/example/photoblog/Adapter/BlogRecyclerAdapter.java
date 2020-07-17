package com.example.photoblog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photoblog.MainActivity;
import com.example.photoblog.Model.BlogPost;
import com.example.photoblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.BlogViewHolder> {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    Context mContext;
    List<BlogPost> blog_list;

    public BlogRecyclerAdapter(Context mContext, List<BlogPost> blog_list) {
        this.mContext = mContext;
        this.blog_list = blog_list;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_list_item, parent, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        BlogPost blogPost = blog_list.get(position);

        holder.descView.setText(blogPost.getDesc());

        holder.blogDateView.setText(blogPost.getTimestamp().toString());

        Glide.with(mContext)
                .load(blogPost.getImage_url())
                .into(holder.blogImageView);


        String user_id = blog_list.get(position).getUser_id();
        //TO retrieve data from User
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.username.setText(userName);
                    Glide.with(mContext).load(userImage).into(holder.blogUserImage);

                } else {
                    String err = task.getException().getMessage();
                    Toast.makeText(mContext, "Err: " + err, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Get Likes count
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    int count = queryDocumentSnapshots.size();
                    holder.updateLikesCount(count);

                } else {
                    holder.updateLikesCount(0);
                }
            }
        });

        //Get Likes
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes")
                .document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    holder.blogLikeBtn.setImageDrawable(mContext.getDrawable(R.drawable.action_like_accent));
                } else {
                    holder.blogLikeBtn.setImageDrawable(mContext.getDrawable(R.drawable.action_like_gray));
                }
            }
        });


        //Likes feature
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes")
                        .document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

//                          firebaseFirestore.collection("Posts").document(blogPostId).collection("Likes")
//                          mathi ko lai sajilo way ma
                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes")
                                    .document(currentUserId).set(likesMap);

                        } else {
                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes")
                                    .document(currentUserId).delete();
                        }
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView blogUserImage;
        private ImageView blogImageView;
        private TextView descView, username, blogDateView;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogImageView = itemView.findViewById(R.id.blog_image);
            descView = itemView.findViewById(R.id.blog_desc);
            username = itemView.findViewById(R.id.blog_user_name);
            blogUserImage = itemView.findViewById(R.id.blog_user_image);
            blogDateView = itemView.findViewById(R.id.blog_date);

            blogLikeBtn = itemView.findViewById(R.id.blog_like_btn);
//            blogLikeCount = itemView.findViewById(R.id.blog_like_count);
        }

        public void updateLikesCount(int count) {
            blogLikeCount = itemView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " Likes");
        }
    }
}
