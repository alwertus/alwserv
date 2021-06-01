package ru.alwertus.alwserv.cash.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CashGroupRepo extends CrudRepository<CashGroupRecord, Long> {
    List<CashGroupRecord> findAllByColumn(CashColumnRecord column);
}
