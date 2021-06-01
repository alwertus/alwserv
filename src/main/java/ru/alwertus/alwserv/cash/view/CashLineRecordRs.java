package ru.alwertus.alwserv.cash.view;

import ru.alwertus.alwserv.cash.model.CashLineRecord;

public class CashLineRecordRs {
    private final CashLineRecord line;

    public CashLineRecordRs(CashLineRecord line) {
        this.line = line;
    }
    public Long getId() { return line.getId(); }
    public String getName() { return line.getName(); }
    public int getSumPlan() { return line.getSumPlan(); }
    public int getSumActual() { return line.getSumActual(); }
    public boolean getCompleted() { return line.getCompleted() != null; }
    public Long getCompletedDate() { return line.getCompleted().getTimeInMillis(); }
    public String getComment() { return line.getComment(); }
    public int getSequence() { return line.getSequence(); }
//    public CashGroupRecord group() { return ; }
}
