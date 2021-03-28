package ru.alwertus.alwserv.fins.sheet;

public class FinUsersListRs {
    private final FinSheetAccess access;

    public FinUsersListRs(FinSheetAccess access) {
        this.access = access;
    }
    public Long getUserId() {return access.getUser().getId();}
    public String getEmail() {return access.getUser().getEmail();}
    public String getAccess() {return access.getAccess().toString();}

}
