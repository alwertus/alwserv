package ru.alwertus.alwserv.cash;

import ru.alwertus.alwserv.auth.User;

public class UserRs {
    private final User user;
    private final SheetAccessType access;

    public UserRs(User user, SheetAccessType access) {
        this.user = user;
        this.access = access;
    }

    public Long getId() {
        return user.getId();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getAccess() {
        return access.toString();
    }
}