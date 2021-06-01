package ru.alwertus.alwserv.cash.view;

import ru.alwertus.alwserv.cash.model.CashGroupRecord;

import java.util.List;
import java.util.stream.Collectors;

public class CashGroupRecordRs {

    private final CashGroupRecord record;

    public CashGroupRecordRs(CashGroupRecord record) {
        this.record = record;
    }

    public Long getId() {return record.getId();}
    public int getSumLimit() {return record.getSumLimit();}
    public int getSequence() {return record.getSequence();}
    public char getSign() {return record.getSign();}
    public String getName() {return record.getName();}
    public List<CashLineRecordRs> getLines() {
        return record
                .getLines()
                .stream()
                .map(CashLineRecordRs::new)
                .collect(Collectors.toList());}
    //    private CashColumnRecord column;
}
