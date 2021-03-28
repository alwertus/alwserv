package ru.alwertus.alwserv.fins.sheet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alwertus.alwserv.auth.User;

import java.util.List;
import java.util.Optional;

public interface FinSheetAccessRepo extends CrudRepository<FinSheetAccess, Long> {
    @Query("select t from FinSheetAccess t where t.user = :u")
    List<FinSheetAccess> findAllByUser(@Param("u") User user);

    @Query("select t from FinSheetAccess t where t.sheet = :sheet")
    List<FinSheetAccess> findAllBySheet(@Param("sheet") FinSheet sheet);

    @Query("select t from FinSheetAccess t where t.sheet = :sheet and t.user = :user")
    Optional<FinSheetAccess> findByUserAndSheet(@Param("sheet") FinSheet sheet, @Param("user") User user);
}