package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.MainActivity;
import io.tilesoft.mediaplayerdiversion.R;

public class Player implements PlayerIntface {

  transient boolean canPlayVideo;
  public  static VideoView videoView;
  private static MediaController mediaController;

  public Player(@NonNull Context context,
                @NonNull VideoView videoView,
                @NonNull MediaController mediaController)
  {
    this.mediaController = new MediaController(context.getApplicationContext());
    this.videoView = videoView;
    this.mediaController = mediaController;

    this.videoView.setMediaController(mediaController);
  }

  /**
   * Check if notification it accepted
   * @param check <b>simple only passage PackageManager.PERMISSION_GRANTED</b>
   * @return
   */
  @Override
  public boolean checkIfNotificationAccept(@NonNull Context context, @NonNull int check) {
    check = PackageManager.PERMISSION_GRANTED;

    if(ContextCompat.checkSelfPermission(context.getApplicationContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE ) ==
            check)  canPlayVideo = true;

    return canPlayVideo;
  }

  public void simpleReadExternalSdCard(@NonNull Context context) {
    checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);

    if(canPlayVideo) {
      Uri uriPath = Uri.fromFile(new File("/sdcard/Videos/1.mp4"));
      videoView.setVideoURI(uriPath);
    }
  }

  public void play() {
    if(canPlayVideo)
      videoView.start();
  }

  @Override
  public boolean initializeVideoViewForPlay(@NonNull Context context) {
    checkIfNotificationAccept(context.getApplicationContext(), PackageManager.PERMISSION_GRANTED);
    simpleReadExternalSdCard(context.getApplicationContext()); //test
    return false;
  }

  @Override
  public void newInstanceFromVideoView(@NonNull Context context) {
    initializeVideoViewForPlay(context.getApplicationContext());
    if(canPlayVideo) play();
  }
}
