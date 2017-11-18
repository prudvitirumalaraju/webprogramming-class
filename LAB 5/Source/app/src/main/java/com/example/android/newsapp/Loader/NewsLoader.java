package com.example.android.newsapp.Loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.newsapp.Utils.DefaultParameter;
import com.example.android.newsapp.Utils.QueryUtils;
import com.example.android.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jennifernghinguyen on 1/19/17.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    final static String LOG_TAG = NewsLoader.class.getSimpleName();
    private String baseUrl;
    private String section;
    private int page = DefaultParameter.DEFAULT_PAGE;
    private Context context;

    public NewsLoader(Context context, String baseUrl, String section, int page) {
        super(context);
        this.baseUrl = baseUrl;
        this.section = section;
        this.page = page;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        ArrayList<News> news;
        if ((baseUrl.length() < 1 || baseUrl == null) || (section.length() < 1 || section == null) || page < 1) {
            return null;
        }

        String url = QueryUtils.buildURI(context, baseUrl, section, page);
        news = QueryUtils.fetchNewsData(url, section);

        return news;
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<News> data) {
        super.onCanceled(data);
        if (data != null) {
            data.clear();
        }

    }
}
