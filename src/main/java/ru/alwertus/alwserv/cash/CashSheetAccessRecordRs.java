package ru.alwertus.alwserv.cash;

public class CashSheetAccessRecordRs {
    private final CashSheetAccessRecord accessRecord;

    public CashSheetAccessRecordRs(CashSheetAccessRecord accessRecord) {
        this.accessRecord = accessRecord;
    }

    public Long getId() {
        return accessRecord.getId();
    }

    public String getAccess() {
        return accessRecord.getAccess().toString();
    }

    public Long getSheetId() {
        return accessRecord.getCashSheet().getId();
    }

    public String getSheetName() {
        return accessRecord.getCashSheet().getName();
    }

    public int getColumnCount() {
        return accessRecord.getCashSheet().getColumnCount();
    }

    public boolean isActive() {
        return accessRecord.getCashSheet().getId()
                .equals(accessRecord.getUser().getFinSheetId());
    }
}
