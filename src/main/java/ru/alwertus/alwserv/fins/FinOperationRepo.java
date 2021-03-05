package ru.alwertus.alwserv.fins;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alwertus.alwserv.auth.User;

import java.util.Date;
import java.util.List;

public interface FinOperationRepo extends CrudRepository<FinOperation, Long> {
    @Query("select t from FinOperation t where t.creator=:u and (t.plannedDate between :d1 and :d2)")
    List<FinOperation> findAll(@Param("u") User user,
                               @Param("d1") Date d1,
                               @Param("d2") Date d2);

//    @Query("select t from FinOperation t where t.creator=:u")
//    List<FinOperation> findAll(@Param("u") User user);
}
