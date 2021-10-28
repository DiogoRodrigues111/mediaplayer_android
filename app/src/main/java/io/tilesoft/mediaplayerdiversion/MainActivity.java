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

package io.tilesoft.mediaplayerdiversion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.tilesoft.mediaplayerdiversion.FileSystem.SelectedFile;
import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public class MainActivity extends AppCompatActivity {

    public String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public final int REQUEST_CODES = 1;

    private Player player;

    public static Intent CHOOSER_PAGE;

    // Objects find
    private MenuView.ItemView play_button_nav;
    private BottomNavigationView nav_view;
    private MenuView.ItemView loop_button_nav;
    private VideoView videoView;
    private SeekBar sliderDuration;
    private MediaController mediaController;
    private TextView startText;
    private TextView endText;

    private transient boolean isLooping;
    private transient boolean isFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    return;
                }

                requestPermissions(PERMISSIONS, REQUEST_CODES);
            }
        }

        isLooping = false;
        isFullScreen = false;

        // find objects
        play_button_nav = (MenuView.ItemView) findViewById(R.id.play);
        nav_view = (BottomNavigationView) findViewById(R.id.nav_menu_main);
        loop_button_nav = (MenuView.ItemView) findViewById(R.id.loop);
        startText = (TextView) findViewById(R.id.text_init_duration);
        endText = (TextView) findViewById(R.id.text_end_duration);

        // Initialize Player Class
        // Find VideoView
        // Initialize MediaCpntroller
        sliderDuration = (SeekBar) findViewById(R.id.seekbar_main);
        videoView = (VideoView) findViewById(R.id.video_view_main);
        mediaController = new MediaController(this);
        player = new Player(this, videoView, null, sliderDuration, startText, endText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODES:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri getData = data.getData();
                        try {
                            player.getVideoViewPath(getData);
                        } catch (Exception fileSelect_ex) {
                            SelectedFile.errorMessageFromSelectedFile(
                                    this, "Failed to load file", fileSelect_ex.getMessage());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            //navigationBar();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * Open external sdcard filesystem
     * <b>You can see in <i>nav_menu.xml</i></b>
     *
     * @param item null
     */
    public void FilesChooser_OnClick(MenuItem item) {
        String title = getResources().getString(R.string.filechooser_string);

        CHOOSER_PAGE = new Intent(Intent.ACTION_GET_CONTENT);
        CHOOSER_PAGE.setType("*/*");

        Intent i = Intent.createChooser(CHOOSER_PAGE, title);

        try {
            startActivityForResult(i, 1);
        } catch (ActivityNotFoundException start_ac_ex) {
            start_ac_ex.printStackTrace();
        }
    }

    /**
     * Play button in navigation
     * @param item null
     */
    public void PlayNavVideoView_OnClick(MenuItem item) {
        if (!player.videoView.isPlaying()) {
            player.play();
            play_button_nav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_24));
            play_button_nav.setTitle("play");
        } else if (player.videoView.isPlaying()) {
            player.pause();
            play_button_nav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pause_24));
            play_button_nav.setTitle("pause");
        }
    }

    /**
     * Navigation menu
     */
    @Deprecated
    private void navigationBar() {
        if (isFullScreen) {
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isFullScreen = false;
                    player.fullscreenShowVisibility(MainActivity.this);
                    nav_view.setVisibility(View.VISIBLE);
                }
            });
        } else {
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isFullScreen = true;
                    player.fullscreenHideVisibility(MainActivity.this);
                    nav_view.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * Play button in navigation
     * @param item null
     */
    public void Loop_OnClick(MenuItem item) {
        if(!item.isChecked()) {
            player.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.play();
                    item.setChecked(true);
                    Toast.makeText(MainActivity.this, "CHEKCED", Toast.LENGTH_LONG).show();
                }
            });
        } else if (item.isChecked()) {
            player.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    item.setChecked( false );
                    Toast.makeText(MainActivity.this, "NO CHEKCED", Toast.LENGTH_LONG).show();
                    player.pause();
                }
            });
        }
    }

    /**
     * Show and Hide's
     * Get visibility of the component's.
     *
     * @param event self
     * @return  super class
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            player.fullscreenHideVisibility(this);
            nav_view.setVisibility(View.GONE);
        } else {
            if(event.getAction() == MotionEvent.ACTION_MOVE) {
                player.fullscreenShowVisibility(this);
                nav_view.setVisibility(View.VISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }
}