package io.tilesoft.mediaplayerdiversion.FileSystem;

import android.content.Context;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import io.tilesoft.mediaplayerdiversion.Config.DebugOnly;

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

    public void displayExternalStorage( Context context ) {

        ArrayList<File> displayRoutine = getStorageFiles (
                // Get reader external storage files.
                Environment.getExternalStorageDirectory()
        );

        String[] item = new String[displayRoutine.size()];

        for(int i=0; i < displayRoutine.size(); i++) {
            item[i] = displayRoutine.get(i).getName().replace(".*", "");
        }

        ITEM = item;

        if(DebugOnly.DEBUG_ONLY == 1) {
            if(ITEM == null)
                Toast.makeText(
                        context.getApplicationContext(), "External equal null", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(
                        context.getApplicationContext(),
                        "External differ then null", Toast.LENGTH_LONG).show();
        }
    }
}
