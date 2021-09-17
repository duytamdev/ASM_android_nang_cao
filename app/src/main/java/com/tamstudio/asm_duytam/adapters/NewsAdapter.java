package com.tamstudio.asm_duytam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    public interface ImyInterface{
        void clickToReadNews(News news);
    }
    ImyInterface imyInterface;
    View view;
    List<News> list = new ArrayList<>();

    public NewsAdapter(ImyInterface imyInterface) {
        this.imyInterface = imyInterface;
    }

    public void setData(List<News> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = list.get(position);
        if(news==null) return;
        holder.tvTitle.setText(news.getTitle());
        holder.tvDescription.setText(news.getDescription());
       // Picasso.get().load(news.getResImg()).into(holder.ivRes);// get img form internet
        view.setOnClickListener(view1 -> {
            imyInterface.clickToReadNews(news);
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRes;
        TextView tvTitle,tvDescription;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRes = itemView.findViewById(R.id.iv_resImgNews);
            tvTitle = itemView.findViewById(R.id.tv_title_news);
            tvDescription = itemView.findViewById(R.id.tv_description_news);
        }
    }
}
