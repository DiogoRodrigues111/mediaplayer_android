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

package io.tilesoft.mediaplayerdiversion.FileSystem;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.File;

import io.tilesoft.mediaplayerdiversion.MainActivity;
import io.tilesoft.mediaplayerdiversion.VideoPlayer.Player;

public class SelectedFile implements SelectedFileIntface {

  public static ContentResolver contentResolver;

  /**
   * Check if file is selected and then send to <b>makeOpenFile</b>
   *
   * @param context self
   * @param intent  file
   */
  @Override
  public void checkIfSelectedFileInChooser(
          @NonNull Context context,
          @NonNull Intent intent)
  {
    try {
      Uri filePath = Uri.fromFile(
              new File(Environment.getExternalStorageDirectory().getPath()));

      contentResolver.openInputStream(filePath);

      if(contentResolver == null) errorMessage(
              context.getApplicationContext(),
              "ContentResolver equal NULL", "Failed to open and read file");

      } catch(Exception fileNotFound_ex) {
        errorMessage(
                context.getApplicationContext(),
                "EXCEPTION FAILED", fileNotFound_ex.getMessage());
      }
  }

  /**
   * Make serialize file
   *
   * @param context self
   * @param intent  file
   * @param uri     path
   */
  @Override
  public void makeOpenFile(@NonNull Context context, @NonNull Intent intent, @NonNull Uri uri) {
  }

  /**
   * Send file to <b>player</b>
   *
   * @param context self
   * @param intent  file
   * @param player  self
   */
  @Override
  public void redirect(
          @NonNull Context context,
          @NonNull Intent intent,
          @NonNull Player player)
  {
  }

  /**
   * Play new instance of file
   *
   * @param context self
   */
  @Override
  public void canPlay(@NonNull Context context) {
  }

  /**
   * Error message
   *
   * @param context self
   * @param title   title
   * @param message error message
   */
  @Override
  public void errorMessage(
          @NonNull Context context,
          @NonNull CharSequence title,
          @NonNull CharSequence message)
  {
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

  /**
   * Static error message
   * @param context self
   * @param title   title
   * @param message message
   */
  public static void errorMessageFromSelectedFile(
          @NonNull Context context,
          @NonNull CharSequence title,
          @NonNull CharSequence message) {
    AlertDialog.Builder b = new AlertDialog.Builder(context.getApplicationContext());
    b.setTitle(title);
    b.setMessage(message);
    b.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
    b.create();
    b.show();
  }
}
