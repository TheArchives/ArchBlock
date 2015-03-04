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

public class Utils {
    public static UUID fetchUuid(String username) {
        Gson gson = new Gson();
        InputStream in = null;

        try {
            in = new URL(String.format(
                    "https://api.mojang.com/users/profiles/minecraft/%s", username
            )).openStream();

            String urlData = IOUtils.toString(in);

            Map jsonData = gson.fromJson(urlData, Map.class);
            String id = (String) jsonData.get("id");

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
}
