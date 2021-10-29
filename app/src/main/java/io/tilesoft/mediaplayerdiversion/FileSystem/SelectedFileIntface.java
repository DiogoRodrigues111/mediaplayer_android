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
