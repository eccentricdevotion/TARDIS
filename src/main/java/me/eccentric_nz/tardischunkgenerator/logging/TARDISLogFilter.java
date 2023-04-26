package me.eccentric_nz.tardischunkgenerator.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.ChatColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TARDISLogFilter implements Filter {

    private final String path;
    private final List<String> filters = new ArrayList<>();
    private boolean clean = true;

    public TARDISLogFilter(String path) {
        this.path = path;
        filters.add("TARDIS");
        filters.add("tardis");
        filters.add("me.eccentric_nz");
        filters.add("Caused by:");
    }

    public Result checkMessage(String message) {
        for (String filter : filters) {
            if (message.contains(filter)) {
                writeToFile(ChatColor.stripColor(message));
                break;
            }
        }
        return Result.NEUTRAL;
    }

    private void writeToFile(String message) {
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        try {
            if (clean) {
                fileWriter = new FileWriter(path); // overwrite
                clean = false;
            } else {
                fileWriter = new FileWriter(path, true); // true to append
            }
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return checkMessage(TextUtils.getStacktrace(throwable, true, "me.eccentric_nz."));
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return checkMessage(TextUtils.getStacktrace(throwable, true, "me.eccentric_nz."));
    }

    @Override
    public Result filter(LogEvent logEvent) {
        if (logEvent.getThrown() != null) {
            return checkMessage(TextUtils.getStacktrace(logEvent.getThrown(), true, "me.eccentric_nz."));
        }
        return checkMessage(logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public State getState() {
        try {
            return State.STARTED;
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}
