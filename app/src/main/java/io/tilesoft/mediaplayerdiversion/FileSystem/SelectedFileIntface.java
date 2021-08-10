package io.tilesoft.mediaplayerdiversion.FileSystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public interface SelectedFileIntface {
  void checkIfSelectedFileInChooser(Context context, Intent intent);
  void makeOpenFile(Context context, Intent intent, Uri uri);
  void redirect(Context context, Intent intent, Player player);
  void canPlay(Context context);
  void errorMessage(Context context, CharSequence title, CharSequence message);
}
