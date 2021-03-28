package ru.alwertus.alwserv.info.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.info.list.InfoAccess;
import ru.alwertus.alwserv.info.list.InfoRepo;
import ru.alwertus.alwserv.info.list.InfoRepoElement;

import java.util.NoSuchElementException;

@Service
public class PageCurdService {


    private final InfoRepo infoRepo;
    private final UserService userService;
    private final PageRepo pageRepo;

    @Autowired
    public PageCurdService(InfoRepo infoRepo, UserService userService, PageRepo pageRepo) {
        this.infoRepo = infoRepo;
        this.userService = userService;
        this.pageRepo = pageRepo;
    }

    private InfoRepoElement getInfoRepoElement(Long id) {
        InfoRepoElement menuItem = infoRepo.findById(id)
                .orElseThrow(()->new NoSuchElementException(String.format("Record id=%d not found", id)));

        if (userService.getCurrentUser() != menuItem.getCreator() && menuItem.getAccess() != InfoAccess.PUBLIC)
            throw new RuntimeException("Forbidden. You have not access to record id=" + id);

        return menuItem;
    }

    public String getPage(Long id) {

        getInfoRepoElement(id);

        Page page = pageRepo.findById(id).orElse(new Page(id, ""));

        return page.getHtml();
    }

    public void setPage(Long id, String newHtml) {
        getInfoRepoElement(id);

        Page page = pageRepo.findById(id).orElse(new Page(id, ""));
        page.setHtml(newHtml);

        pageRepo.save(page);
    }
}
