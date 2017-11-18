package com.example.android.newsapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.android.newsapp.R;
import com.example.android.newsapp.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.newsapp.Utils.DateUtil.JSON_FORMAT;

/**
 * Created by jennifernghinguyen on 1/17/17.
 */

public final class QueryUtils {
    final static String LOG_TAG = QueryUtils.class.getSimpleName();
    public static Context context;

    private QueryUtils() {
        throw new AssertionError("can't instantiate Query Util");
    }

    /**
     * build a complete url
     *
     * @param urlBase
     * @param section - sectionId
     * @return
     */
    public static String buildURI(Context ct, String urlBase, String section, int page) {
        context = ct;
        if (urlBase == null && section == null && page < 1) {
            return null;
        }
        Uri base = Uri.parse(urlBase);
        Uri.Builder builder = base.buildUpon();
        builder.path(section);
        builder.appendQueryParameter(context.getString(R.string.query_util_api_key_param), DefaultParameter.DEFAULT_API_KEY);
        builder.appendQueryParameter(context.getString(R.string.query_util_show_field_param), DefaultParameter.DEFAULT_FIELDS);
        builder.appendQueryParameter(context.getString(R.string.query_util_order_by_param), DefaultParameter.DEFAULT_ORDER_BY);
        builder.appendQueryParameter(context.getString(R.string.query_util_from_date_param), DateUtil.formatDate(DateUtil.URL_FORMAT, DateUtil.getXDaysBeforeToday(10)));// 10 days before today
        builder.appendQueryParameter(context.getString(R.string.query_util_to_date_param), DateUtil.formatDate(DateUtil.URL_FORMAT, DateUtil.getTodayDate())); // today' date
        builder.appendQueryParameter(context.getString(R.string.query_util_page_size_param), String.valueOf(DefaultParameter.DEFAULT_PAGE_SIZE));
        builder.appendQueryParameter(context.getString(R.string.query_util_page_param), String.valueOf(page));
        builder.appendQueryParameter(context.getString(R.string.query_util_show_tags_param), DefaultParameter.DEFAULT_TAGS);

        return builder.toString().replace("%2C", ",");
    }

    /**
     * createURL
     */
    public static URL createURL(String urlString) {
        URL url = null;
        if (urlString == null) {
            return null;
        }

        if (url == null) {
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, context.getString(R.string.query_util_error_create_url));
            }
        }
        return url;
    }

    /**
     * download JSON response
     */

    public static String downloadJsonResponse(URL url) throws IOException {
        String response = context.getString(R.string.empty_string);
        if (url == null) {
            return response;
        }

        //open HTTP connection
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setRequestMethod("GET");
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                response = getResponseFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.query_util_error_connection));
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }

    /**
     * build json response string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String getResponseFromStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                response.append(line);
                line = reader.readLine();
            }
        }

        return response.toString();
    }

    /**
     * extract news fron json response
     */
    public static ArrayList<News> extractNews(String jsonResponse, String sectionName) {
        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            JSONObject response = root.getJSONObject(context.getString(R.string.query_util_json_response));

            if (sectionName.equals(DefaultParameter.DEFAULT_US_SECTION)) {
                GeneralParameter.totalSizeUSSection = Integer.valueOf(extractString(response, context.getString(R.string.query_util_json_pages)));
            } else if (sectionName.equals(DefaultParameter.DEFAULT_WORLD_SECTION)) {
                GeneralParameter.totalSizeWorldSection = Integer.valueOf(extractString(response, context.getString(R.string.query_util_json_pages)));
            } else if (sectionName.equals(DefaultParameter.DEFAULT_TECH_SECTION)) {
                GeneralParameter.totalSizeTechSection = Integer.valueOf(extractString(response, context.getString(R.string.query_util_json_pages)));
            } else {
                GeneralParameter.totalSizeSportSection = Integer.valueOf(extractString(response, context.getString(R.string.query_util_json_pages)));
            }

            JSONArray results = response.getJSONArray(context.getString(R.string.query_util_json_results));


            for (int i = 0; i < results.length(); i++) {
                JSONObject newInfo = (JSONObject) results.get(i);


                String section = extractString(newInfo, context.getString(R.string.query_util_json_section_name));


                Date publishedDate = DateUtil.getDate(JSON_FORMAT, extractString(newInfo, context.getString(R.string.query_util_json_web_publication_date)));

                String webUrl = extractString(newInfo, context.getString(R.string.query_util_json_web_url));


                JSONArray tags = newInfo.getJSONArray(context.getString(R.string.query_util_json_tags));


                String contributor = null;
                if (tags.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tags.get(0);


                    contributor = extractString(contributorTag, context.getString(R.string.query_util_json_web_title));

                } else {
                    //no contributor
                    contributor = context.getString(R.string.empty_string);
                }
                JSONObject fields = newInfo.getJSONObject(context.getString(R.string.query_util_json_fields));


                String headline = extractString(fields, context.getString(R.string.query_util_json_headline));


                String trailText = extractString(fields, context.getString(R.string.query_util_json_trailText));


                String thumbnailUrl = extractString(fields, context.getString(R.string.query_util_json_thumbnail));


                Bitmap thumbnail = null;
                if (thumbnailUrl != null && !thumbnailUrl.equals(context.getString(R.string.empty_string))) {
                    thumbnail = makeBitmap(thumbnailUrl);
                }

                if (thumbnail != null) {
                    news.add(new News(headline, section, publishedDate, trailText, webUrl, contributor, thumbnail));
                } else {
                    news.add(new News(headline, section, publishedDate, trailText, webUrl, contributor));
                }

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, context.getString(R.string.query_util_error_extracting));
        }
        return news;
    }

    private static Bitmap makeBitmap(String thumbnailUrl) {
        Bitmap thumbnail = null;

        if (thumbnailUrl != null) {
            InputStream in = null;

            try {
                in = createURL(thumbnailUrl).openStream();

            } catch (IOException e) {
                Log.e(LOG_TAG, context.getString(R.string.query_util_error_bitmap));
            }

            if (in != null) {
                thumbnail = BitmapFactory.decodeStream(in);
            }
        }
        return thumbnail;
    }

    private static String extractString(JSONObject newInfo, String stringName) {
        String str = null;

        try {
            str = newInfo.getString(stringName);
        } catch (JSONException e) {
            Log.e(LOG_TAG, context.getString(R.string.query_util_error_extract_string) + stringName);
        }

        if (str != null) {
            return str;
        } else {
            return context.getString(R.string.empty_string);
        }
    }

    public static ArrayList<News> fetchNewsData(String urlString, String section) {
        ArrayList<News> news = new ArrayList<>();
        URL url = createURL(urlString);

        String response = context.getString(R.string.empty_string);
        try {
            response = downloadJsonResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.query_util_error_fetch_data));
        }

        if (!response.equals(context.getString(R.string.empty_string))) {
            news = extractNews(response, section);
        }

        return news;

    }
}
