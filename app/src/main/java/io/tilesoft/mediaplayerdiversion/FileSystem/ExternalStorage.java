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

    /**
     * Get access to <p><b>List View </b>.</p>
     *
     * And replace end formats extensions from files.
     *
     * @param file <p><b>Environment.getExternalStorageDirectory()</b></p>
     * @return <p>ArrayList of File</p>
     */
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

    /**
     * It's to do showing list all files found in <p><b>External Storage</b>.</p>
     *
     * @param context <p>Context.getApplicationContext()</p>
     */
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
                        context.getApplicationContext(), "External equal null", Toast.LENGTH_LONG)
                        .show();
            else
                Toast.makeText(
                        context.getApplicationContext(),
                        "External differ then null", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get reader to external storage
     *
     * This take name files from location of referencable absolute path's.
     *
     * For example:
     *
     *  /storage/1F08-2107/
     *  /storage/emulated/0
     *  ...
     *
     * @return <p><b>Self file, but not array list</b></p>
     */
    public static File getFileLists( String absolutePath ) {

        File file = new File( absolutePath );
        File[] fi = file.listFiles();

        for ( File iterator : fi ) {
            System.out.println(iterator.getName());
        }

        return file;
    }
}
