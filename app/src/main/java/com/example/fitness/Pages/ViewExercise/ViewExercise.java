package com.example.fitness.Pages.ViewExercise;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import com.example.fitness.MainActivity;
import com.example.fitness.R;
import com.example.fitness.YoutubeConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ViewExercise extends YouTubeBaseActivity {

    private static final String TAG = "ViewExercise";
    private static final int PICK_FROM_FILE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int MEDIA_PERMISSION_CODE = 200;


    private TextView viewName, viewDesc;
    private Button btnPlay, btnBrowse, btnRecord, btnInc, btnDec,btnHome;
    private String url;
    private int descFontSize = 15;
    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
        }

        Uri vidUri = null;
        if(resultCode == RESULT_OK && requestCode == PICK_FROM_FILE) {
            vidUri = data.getData();
            VideoView vidView = findViewById(R.id.view_user_video);
            vidView.setVideoURI(vidUri);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(vidView);
            vidView.setMediaController(mediaController);
        }
    }

    public boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ViewExercise.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ViewExercise.this, new String[] {permission}, requestCode);
            return false;
        } else {
            Toast.makeText(ViewExercise.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Camera Permission Granted!", Toast.LENGTH_SHORT).show();
                makeRecordIntent();
            } else {
                Toast.makeText(getApplicationContext(),"Camera Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MEDIA_PERMISSION_CODE) {
            if (grantResults.length !=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Media Permission Granted!", Toast.LENGTH_SHORT).show();
                makeBrowseIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Media Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeRecordIntent() {
        Intent takeVidIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVidIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
        startActivityForResult(takeVidIntent, REQUEST_VIDEO_CAPTURE);
    }

    private void makeBrowseIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        startActivityForResult(i, PICK_FROM_FILE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        Log.d(TAG, "OnCreate: Starting");
        viewName = findViewById(R.id.view_name);
        viewDesc = findViewById(R.id.view_desc);
        //viewUrl = findViewById(R.id.view_url);
        btnPlay = findViewById(R.id.view_play);
        btnRecord = findViewById(R.id.record);
        btnBrowse = findViewById(R.id.view_browser_video);
        btnInc = findViewById(R.id.inc_view_btn);
        btnDec = findViewById(R.id.dec_view_btn);
        btnHome = findViewById(R.id.home_view_btn);
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
        
        
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mPerm = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, MEDIA_PERMISSION_CODE);
                if (mPerm) {
                    makeBrowseIntent();
                }
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                i.setType("video/*");
//                startActivityForResult(i, PICK_FROM_FILE);
            }
        });

        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descFontSize < 20) {
                    descFontSize += 1f;
                    viewDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descFontSize);
                }
            }
        });

        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descFontSize > 15) {
                    descFontSize -= 1f;
                    viewDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descFontSize);
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mPerm = checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                if (mPerm) {
                    makeRecordIntent();
                }
//                Intent takeVidIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                takeVidIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
//                startActivityForResult(takeVidIntent, REQUEST_VIDEO_CAPTURE);
            }
        });

    }

}