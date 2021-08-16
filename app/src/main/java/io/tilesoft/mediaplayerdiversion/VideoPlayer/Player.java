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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.FileSystem.SelectedFile;

public class Player implements PlayerIntface {

  public static transient boolean canPlayVideo;
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
    if(Player.canPlayVideo)
      videoView.start();
  }

  public void noCheckAndPlay() {
    videoView.start();
  }

  /**============================================================================
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

  /**============================================================================
   * Simples checkout if work, that read file
   * @param context self
   */
  public void checkIfSelectionItWork(Context context) {
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
  @Override
  public void newInstanceFromVideoView(@NonNull Context context) {
    initializeVideoViewForPlay(context.getApplicationContext());
    if(Player.canPlayVideo) play();
  }

  public void getVideoViewPath(Context context, Uri uri) {
    videoView.setVideoURI(uri);
    noCheckAndPlay();
  }
}
