package ru.alwertus.alwserv.cash.model;

import org.springframework.data.repository.CrudRepository;

public interface CashLineRepo extends CrudRepository<CashLineRecord, Long> {
}
