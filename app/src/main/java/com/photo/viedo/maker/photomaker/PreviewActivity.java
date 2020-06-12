package com.photo.viedo.maker.photomaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.VideoView;

public class PreviewActivity extends AppCompatActivity{
    public  VideoView videoView;
    private SeekBar seekBar;
    private int stopPosition;
    RecyclerView recyclerView;
    FrameAdapter frameAdapter;
    int [] imgs;
  //  ArrayList<Integer> imgs = new ArrayList<>();
    ImageView img;
    Button frameimg,effectimg,stickerimg;
    private static final String POSITION = "position";
    private static final String FILEPATH = "filepath";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        videoView = (VideoView) findViewById(R.id.videoView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        img = (ImageView) findViewById(R.id.pause);
        recyclerView = findViewById(R.id.layout);
        frameimg = (Button) findViewById(R.id.frames);
        effectimg = (Button)findViewById(R.id.effects);
        stickerimg = findViewById(R.id.Sticker);
        stickerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(PreviewActivity.this, 3);
                recyclerView.setLayoutManager(gridLayoutManager);
                StickerAdapter stickerAdapter = new StickerAdapter (getApplicationContext(),imgs);
                recyclerView.setAdapter(stickerAdapter);

            }
        });

        effectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PreviewActivity.this,LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                FilterAdapter filterAdapter = new FilterAdapter(getApplicationContext(),imgs,videoView);
             //  FilterAdapter filterAdapter = new FilterAdapter(getApplicationContext(), view.getContext(),imgs,videoView);
                recyclerView.setAdapter(filterAdapter);
            }
        });

        frameimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager llmFilters = new LinearLayoutManager(PreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(llmFilters);
                frameAdapter = new FrameAdapter( getApplicationContext(), FrameAdapter.FrameListener,imgs,videoView);
                recyclerView.setAdapter(frameAdapter);
            }
        });
        String filePath = getIntent().getStringExtra(FILEPATH);
        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                seekBar.setMax(videoView.getDuration());

                seekBar.postDelayed(onEverySecond, 1000);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(stopPosition);

        videoView.start();
    }
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if (videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
                img.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));

            }
           if(!videoView.isPlaying() ) {
               img.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));


           }

        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }



}

