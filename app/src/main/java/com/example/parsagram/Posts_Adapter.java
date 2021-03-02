package com.example.parsagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class Posts_Adapter extends RecyclerView.Adapter<Posts_Adapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public Posts_Adapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Posts_Adapter.ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username, caption;
        private ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_view);
            caption = itemView.findViewById(R.id.caption_view);
            photo = itemView.findViewById(R.id.photo_view);
        }

        public void bind(Post post) {
            // populate the post row
            caption.setText(post.getDescription());
            username.setText(post.getUser().getUsername());
            ParseFile post_photo = post.getImage();

            if (post_photo != null)
                Glide.with(context).load(post_photo.getUrl()).into(photo);
        }
    }
}
