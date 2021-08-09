package io.tilesoft.mediaplayerdiversion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final int REQUEST_CODES = 1;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                }

                requestPermissions(PERMISSIONS, REQUEST_CODES);
            }
        }

        VideoView videoView = (VideoView)findViewById(R.id.video_view_main);
        MediaController mediaController = new MediaController(this);
        player = new Player(this, videoView, mediaController);
        player.newInstanceFromVideoView(this);
    }
}