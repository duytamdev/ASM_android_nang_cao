package com.tamstudio.asm_duytam.fragments;

import static com.tamstudio.asm_duytam.utilities.Utilities.docNoiDung_Tu_URL;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
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
import com.tamstudio.asm_duytam.utilities.XMLDOMParser;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewsFragment extends Fragment {

    View mView;
    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();
    String s;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_news,container,false);
        initUI();
        new ReadRSS().execute("https://caodang.fpt.edu.vn/feed");
        return mView;
    }

    private void initUI() {
        rvNews = mView.findViewById(R.id.rv_news);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvNews.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getContext(),new NewsAdapter.ImyInterface() {
            @Override
            // click item news
            public void clickToReadNews(News news) {
                Intent intent = new Intent(getContext(), ReadNewsActivity.class);
                intent.putExtra("linkNews",news.getLinkNews());
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
        newsAdapter.setData(newsList);
        rvNews.setAdapter(newsAdapter);
    }
    private class ReadRSS extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Đang tải");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDescription = document.getElementsByTagName("description");
            NodeList nodeListContent = document.getElementsByTagName("content:encoded");
            String resImgNews ="";
            String title = "";
            String link = "";
            boolean isHadImg = false;
            for (int i = 0; i <nodeList.getLength() ; i++) {
                String cData = nodeListDescription.item(i+1).getTextContent();
                String desP = getDescriptionP(cData);

                Element element  = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element,"link");
                String cDataContent = nodeListContent.item(i).getTextContent();
                Matcher matcher = p.matcher(cDataContent);
                if(matcher.find() && !isHadImg){
                    resImgNews = matcher.group(1);
                    isHadImg = true;
                }
                newsList.add(new News(title,desP,resImgNews,link));
                isHadImg = false;
            }
            reloadData();
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }
    public static String getDescriptionP(String description){
        if(description.startsWith("<p")){
            String p = description.substring(description.indexOf("<p")+3,description.indexOf("</p>")-4);
            return p;
        }
        return null;
    }
    private void reloadData(){
        newsAdapter.setData(newsList);
        rvNews.setAdapter(newsAdapter);
    }

}
