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
import com.example.photoblog.Model.BlogPost;
import com.example.photoblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.BlogViewHolder> {

    private FirebaseFirestore firebaseFirestore;


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

        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogViewHolder holder, int position) {

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


    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        CircleImageView blogUserImage;
        ImageView blogImageView;
        TextView descView, username, blogDateView;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogImageView = itemView.findViewById(R.id.blog_image);
            descView = itemView.findViewById(R.id.blog_desc);
            username = itemView.findViewById(R.id.blog_user_name);
            blogUserImage = itemView.findViewById(R.id.blog_user_image);
            blogDateView = itemView.findViewById(R.id.blog_date);
        }
    }
}
