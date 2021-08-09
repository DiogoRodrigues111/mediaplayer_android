package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.content.Context;
import android.widget.VideoView;

public interface PlayerIntface {
  boolean checkIfNotificationAccept (Context context, int check);
  boolean initializeVideoViewForPlay(Context context);
  void    newInstanceFromVideoView  (Context context);
}
