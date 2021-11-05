package io.tilesoft.mediaplayerdiversion.VideoPlayer;

/**
 * An abstraction for uses into the Player class
 *
 * It's can uses how to do event's.
 */
public abstract class PlayerFunctionInfo {
    public abstract void onPlay();
    public abstract void onPause();
    public abstract void onStop();
}
