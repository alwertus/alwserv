package ru.alwertus.alwserv.cash.model;

import org.springframework.data.repository.CrudRepository;

public interface CashSheetRepo extends CrudRepository <CashSheetRecord, Long> {
}
