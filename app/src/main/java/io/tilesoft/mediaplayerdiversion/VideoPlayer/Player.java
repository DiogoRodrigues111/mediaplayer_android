/**
 *    Copyright (C) 2020  Diogo Rodrigues Roessler
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
/**
 * DONT NOT DO CHANGE
 */

package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.LocaleList;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStructure;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.google.android.material.slider.Slider;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.FileSystem.SelectedFile;
import io.tilesoft.mediaplayerdiversion.MainActivity;
import io.tilesoft.mediaplayerdiversion.R;

public class Player implements PlayerIntface {

  public static transient boolean canPlayVideo;
  public  VideoView videoView;
  public  MediaController mediaController;
  public  SeekBar slider;
  private double currentPos;
  private Handler handler;

  public Player(@NonNull Context context,
                @NonNull VideoView videoView,
                         MediaController mediaController,
                @NonNull SeekBar slider)
  {
    this.mediaController = new MediaController(context.getApplicationContext());
    this.mediaController = mediaController;

    this.videoView = new VideoView(context.getApplicationContext());
    this.videoView = videoView;
    this.videoView.setMediaController(mediaController);

    this.slider = new SeekBar(context.getApplicationContext());
    this.slider = slider;

    handler = new Handler();
  }

  /**============================================================================
   * Check if notification it accepted
   * @param check <b>simple only passage PackageManager.PERMISSION_GRANTED</b>
   * @return Check if accepted and then can read
   */
  @Override
  public boolean checkIfNotificationAccept(@NonNull Context context, int check) {
    check = PackageManager.PERMISSION_GRANTED;

    if(ContextCompat.checkSelfPermission(context.getApplicationContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) ==
            check) Player.canPlayVideo = true;

    return Player.canPlayVideo;
  }

  /**============================================================================
   * Simple reader for external sdcard.
   * <b>That is only test</b>
   * @param context self
   */
  @Deprecated
  public void simpleReadExternalSdCard(@NonNull Context context) {
    checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    File env = Environment.getExternalStorageDirectory().getAbsoluteFile();
    if(Player.canPlayVideo) {
      if(Environment.getExternalStorageDirectory().getAbsoluteFile().canRead()) {
        Uri uriPath = Uri.fromFile(new File(env.toURI())); // filepath
        videoView.setVideoURI(uriPath);
      }
    } else {
      SelectedFile.errorMessageFromSelectedFile(
              context.getApplicationContext(),
              "Failed to 'CanPlayVideo'", "Failed to CanPlayVideo");
    }
  }

  /**============================================================================
   * Play <b>VideoView</b>
   */
  public void play() {
    videoView.start();
  }

  /**============================================================================
   * Pause <b>VideoView</b>
   */
  public void pause() {
    videoView.pause();
  }

  /**============================================================================
   * No check, and then play
   */
  @Deprecated
  public void noCheckAndPlay() {
    videoView.start();
  }

  /**============================================================================
   * Check if notification is accepted and then <b>make simple read external sdcard</b>
   * @param context self
   * @return Check if accepted and then can read and read sdcard
   */
  @Deprecated
  @Override
  public boolean initializeVideoViewForPlay(@NonNull Context context) {
    checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);
    simpleReadExternalSdCard(context.getApplicationContext()); // simple test
    return false;
  }

  /**============================================================================
   * Simples checkout if work, that read file
   * @param context self
   */
  @Deprecated
  public void checkIfSelectionItWork(@NonNull Context context) {
    initializeVideoViewForPlay(context.getApplicationContext());
    SelectedFile fp = new SelectedFile();
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    fp.checkIfSelectedFileInChooser(context.getApplicationContext(), intent);
    play();
  }

  /**============================================================================
   * <b><i>That create new instance for VideoView</i></b>
   * @param context self
   */
  @Deprecated
  @Override
  public void newInstanceFromVideoView(@NonNull Context context) {
    initializeVideoViewForPlay(context.getApplicationContext());
    if(Player.canPlayVideo) play();
  }

  /**============================================================================
   * Get video
   * @param context self
   * @param uri     self
   */
  public void getVideoViewPath(@NonNull Context context, @NonNull Uri uri) {
    videoView.setVideoURI(uri);
    durationVideo(context.getApplicationContext(), slider, videoView);
    play();
  }

  /**============================================================================
   * Time conversion for label
   * @param value duration
   * @return      duration
   */
  @Deprecated
  public int timeConversion(long value) {
    int dur = (int) value;
    int hrs = (dur / 3600000);
    int mns = (dur / 60000) % 60000;
    int scs = dur % 60000 / 1000;
    return (int)value;
  }

  /**============================================================================
   * Duration Slider
   * @param context self
   * @param sl      self
   */
  public void durationVideo(
          @NonNull Context context,
          @NonNull SeekBar sl,
          @NonNull VideoView video) {
    currentPos = video.getCurrentPosition();
    int delay = 1000;
    sl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        seekBar.setMax(video.getDuration());
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
        video.seekTo((int)currentPos);
      }
    });

    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        sl.setProgress(video.getCurrentPosition());
        handler.postDelayed(this, delay);
      }
    }, delay);
  }

  /**============================================================================
   * Hide Visibility system
   * @param context self
   * @param window  self
   */
  public void fullscreenHideVisibility(@NonNull Context context, @NonNull MainActivity window) {
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

  /**============================================================================
   * Show Visibility system
   * @param context self
   * @param window  self
   */
  public void fullscreenShowVisibility(@NonNull Context context, @NonNull MainActivity window) {
    View d = window.getWindow().getDecorView();
    d.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    );
  }
}
