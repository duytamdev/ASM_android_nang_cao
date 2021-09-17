package com.tamstudio.asm_duytam.fragments;

import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.activities.ReadNewsActivity;
import com.tamstudio.asm_duytam.adapters.NewsAdapter;
import com.tamstudio.asm_duytam.model.News;


import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    View mView;
    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_news,container,false);
        getListTest();
        initUI();
        return mView;
    }
    private void getListTest() {
        for (int i = 0; i <10 ; i++) {
            News news = new News("Title"+i,"Description"+i,null,null);
            newsList.add(news);
        }
    }
    private void initUI() {
        rvNews = mView.findViewById(R.id.rv_news);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvNews.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(new NewsAdapter.ImyInterface() {
            @Override
            // click item news
            public void clickToReadNews(News news) {
                Intent intent = new Intent(getContext(), ReadNewsActivity.class);
                startActivity(intent);
            }
        });
        newsAdapter.setData(newsList);
        rvNews.setAdapter(newsAdapter);
    }

}
