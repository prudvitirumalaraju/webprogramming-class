package com.example.android.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.Utils.DateUtil;
import com.example.android.newsapp.models.News;

import java.util.List;

import static com.example.android.newsapp.Utils.DateUtil.formatDate;

/**
 * Created by jennifernghinguyen on 1/17/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private Context context;

    static class ViewHolder {
        private ImageView newsThumbnailImageView;
        private TextView headLineTextView;
        private TextView contributorTextView;
        private TextView publishedDateTextView;
        private TextView sectionTextView;
        private TextView trailTextTextView;


    }

    public NewsAdapter(Context context, List<News> objects) {

        super(context, 0, objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item_view, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder();
        News currentNews = getItem(position);

        viewHolder.newsThumbnailImageView = (ImageView) convertView.findViewById(R.id.news_thumbnail);
        viewHolder.headLineTextView = (TextView) convertView.findViewById(R.id.headline);
        viewHolder.contributorTextView = (TextView) convertView.findViewById(R.id.contributor);
        viewHolder.publishedDateTextView = (TextView) convertView.findViewById(R.id.published_date);
        viewHolder.sectionTextView = (TextView) convertView.findViewById(R.id.section_name);
        viewHolder.trailTextTextView = (TextView) convertView.findViewById(R.id.trail_text);

        //set image view
        if(currentNews.getThumbnail()!=null){
            viewHolder.newsThumbnailImageView.setImageBitmap(currentNews.getThumbnail());
        }else {
            viewHolder.newsThumbnailImageView.setVisibility(View.GONE);
        }


        //set headline
        viewHolder.headLineTextView.setText(currentNews.getHeadLine());

        //set author
        if (!currentNews.getContributor().equals(context.getString(R.string.empty_string))) {
            viewHolder.contributorTextView.setText(context.getString(R.string.adapter_contributor) + currentNews.getContributor());
        } else {
            viewHolder.contributorTextView.setText(context.getString(R.string.adapter_contributor_anonymous));
        }


        //set published date
        if (currentNews.getPublishedDate() != null) {
            viewHolder.publishedDateTextView.setText(formatDate(DateUtil.DATE_TIME_FORMAT, currentNews.getPublishedDate()));
        } else {
            viewHolder.publishedDateTextView.setVisibility(View.GONE);
        }


        //set section
        viewHolder.sectionTextView.setText(context.getString(R.string.adapter_section) + currentNews.getSection());

        //set trailText
        viewHolder.trailTextTextView.setText(currentNews.getTrailText());

        return convertView;
    }
}
