package com.example.photoblog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photoblog.Model.BlogPost;
import com.example.photoblog.R;

import java.text.DateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.BlogViewHolder> {
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
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {

        BlogPost blogPost = blog_list.get(position);

        holder.descView.setText(blogPost.getDesc());

        holder.blogDateView.setText(blogPost.getTimestamp().toString());

        Glide.with(mContext)
                .load(blogPost.getImage_url())
                .into(holder.blogImageView);


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
