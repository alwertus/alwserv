package ru.alwertus.alwserv.management_apps;

import lombok.Getter;

public class LinuxProcessData {
    @Getter
    private String title;

    public LinuxProcess sp;

    public LinuxProcessData(String title, LinuxProcess sp) {
        this.title = title;
        this.sp = sp;
    }

    public String getStatus() {
        return sp.getStatus();
    }

    public boolean isExists() {
        return sp.isExists();
    }

    public int getId() { return title.hashCode(); }

    @Override
    public String toString() {
        return sp.toString();
    }
}
