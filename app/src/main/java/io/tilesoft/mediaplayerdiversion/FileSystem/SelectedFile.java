package io.tilesoft.mediaplayerdiversion.FileSystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;

import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public class SelectedFile implements SelectedFileIntface {

  private transient boolean canPlayFile;

  /**
   * Check if file is selected and then send to <b>makeOpenFile</b>
   * @param context self
   * @param intent file
   */
  @Override
  public void checkIfSelectedFileInChooser(Context context, Intent intent) {
    intent = new Intent(Intent.ACTION_GET_CONTENT);

    for(File selectorFile : Environment.getExternalStorageDirectory().listFiles()) {
      Uri filePath = Uri.fromFile(
              new File(Environment.getExternalStorageDirectory().getPath() + selectorFile));

      if(!selectorFile.isFile()) errorMessage(
              context.getApplicationContext(),
              "Failed is not file", "Warning this is not a file \n please select an file");

      try {
        FileInputStream readerFile = new FileInputStream(filePath.getPath());
        readerFile.read();

        if(!readerFile.getChannel().isOpen()) errorMessage(
                context.getApplicationContext(),
                "Failed file is not opened", "Error to open");

        if(readerFile.read() != 0) canPlayFile = true;
      } catch(Exception fileNotFound_ex) {
        errorMessage(
                context.getApplicationContext(),
                "EXCEPTION FAILED", fileNotFound_ex.getMessage());
      }
    }
  }

  /**
   * Make serialize file
   * @param context self
   * @param intent file
   * @param uri path
   */
  @Override
  public void makeOpenFile(Context context, Intent intent, Uri uri) {

  }

  /**
   * Send file to <b>player</b>
   * @param context self
   * @param intent file
   * @param player self
   */
  @Override
  public void redirect(Context context, Intent intent, Player player) {

  }

  /**
   * Play new instance of file
   * @param context self
   */
  @Override
  public void canPlay(Context context) {

  }

  /**
   * Error message
   * @param context self
   * @param title title
   * @param message error message
   */
  @Override
  public void errorMessage(Context context, CharSequence title, CharSequence message) {
    AlertDialog.Builder b = new AlertDialog.Builder(context.getApplicationContext());
    b.setTitle(title);
    b.setMessage(message);
    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
      }
    });
    b.create();
    b.show();
  }
}
