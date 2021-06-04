package ru.alwertus.alwserv.todo;

import ru.alwertus.alwserv.auth.User;

public class TodoRecordRs {
    private final TodoRecord record;
    private final User currentUser;

    public TodoRecordRs(TodoRecord record, User currentUser) {
        this.record = record;
        this.currentUser = currentUser;
    }

    public Long getId() { return record.getId(); }

    public String getDescription() { return record.getDescription(); }

    public String getDecision() { return record.getDecision(); }

    public String getRequestor() { return record.getRequestor() == null
            ? null
            : record.getRequestor().getFirstName() + " " + record.getRequestor().getLastName(); }

    public boolean getIsRequestorCurrent() { return currentUser.equals(record.getRequestor()); }

    public String getExecutor() { return record.getExecutor() == null
            ? null
            : record.getExecutor().getFirstName() + " " + record.getExecutor().getLastName(); }

    public boolean getIsExecutorCurrent() { return currentUser.equals(record.getExecutor()); }

    public Long getCreated() { return record.getCreated().getTimeInMillis(); }

    public Long getCompleted() { return record.getCompleted().getTimeInMillis(); }
}
