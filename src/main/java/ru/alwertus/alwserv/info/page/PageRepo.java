package ru.alwertus.alwserv.info.page;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PageRepo extends CrudRepository<Page, Long> {

    @Override
    Optional<Page> findById(Long id);
}
