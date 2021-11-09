package io.tilesoft.mediaplayerdiversion.Lists;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecycleItemList {

    public String path;
    public String files;

    public RecycleItemList(@NonNull String path, @Nullable String files) {
        this.path = path;
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    @NonNull
    @Override
    public String toString() {
        return path + "/" + files;
    }
}
