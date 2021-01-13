package ru.alwertus.alwserv.info.list;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.alwertus.alwserv.auth.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface InfoRepo extends CrudRepository<InfoRepoElement, Long> {

    @Override
    @Nonnull
    Optional<InfoRepoElement> findById(@Nonnull Long id);

    @Query("select t from InfoRepoElement t where t.access='PRIVATE' and t.creator=:u")
    List<InfoRepoElement> findAllPrivate(@Param("u") User user);

    @Query("select t from InfoRepoElement t where t.access='PUBLIC'")
    List<InfoRepoElement> findAllPublic();
}
