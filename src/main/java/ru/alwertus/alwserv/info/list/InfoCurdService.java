package ru.alwertus.alwserv.info.list;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.CurrentUser;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Log4j2
@Service
public class InfoCurdService {

    private final InfoRepo infoRepo;
    private final CurrentUser currentUser;

    @Autowired
    public InfoCurdService(InfoRepo infoRepo, CurrentUser currentUser) {
        this.infoRepo = infoRepo;
        this.currentUser = currentUser;
    }

    public void update(Long id, Long newParentId, String newTitle) {
        log.info("Update InfoList. Record id=" + id);

        InfoRepoElement updatedRecord = infoRepo.findById(id)
                .orElseThrow(()->new NoSuchElementException(String.format("Record id=%d not found", id)));

        if (currentUser.getCurrentUser() != updatedRecord.getCreator())
            throw new RuntimeException("Forbidden. You have not access to record id=" + id);

        if (newParentId != null) {
            if (newParentId > 0) {
                InfoRepoElement parent = infoRepo.findById(newParentId)
                        .orElseThrow(() -> new NoSuchElementException(String.format("Parent id=%d not found", newParentId)));

                updatedRecord.setParent(parent);
            } else {
                updatedRecord.setParent(null);
            }
        }

        if (newTitle != null) {
            updatedRecord.setTitle(newTitle);
        }

        infoRepo.save(updatedRecord);
    }

    public Long create(Long newParentId, String newTitle) {
        log.info("Create new record InfoList");
        InfoRepoElement newRecord = new InfoRepoElement();
        newRecord.setTitle(newTitle);
        newRecord.setCreator(currentUser.getCurrentUser());
        if (newParentId != null && newParentId > 1) {
            InfoRepoElement parent = infoRepo.findById(newParentId)
                    .orElseThrow(() -> new NoSuchElementException(String.format("Parent id=%d not found", newParentId)));

            newRecord.setParent(parent);
        }

        infoRepo.save(newRecord);
        return newRecord.getId();
    }

    public List<InfoTreeNode> getAll(InfoAccess infoAccess) {
        log.trace("Get All " + infoAccess + " Records");
        List<InfoRepoElement> dbResult = infoAccess.equals(InfoAccess.PUBLIC)
                ? infoRepo.findAllPublic()
                : infoRepo.findAllPrivate(currentUser.getCurrentUser());
        return dbResult.stream()
                .map(repo -> new InfoTreeNode(
                        repo.getId(),
                        repo.getTitle(),
                        repo.getAccess(),
                        repo.getParent() == null
                                ? null
                                : repo.getParent().getId(),
                        repo.getCreator() == null
                                ? null
                                : repo.getCreator().getEmail()))
                .collect(Collectors.toList());
    }
}
