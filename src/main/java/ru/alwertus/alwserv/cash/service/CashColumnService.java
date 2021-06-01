package ru.alwertus.alwserv.cash.service;

import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.cash.model.CashColumnRecord;
import ru.alwertus.alwserv.cash.model.CashColumnRepo;
import ru.alwertus.alwserv.cash.model.CashSheetRecord;

import java.util.Optional;

@Service
public class CashColumnService {
    private final CashColumnRepo columnRepo;

    public CashColumnService(CashColumnRepo columnRepo) {
        this.columnRepo = columnRepo;
    }

    public CashColumnRecord getOrCreate(CashSheetRecord sheet,
                                        int year,
                                        int month,
                                        int part) {
        CashColumnRecord column = columnRepo
                .findBySheetIsAndYearIsAndMonthIsAndPartIs(sheet, year, month, part)
                .orElse(new CashColumnRecord(year, month, part, sheet));

        if (column.getId() == null)
            columnRepo.save(column);

        return column;
    }

    public Optional<CashColumnRecord> find(CashSheetRecord sheet,
                                           int year,
                                           int month,
                                           int part) {
        return columnRepo.findBySheetIsAndYearIsAndMonthIsAndPartIs(sheet, year, month, part);
    }

    public void rename (CashColumnRecord column, String newName) {
        column.setName(newName);
        columnRepo.save(column);
    }

}
