package com.archivesmc.archblock.utils;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A general utility class with several useful static method
 */
public class Utils {
    /**
     * Attempt to fetch a UUID for a player from Mojang
     *
     * @param username The username to look up
     * @return The UUID of the player, or null when
     */
    public static UUID fetchUuid(String username) {
        Gson gson = new Gson();
        InputStream in = null;

        try {
            in = new URL(String.format(
                    "https://api.mojang.com/users/profiles/minecraft/%s", username
            )).openStream();

            String urlData = IOUtils.toString(in);

            Map jsonData = gson.fromJson(urlData, Map.class);

            if (jsonData == null) {
                return null;
            }

            Object idObject = jsonData.get("id");

            if (idObject == null) {
                return null;
            }

            String id = (String) idObject;

            return UUID.fromString(  // Of course, Mojang couldn't send actual UUIDs, that'd be too easy
                    id.substring(0 ,  8) + "-"
                  + id.substring(8 , 12) + "-"
                  + id.substring(12, 16) + "-"
                  + id.substring(16, 20) + "-"
                  + id.substring(20, 32)
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }

        return null;
    }

    /**
     * Given a File object that represents a directory, return a List of File objects that represent
     * subdirectories
     *
     * @param dir The containing directory to list subdirectories for
     * @return A list containing all subdirectories
     */
    public static List<File> listDirectories(File dir) {
        ArrayList<File> directories = new ArrayList<>();

        if (! (dir.exists() || dir.isDirectory()) ) {
            return directories;
        }

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                directories.add(f);
            }
        }

        return directories;
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, "utf8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
