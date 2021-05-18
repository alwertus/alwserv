package ru.alwertus.alwserv.cash;

import org.springframework.data.repository.CrudRepository;
import ru.alwertus.alwserv.auth.User;

import java.util.List;
import java.util.Optional;

public interface CashSheetAccessRepo extends CrudRepository<CashSheetAccessRecord, Long> {
    Optional<CashSheetAccessRecord> findByUserIsAndCashSheetIs(User user, CashSheetRecord cashSheet);

    List<CashSheetAccessRecord> findAllByUser(User user);

    List<CashSheetAccessRecord> findAllByCashSheet(CashSheetRecord cashSheet);

}