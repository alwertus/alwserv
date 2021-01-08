package ru.alwertus.alwserv.management_apps;

public interface LinuxProcess {

    boolean isExists();

    String getStatus();

    int getPID();

    String getCmdline();

    void start();

    void stop();
}
