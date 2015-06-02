package com.archivesmc.archblock.wrappers;

public interface Logger {
    void info(String message);
    void warning(String message);
    void error(String message);
    void critical(String message);

    Object getWrapped();
}
