package com.skydoves.waterdays.services.receivers;

import android.content.Context;
import android.os.AsyncTask;

import com.skydoves.waterdays.consts.LocalUrls;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class LocalWeather extends AsyncTask<String, Integer, String> {

    // Systems
    PreferenceManager systems;
    String TAG = "LocalWeather";
    Context mContext;

    ArrayList<ShortWeather> shortWeathers = new ArrayList<ShortWeather>();

    public LocalWeather(Context context){
        this.mContext = context;
        this.systems = new PreferenceManager(context);
    }

    // doInBackground //
    public String doInBackground(String[] StringParams) {
        String url = LocalUrls.getLocalUrl(systems.getInt("localIndex", 0));
        Response response;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        // Xml Parsing : Get Reh Data
        try {
            response = client.newCall(request).execute();
            int total = parseXML(response.body().string());
            return String.valueOf(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // onPostExecute //
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    // Xml Parsing : Get Reh Data //
    //region
    int parseXML(String xml) {
        int total=0;
        try {
            int i = 0;
            String tagName = "";
            boolean onreh = false;
            boolean onEnd = false;
            boolean isItemTag1 = false;

            // Initialize XmlPullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = parser.getName();
                    if (tagName.equals("data")) {
                        shortWeathers.add(new ShortWeather());
                        onEnd = false;
                        isItemTag1 = true;
                    }
                } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                    if(tagName.equals("reh") && !onreh){
                        shortWeathers.get(i).setReh(parser.getText());
                        total += Integer.parseInt(parser.getText());
                        onreh = true;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (tagName.equals("s06") && onEnd == false) {
                        i++;
                        isItemTag1 = false;
                        onreh = false;
                        onEnd = true;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {

        }
        return total/shortWeathers.size();
    }

    // Short Local Weather //
    public class ShortWeather {
        private String reh;

        public String getReh() {
            return reh;
        }

        public void setReh(String reh) {
            this.reh = reh;
        }
    }
    //endregion
}