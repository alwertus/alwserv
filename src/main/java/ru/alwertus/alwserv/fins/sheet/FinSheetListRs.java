package ru.alwertus.alwserv.fins.sheet;

public class FinSheetListRs {
    private final FinSheetAccess access;

    public FinSheetListRs(FinSheetAccess access) {
        this.access = access;
    }

    public Long getId() {return access.getSheet().getId();}
    public String getName() {return access.getSheet().getName();}
    public String getAccess() {return access.getAccess().toString();}
}
