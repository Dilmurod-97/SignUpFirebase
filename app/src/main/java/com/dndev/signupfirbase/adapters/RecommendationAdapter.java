package com.dndev.signupfirbase.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dndev.signupfirbase.CustomViewHolder;
import com.dndev.signupfirbase.Models.NewsHeadlines;
import com.dndev.signupfirbase.R;
import com.dndev.signupfirbase.SelectListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder>{

    private Context context;
    private List<NewsHeadlines> headlines;
    private ClickListener listener;

    public RecommendationAdapter(Context context, List<NewsHeadlines> headlines, ClickListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = headlines.get(position).getTitle();
        String url = headlines.get(position).getUrlToImage();

        holder.title.setText(title);
        Picasso.get().load(url).into(holder.imageView);
        holder.imageView.getRootView().setOnClickListener(view -> {
            listener.onClick(headlines.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public interface ClickListener{
        void onClick(NewsHeadlines newsHeadlines);
    }
}
