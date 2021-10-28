/**
 * Copyright (C) 2020  Diogo Rodrigues Roessler
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * DONT NOT DO CHANGE
 */
/**
 * DONT NOT DO CHANGE
 */

package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.FileSystem.SelectedFile;
import io.tilesoft.mediaplayerdiversion.MainActivity;

public class Player implements PlayerIntface {

    public static transient boolean canPlayVideo;
    public VideoView videoView;
    public MediaController mediaController;
    public SeekBar slider;
    private double currentPos;
    private Handler handler;
    private TextView startText;
    private TextView endText;

    private transient boolean isLooping;

    private Context context;

    public Player(@NonNull Context context,
                  @NonNull VideoView videoView,
                  MediaController mediaController,
                  @NonNull SeekBar slider,
                  @NonNull TextView startText,
                  @NonNull TextView endText) {
        this.context = context;

        this.mediaController = new MediaController(context.getApplicationContext());
        this.mediaController = mediaController;

        this.videoView = new VideoView(context.getApplicationContext());
        this.videoView = videoView;
        this.videoView.setMediaController(mediaController);

        this.slider = new SeekBar(context.getApplicationContext());
        this.slider = slider;

        this.startText = new TextView(context.getApplicationContext());
        this.endText = new TextView(context.getApplicationContext());
        this.startText = startText;
        this.endText = endText;

        handler = new Handler();

        isLooping = false;
    }

    /**
     * Check if notification it accepted
     *
     * @param check <b>simple only passage PackageManager.PERMISSION_GRANTED</b>
     * @return Check if accepted and then can read
     */
    @Override
    public boolean checkIfNotificationAccept(@NonNull Context context, int check) {
        check = PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                check) Player.canPlayVideo = true;

        return Player.canPlayVideo;
    }

    /**
     * Simple reader for external sdcard.
     * <b>That is only test</b>
     *
     * @param context self
     */
    public void simpleReadExternalSdCard(Context context) {
        checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        File env = Environment.getExternalStorageDirectory().getAbsoluteFile();
        if (Player.canPlayVideo) {
            if (Environment.getExternalStorageDirectory().getAbsoluteFile().canRead()) {
                Uri uriPath = Uri.fromFile(new File(env.toURI())); // filepath
                videoView.setVideoURI(uriPath);
            }
        } else {
            SelectedFile.errorMessageFromSelectedFile(
                    context.getApplicationContext(),
                    "Failed to 'CanPlayVideo'", "Failed to CanPlayVideo");
        }
    }

    /**
     * Open and send uri with file to video view
     * But it do not check if notification is accepted in <b>checkIfNotificationAccept</b>
     */
    private void readFileFromSdCard() {
        File fp = Environment.getExternalStorageDirectory().getAbsoluteFile();
        Uri uri = Uri.fromFile(new File(fp.toURI()));
        if (Environment.getExternalStorageDirectory().canRead()) videoView.setVideoURI(uri);
    }

    /**
     * Play <b>VideoView</b>
     */
    public void play() {
        videoView.start();
    }

    /**
     * Pause <b>VideoView</b>
     */
    public void pause() {
        videoView.pause();
    }

    public void stop() {
        videoView.pause();
    }

    /**
     * No check, and then play
     */
    @Deprecated
    public void noCheckAndPlay() {
        videoView.start();
    }

    /**
     * Check if notification is accepted and then <b>make simple read external sdcard</b>
     *
     * @param context self
     * @return Check if accepted and then can read and read sdcard
     */
    @Override
    public boolean initializeVideoViewForPlay(@NonNull Context context) {
        checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);
        simpleReadExternalSdCard(context.getApplicationContext()); // simple test
        return false;
    }

    /**
     * Simples checkout if work, that read file
     *
     * @param context self
     */
    public void checkIfSelectionItWork(Context context) {
        initializeVideoViewForPlay(context.getApplicationContext());
        SelectedFile fp = new SelectedFile();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        fp.checkIfSelectedFileInChooser(context.getApplicationContext(), intent);
        //play();
    }

    /**
     * <b><i>That create new instance for VideoView</i></b>
     *
     * @param context self
     */
    @Override
    public void newInstanceFromVideoView(@NonNull Context context) {
        initializeVideoViewForPlay(context.getApplicationContext());
        if (Player.canPlayVideo) play();
    }

    /**
     * Get video
     *
     * @param uri     self
     */
    public void getVideoViewPath(@NonNull Uri uri) {
        videoView.setVideoURI(uri);
        durationVideo(slider, videoView);
        play();
    }

    /**
     * Time conversion for label
     *
     * @param value duration
     * @return duration
     */
    public String endTimeConvertion(int value) {
        int min = value / 1000 / 60;
        int sec = value / 1000 % 60;
        String time = "";

        time += min + ":";
        if (sec <= 10) time += "0";
        time += sec;

        return time;
    }

    /**
     * Get media duration
     * @param gD Media Duration
     */
    private void endCountLabel(int gD) {
        endText.setText(endTimeConvertion(gD));
    }

    /**
     * Time conversion for label
     *
     * @param value duration
     * @return Time variable from duration
     */
    public String startTimeConversion(int value) {
        int min = value / 1000 / 60;
        int sec = value / 1000 % 60;
        String time = "";

        time += min + ":";
        if (sec <= 10) time += "0";
        time += sec;

        return time;
    }

    /**
     * Start label counter
     */
    private void startCountLabel() {
        int current = videoView.getCurrentPosition();
        startText.setText(startTimeConversion(current));
    }

    /**
     * Duration Slider
     * @param sl      self
     * @param video   self
     */
    public void durationVideo(
            @NonNull SeekBar sl,
            @NonNull VideoView video) {
        currentPos = video.getCurrentPosition();
        int delay = 1;
        sl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sl.setMax(video.getDuration());
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        seekBar.setProgress(0);
                    }
                });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentPos = seekBar.getProgress();
                video.seekTo((int) currentPos);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sl.setMax(video.getDuration());
                sl.setProgress(video.getCurrentPosition());
                startCountLabel();
                endCountLabel(sl.getMax());
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    /**
     * Make conversion of strings format.
     *
     * @param tseq
     */
    private void startLabel(int tseq) {
        startTimeConversion(tseq);
    }

    /**
     * Make conversion of strings format.
     *
     * @param tseq
     */
    private void endLabel(int tseq) {
        endTimeConvertion(tseq);
    }

    /**
     * Hide Visibility system
     *
     * @param window  self
     */
    public void fullscreenHideVisibility(@NonNull MainActivity window) {
        View d = window.getWindow().getDecorView();
        d.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    /**
     * Show Visibility system
     *
     * @param window  self
     */
    public void fullscreenShowVisibility(@NonNull MainActivity window) {
        View d = window.getWindow().getDecorView();
        d.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        );
    }
}
