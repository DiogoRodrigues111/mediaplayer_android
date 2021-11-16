/**
 * Copyright (C) 2021  Diogo Rodrigues Roessler
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * DON'T NOT DO CHANGE - Diogo Rodrigues Roessler ( 2021 )
 */

package io.tilesoft.mediaplayerdiversion.VideoPlayer;

import android.content.Context;
import android.widget.VideoView;

public interface PlayerIntface {
  boolean checkIfNotificationAccept (Context context, int check);
  boolean initializeVideoViewForPlay(Context context);
  void    newInstanceFromVideoView  (Context context);
}
