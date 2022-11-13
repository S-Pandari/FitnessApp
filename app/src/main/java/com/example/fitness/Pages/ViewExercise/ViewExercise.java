package com.example.fitness.Pages.ViewExercise;
import com.example.fitness.R;
import com.example.fitness.YoutubeConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Bind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewExercise extends YouTubeBaseActivity {

    private static final String TAG = "ViewExercise";

    private TextView viewName, viewDesc;
    private Button btnPlay;
    private String url;
    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        Log.d(TAG, "OnCreate: Starting");
        viewName = findViewById(R.id.view_name);
        viewDesc = findViewById(R.id.view_desc);
        //viewUrl = findViewById(R.id.view_url);
        btnPlay = findViewById(R.id.view_play);
        mYoutubePlayerView = (YouTubePlayerView) findViewById(R.id.view_youtube);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewName.setText(extras.getString("uName"));
            viewDesc.setText(extras.getString("uDesc"));
            url = extras.getString("uUrl");
        }

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Initialize Success");
                if (!url.equals("")){
                    youTubePlayer.loadVideo(url);
                } else {
                    youTubePlayer.loadVideo("qEVUtrk8_B4");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Failed loading Video");
            }
        };

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Initializing YouTube player!");
                mYoutubePlayerView.initialize(YoutubeConfig.getApiKey(), mOnInitializedListener);
            }
        });


    }
}