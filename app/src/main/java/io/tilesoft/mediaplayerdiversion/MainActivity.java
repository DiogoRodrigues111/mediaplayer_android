/**
 * Copyright (C) 2021  Diogo Rodrigues Roessler
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
 *
 * <b>DON'T NOT DO CHANGE - Diogo Rodrigues Roessler ( 2021 )</b>
 */

package io.tilesoft.mediaplayerdiversion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;    // debug
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.Config.DebugOnly;
import io.tilesoft.mediaplayerdiversion.FileSystem.ExternalStorage;
import io.tilesoft.mediaplayerdiversion.Lists.RecycleItemList;
import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public class MainActivity extends AppCompatActivity {

    private Player player;

    // Be careful uses this public globals variables, that MainActivity
    // on others classes.
    public String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int REQUEST_CODES = 1;

    public static Intent CHOOSER_PAGE;

    public MenuView.ItemView loop_button_nav;
    public VideoView videoView;
    public SeekBar sliderDuration;
    public MediaController mediaController;
    public TextView startText;
    public TextView endText;
    public ListView listView;

    public MenuView.ItemView play_button_nav;
    public BottomNavigationView nav_view;

    public ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugOnly.DEBUG_ONLY = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    return;
                }

                requestPermissions(PERMISSIONS, REQUEST_CODES);
            }
        }

        // Find's objects
        play_button_nav = findViewById(R.id.play);
        nav_view = findViewById(R.id.nav_menu_main);
        loop_button_nav = findViewById(R.id.loop);
        startText = findViewById(R.id.text_init_duration);
        endText = findViewById(R.id.text_end_duration);

        sliderDuration = findViewById(R.id.seekbar_main);
        videoView = findViewById(R.id.video_view_main);
        mediaController = new MediaController(this);

        player = new Player(this, videoView, null, sliderDuration, startText, endText);

        listView = findViewById(R.id.external_storage_view);
        arrayAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1, ExternalStorage.ITEM);
        listView.setAdapter(arrayAdapter);
    }

    /**
     * Get Activity result for codes how request, result and open chooser
     * in the case user accepted permission for read external sd card or internal storage.
     *
     * If access is <b>GRANTED</b> then can play video, or others media format compatible
     * with Android Phone.
     *
     * @param requestCode <b>REQUEST_CODES</b>
     * @param resultCode <b>if is result is OK</b>
     * @param data <b>Intent of activity chooser</b>
     */
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
                            fileSelect_ex.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /**
     * Get focus on window, while window is changed.
     *
     * @param hasFocus always true.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * It's get thumbnails of chooser in Sd Card.
     *
     * Examples:
     * <b>Bitmap bmp = MediaStore.Video.Thumbnails.getThumbnail(
     *                                 getContentResolver(), ContentUris.parseId(getData),
     *                                 MediaStore.Video.Thumbnails.MICRO_KIND,
     *                                 (BitmapFactory.Options)null);</b>
     *
     * <b>Bitmap bmp = ThumbnailUtils.createVideoThumbnail(data.getData().getPath(),
     *                                 MediaStore.Video.Thumbnails.MINI_KIND);</b>
     *
     */
    @Nullable
    private Drawable getVideoThumbnails() throws RuntimeException {
        final int videoId = MediaStore.Video.Thumbnails.MICRO_KIND;
        String[] proj = new String[] { MediaStore.Video.Thumbnails.DATA };
        String[] vID  = new String[] { String.valueOf( videoId ) };
        ContentResolver contentResolver = getContentResolver();

        @SuppressLint("Recycle") // Recycle for a while
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                proj,
                MediaStore.Video.Thumbnails.VIDEO_ID,
                vID, null);

        return Drawable.createFromPath(cursor.getString(0));
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
     *
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
     * Play button in navigation
     *
     * @param item loop_button_nav <b>self</b>
     */
    public void Loop_OnClick(MenuItem item) {
        player.loop(item);
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

    /**
     * Open an new file system for chooser files.
     *
     * It is an simulation button <p>event</p> clink.
     *
     * @param item self
     */
    public void openExternalStorage(MenuItem item) {

        File file = ExternalStorage.getFileLists("/storage/1F08-2107/Videos");

    }
}