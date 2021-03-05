package ru.alwertus.alwserv.fins;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alwertus.alwserv.auth.User;

import java.util.Calendar;
import java.util.List;

public interface FinOperationRepo extends CrudRepository<FinOperation, Long> {
    @Query("select t from FinOperation t where t.creator=:u and t.plannedDate >= :d1 and t.plannedDate <= :d2")
    List<FinOperation> findAllBetweenDates(@Param("u") User user,
                               @Param("d1") Calendar d1,
                               @Param("d2") Calendar d2);
}
