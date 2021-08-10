package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;

public class Player implements PlayerIntface {

  transient boolean canPlayVideo;
  public  VideoView videoView;
  public  MediaController mediaController;

  public Player(@NonNull Context context,
                @NonNull VideoView videoView,
                @NonNull MediaController mediaController)
  {
    this.mediaController = new MediaController(context.getApplicationContext());
    this.mediaController = mediaController;

    this.videoView = videoView;
    this.videoView.setMediaController(mediaController);
  }

  /**
   * Check if notification it accepted
   * @param check <b>simple only passage PackageManager.PERMISSION_GRANTED</b>
   * @return Check if accepted and then can read
   */
  @Override
  public boolean checkIfNotificationAccept(@NonNull Context context, int check) {
    check = PackageManager.PERMISSION_GRANTED;

    if(ContextCompat.checkSelfPermission(context.getApplicationContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE ) ==
            check)  canPlayVideo = true;

    return canPlayVideo;
  }

  /**
   * Simple reader for external sdcard.
   * <b>That is only test</b>
   * @param context self
   */
  public void simpleReadExternalSdCard(@NonNull Context context) {
    checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);

    if(canPlayVideo) {
      Uri uriPath = Uri.fromFile(new File("/sdcard/Videos/1.mp4")); // fix hardcode
      videoView.setVideoURI(uriPath);
    }
  }

  /**
   * Play <b>VideoView</b>
   */
  public void play() {
    if(canPlayVideo)
      videoView.start();
  }

  /**
   * Check if notification is accepted and then <b>make simple read external sdcard</b>
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
   * <b><i>That create new instance for VideoView</i></b>
   * @param context self
   */
  @Override
  public void newInstanceFromVideoView(@NonNull Context context) {
    initializeVideoViewForPlay(context.getApplicationContext());
    if(canPlayVideo) play();
  }
}
