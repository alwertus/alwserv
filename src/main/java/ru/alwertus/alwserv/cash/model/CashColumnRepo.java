package ru.alwertus.alwserv.cash.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CashColumnRepo extends CrudRepository<CashColumnRecord, Long> {
    Optional<CashColumnRecord> findBySheetIsAndYearIsAndMonthIsAndPartIs(CashSheetRecord sheet, int year, int month, int part);
}
