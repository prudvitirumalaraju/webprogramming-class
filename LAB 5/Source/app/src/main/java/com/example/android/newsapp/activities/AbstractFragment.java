package com.example.android.newsapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.newsapp.Loader.NewsLoader;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Utils.DefaultParameter;
import com.example.android.newsapp.Utils.GeneralParameter;
import com.example.android.newsapp.adapters.NewsAdapter;
import com.example.android.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by jennifernghinguyen on 1/17/17.
 * abstract class, can't instantiated
 */

public abstract class AbstractFragment extends Fragment implements LoaderCallbacks<List<News>> {
    private String section;
    private NewsAdapter adapter;
    private String baseUrl = DefaultParameter.DEFAULT_BASE_URL;
    private int page;
    private ViewHolder viewHolder;
    private int loaderConstant;
    private int maxPage = 0;

    /**
     * constructor
     *
     * @param loaderConstant
     * @param section
     * @param page
     */
    public AbstractFragment(int loaderConstant, String section, int page) {
        this.loaderConstant = loaderConstant;
        this.section = section;
        this.page = page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        if (savedInstanceState != null) {

            page = savedInstanceState.getInt(getContext().getString(R.string.page));
            maxPage = savedInstanceState.getInt(getContext().getString(R.string.maxPage));
        }
        populateViews(viewHolder, inflater, container);


        adapter = new NewsAdapter(getActivity(), new ArrayList<News>());
        viewHolder.listView.setAdapter(adapter);

        viewHolder.listView.setEmptyView(viewHolder.emptyView);
        viewHolder.listView.addFooterView(viewHolder.footView);
        enableEmptyView(false);


        return viewHolder.rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        startLoading(loaderConstant);

    }

    @Override
    public void onResume() {
        super.onResume();
        startLoading(loaderConstant);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(getActivity(), baseUrl, section, page);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        clearAdapter();
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        clearAdapter();
        lostDataEmptyView();

        if (data != null && !data.isEmpty()) {
            showProgressBar(false);
            adapter.addAll(data);
            // update max page for appropriate fragments
            if (loader.getId() == DefaultParameter.DEFAULT_US_CONSTANT) {
                maxPage = GeneralParameter.totalSizeUSSection;
            } else if (loader.getId() == DefaultParameter.DEFAULT_WORLD_CONSTANT) {
                maxPage = GeneralParameter.totalSizeWorldSection;
            } else if (loader.getId() == DefaultParameter.DEFAULT_TECH_CONSTANT) {
                maxPage = GeneralParameter.totalSizeTechSection;
            } else {
                maxPage = GeneralParameter.totalSizeSportSection;
            }
            viewHolder.listView.removeFooterView(viewHolder.footView);
            viewHolder.listView.addFooterView(viewHolder.footView);
        } else {
            lostDataEmptyView();
        }
    }

    public void clearAdapter() {
        if (adapter != null) {
            adapter.clear();

        }
    }

    /**
     * start loader here
     *
     * @param loaderConstant
     */

    public void startLoading(int loaderConstant) {

        if (checkNetWorkConnection()) {
            showProgressBar(true);
            enableEmptyView(false);
            clearAdapter();
            getLoaderManager().restartLoader(loaderConstant, null, AbstractFragment.this).forceLoad();
        } else {
            lostInternetConnectionEmptyView();

        }

    }

    /**
     * show progress bar ui
     *
     * @param on
     */

    public void showProgressBar(boolean on) {
        if (on) {
            viewHolder.loadingText.setVisibility(View.VISIBLE);
            viewHolder.loadingBar.setVisibility(View.VISIBLE);
        } else {
            viewHolder.loadingText.setVisibility(View.GONE);
            viewHolder.loadingBar.setVisibility(View.GONE);
        }
    }

    /**
     * show empty view ui
     *
     * @param visibility
     */
    public void enableEmptyView(boolean visibility) {
        if (visibility) {
            viewHolder.emptyView.setVisibility(View.VISIBLE);
            viewHolder.emptyViewImage.setVisibility(View.VISIBLE);
            viewHolder.emptyViewText.setVisibility(View.VISIBLE);
            viewHolder.emptyViewButton.setVisibility(View.VISIBLE);

        } else {
            viewHolder.emptyView.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.emptyView.setVisibility(View.GONE);
            viewHolder.emptyViewImage.setVisibility(View.GONE);
            viewHolder.emptyViewText.setVisibility(View.GONE);
            viewHolder.emptyViewButton.setVisibility(View.GONE);

        }
    }

    /**
     * set empty view ui: imageview, text, button text
     *
     * @param resId      - resource id for image view
     * @param textView   - text for text view
     * @param buttonText - text for button
     */
    public void setEmptyView(int resId, String textView, String buttonText) {
        viewHolder.loadingBar.setVisibility(View.GONE);
        viewHolder.emptyView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGray));
        viewHolder.emptyViewImage.setImageResource(resId);
        viewHolder.emptyViewText.setText(textView);
        viewHolder.emptyViewButton.setText(buttonText);
        viewHolder.emptyViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEmptyView(false);
                startLoading(loaderConstant);
            }
        });
    }

    /**
     * show and set up empty view for losing internet connection
     */
    public void lostInternetConnectionEmptyView() {
        enableEmptyView(true);
        setEmptyView(R.drawable.disconnect, getString(R.string.empty_text_network), getString(R.string.empty_button));
    }

    /**
     * show and set up empty view for failing retrieving data
     */
    public void lostDataEmptyView() {
        enableEmptyView(true);
        setEmptyView(R.drawable.error, getString(R.string.empty_text_data), getString(R.string.empty_button));
    }

    /**
     * populate all views, called inside oncreateview
     *
     * @param viewHolder
     * @param inflater
     * @param container
     */
    public void populateViews(final ViewHolder viewHolder, LayoutInflater inflater, ViewGroup container) {
        viewHolder.rootView = inflater.inflate(R.layout.news_list_view, container, false);
        viewHolder.footView = ((LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.news_list_view_footer, null, false);
        viewHolder.emptyView = (LinearLayout) viewHolder.rootView.findViewById(R.id.empty_view_container);
        viewHolder.emptyView.setVisibility(View.GONE);
        viewHolder.listView = (ListView) viewHolder.rootView.findViewById(R.id.list);
        viewHolder.loadingBar = (ProgressBar) viewHolder.rootView.findViewById(R.id.loading_bar);
        viewHolder.loadingText = (TextView) viewHolder.rootView.findViewById(R.id.loading_text);
        viewHolder.emptyViewImage = (ImageView) viewHolder.rootView.findViewById(R.id.empty_view_image);
        viewHolder.emptyViewText = (TextView) viewHolder.rootView.findViewById(R.id.empty_view_text);
        viewHolder.emptyViewButton = (Button) viewHolder.rootView.findViewById(R.id.empty_view_button);
        viewHolder.previousButton = (TextView) viewHolder.footView.findViewById(R.id.previous);
        viewHolder.nextButton = (TextView) viewHolder.footView.findViewById(R.id.next);

        viewHolder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetWorkConnection()) {
                    enableEmptyView(false);
                    int temp = ++page;
                    if (temp <= maxPage) {
                        startLoading(loaderConstant);
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_end_of_list), Toast.LENGTH_LONG).show();
                    }

                } else {
                    lostInternetConnectionEmptyView();
                }
            }
        });

        viewHolder.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetWorkConnection()) {
                    int temp = --page;
                    if (temp >= 1) {
                        startLoading(loaderConstant);
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_beginning_of_list), Toast.LENGTH_LONG).show();
                    }
                } else {
                    lostInternetConnectionEmptyView();
                }
            }
        });

        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = adapter.getItem(position);
                if (checkNetWorkConnection()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.getWebUrl())));
                } else {
                    lostInternetConnectionEmptyView();
                }
            }
        });

    }

    /**
     * check network connection
     *
     * @return boolean
     */
    private boolean checkNetWorkConnection() {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getContext().getString(R.string.page), page);
        outState.putInt(getContext().getString(R.string.maxPage), maxPage);
    }

    /**
     * hold key views for list view
     */
    static class ViewHolder {

        private ListView listView;
        private View rootView;
        private ProgressBar loadingBar;
        private TextView loadingText;
        private LinearLayout emptyView;
        private ImageView emptyViewImage;
        private TextView emptyViewText;
        private Button emptyViewButton;
        private View footView;
        private TextView nextButton;
        private TextView previousButton;

    }


}
