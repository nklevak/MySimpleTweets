package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // id
    public User user;
    public String createdAt;
    public String relativeDate;
//    public String additionalImageUrl;

    // "deserialize" aka get information from the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the vals from JSon
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.relativeDate = tweet.getRelativeTime(tweet.createdAt);
        // extract image url
//        JSONObject url = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0);
//        if (url.getString("media_url_https") != null) {
//            tweet.additionalImageUrl = url.getString("media_url_https");
//        }
//        else {
//            tweet.additionalImageUrl = null;
//        }
//        tweet.additionalImageUrl = url.getString("media_url_https");
//        tweet.additionalImageUrl = url.getString("url");
        return tweet;
    }

    // empty constructor needed for parcel
    public Tweet(){

    }

    // setters
    public void setUser (User user) {
        this.user = user;
        this.uid = user.uid;
    }

    public void setBody (String body) {
        this.body = body;
    }

    // method to convert to relative time
    // todo -- fix formatting
    public String getRelativeTime(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
