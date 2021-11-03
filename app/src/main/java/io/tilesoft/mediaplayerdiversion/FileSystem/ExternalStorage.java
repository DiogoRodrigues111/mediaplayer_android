package io.tilesoft.mediaplayerdiversion.FileSystem;

import android.content.Context;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class ExternalStorage {

    public static String[] ITEM = { };

    public ArrayList<File> getStorageFiles(File file) {
        ArrayList<File> addInputArray = new ArrayList<>();
        File[] files = file.listFiles();

        for(File fi : files) {

            boolean check = fi.isDirectory() && !fi.isHidden();
            boolean replaceExt = fi.getName().endsWith(".*");

            if(check) {
                addInputArray.addAll(getStorageFiles(fi));
            } else {
                if(replaceExt) {
                    addInputArray.add(fi);
                }
            }
        }

        return addInputArray;
    }

    public void displayExternalStorage() {

        ArrayList<File> displayRoutine = getStorageFiles (
                // Get reader external storage files.
                Environment.getExternalStorageDirectory()
        );

        String[] item = new String[displayRoutine.size()];

        for(int i=0; i < displayRoutine.size(); i++) {
            item[i] = displayRoutine.get(i).getName().replace(".*", "");
        }

        ITEM = item;
    }
}
