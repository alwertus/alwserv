package ru.alwertus.alwserv.fins;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

public interface FinOperationRepo extends CrudRepository<FinOperation, Long> {
//    @Query("select t from FinOperation t where t.creator=:u and t.plannedDate >= :d1 and t.plannedDate <= :d2 and t.sheet = :sheetId")
    @Query("select t from FinOperation t where t.plannedDate >= :d1 and t.plannedDate <= :d2 and t.sheet = :sheetId")
    List<FinOperation> findAllBetweenDates(//@Param("u") User user,
                               @Param("sheetId") Long sheetId,
                               @Param("d1") Calendar d1,
                               @Param("d2") Calendar d2);

    @Query("select t from FinOperation t where t.parent = :parent")
    List<FinOperation> findAllChildrenByParent(@Param("parent") FinOperation parent);
}
