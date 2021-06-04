package ru.alwertus.alwserv.todo;

import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepo extends CrudRepository<TodoRecord, Long> {

    @Query("select t from TodoRecord t order by t.created desc")
    @NonNull
    List<TodoRecord> findAll();
}
