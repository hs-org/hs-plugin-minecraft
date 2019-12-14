package me.devnatan.hsorg.plugin.mc;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class HSLogger extends Logger {

    private final String prefix;

    HSLogger(String prefix, Logger parent) {
        super(parent.getName(), parent.getResourceBundleName());
        setParent(parent);
        setLevel(Level.ALL);
        this.prefix = prefix;
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage("[" + prefix + "/" + Thread.currentThread().getName() + "] " + logRecord.getMessage());
        super.log(logRecord);
    }

}
