package com.aregcraft.pets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.logging.Level;

public class Updater {
    private static final String META_URL = "https://raw.githubusercontent.com/Aregcraft/pets/master/meta.json";
    private static final String JAR_URL = "https://github.com/Aregcraft/pets/releases/download/v%s/pets-%1$s.jar";
    private static final String JAR_PATH = "pets-%s.jar";
    private static final String DOWNLOAD_ERROR = "An error occurred while trying to update the plugin!";
    private static final String DELETE_WARNING = "Unable to delete the old version! Please do it manually.";
    private static final String SUCCESS_INFO = "Successfully updated the plugin! Please restart the server.";
    private static final String ALREADY_LATEST_INFO = "Already using the latest version of the plugin!";

    private final Pets plugin;

    public Updater(Pets plugin) {
        this.plugin = plugin;
    }

    public void tryDownloadLatestVersion() {
        var currentVersion = plugin.getDescription().getVersion();
        var latestVersion = getLatestVersion();
        var logger = plugin.getLogger();
        if (currentVersion.equals(latestVersion)) {
            logger.log(Level.INFO, ALREADY_LATEST_INFO);
            return;
        }
        try (var reader = new URL(JAR_URL.formatted(latestVersion)).openStream();
             var channel = new FileOutputStream(getJar(latestVersion)).getChannel()) {
            channel.transferFrom(Channels.newChannel(reader), 0, Long.MAX_VALUE);
            logger.log(Level.INFO, SUCCESS_INFO);
        } catch (IOException e) {
            logger.log(Level.SEVERE, DOWNLOAD_ERROR);
        }
        if (!getJar(currentVersion).delete()) {
            plugin.getLogger().log(Level.WARNING, DELETE_WARNING);
        }
    }

    private String getLatestVersion() {
        try (var reader = new InputStreamReader(new URL(META_URL).openStream())) {
            return plugin.getGson().fromJson(reader, Meta.class).version;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getJar(String version) {
        return new File(plugin.getDataFolder().getParentFile(), JAR_PATH.formatted(version));
    }

    private static class Meta {
        private String version;
    }
}
