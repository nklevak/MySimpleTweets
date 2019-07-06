package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    // instance variables
    ImageView ivProfile;
    TextView tvUsername;
    EditText etTweet;
    Button btnCancel;
    Button btnPost;
    TextView tvCounter;
    TwitterClient client;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // initializes instance variables
        ivProfile = this.findViewById(R.id.ivProfile);
        tvUsername = this.findViewById(R.id.tvUsername);
        etTweet = this.findViewById(R.id.etTweet);
        btnCancel = this.findViewById(R.id.btnCancel);
        btnPost = this.findViewById(R.id.btnPost);
        client = TwitterApp.getRestClient(this);
        tvCounter = this.findViewById(R.id.tvCounter);

        // gets User and sets username and profile image
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    User user = User.fromJSON(response);
                    tvUsername.setText("@" + user.screenName);
                    imageUrl = user.profileImageUrl;
                    // to get profile images to load
                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .into(ivProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        // creates char count
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCounter.setText(280 - s.toString().length() + " characters remaining");

            }
        });

    }

    // method called when post button is clicked
    public void composeTweet(android.view.View view) {
        // grabs body of tweet
        final String body = etTweet.getText().toString();

        client.sendTweet(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    // creates intent to return to timeline once tweet is submitted
                    Intent data = new Intent();
                    data.putExtra("tweetInfo", Parcels.wrap(tweet));

                    // returns the intent with information
                    setResult(RESULT_OK, data);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    // method called when cancel button is clicked
    public void onCancel(android.view.View view) {
        // gets current context
        Context context = view.getContext();

        // creates intent to open timeline
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra("code", 200);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
