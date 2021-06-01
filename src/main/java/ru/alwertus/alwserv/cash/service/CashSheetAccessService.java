package ru.alwertus.alwserv.cash.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.User;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.cash.model.CashSheetAccessRecord;
import ru.alwertus.alwserv.cash.model.CashSheetAccessRepo;
import ru.alwertus.alwserv.cash.model.CashSheetRecord;
import ru.alwertus.alwserv.cash.model.SheetAccessType;
import ru.alwertus.alwserv.cash.view.CashSheetAccessRecordRs;
import ru.alwertus.alwserv.cash.view.UserRs;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CashSheetAccessService {

    private final CashSheetAccessRepo accessRepo;
    private final UserService userService;

    @Autowired
    public CashSheetAccessService(CashSheetAccessRepo accessRepo, UserService userService) {
        this.accessRepo = accessRepo;
        this.userService = userService;
    }

    public void addAccess(User user, CashSheetRecord sheet, SheetAccessType access) {
        CashSheetAccessRecord accessRecord = accessRepo.findByUserIsAndCashSheetIs(user, sheet).orElse(new CashSheetAccessRecord());
        accessRecord.setAccess(access);
        accessRecord.setCashSheet(sheet);
        accessRecord.setUser(user);
        accessRepo.save(accessRecord);
    }

    public void removeAccess(User user, CashSheetRecord sheet) {
        CashSheetAccessRecord accessRecord = accessRepo
                .findByUserIsAndCashSheetIs(user, sheet)
                .orElseThrow(RuntimeException::new);
        accessRepo.delete(accessRecord);
    }

    public List<CashSheetAccessRecordRs> getAllAvailableSheets() {
        return accessRepo
                .findAllByUser(userService.getCurrentUser())
                .stream()
                .map(CashSheetAccessRecordRs::new)
                .collect(Collectors.toList());
    }

    public List<UserRs> getUsersBySheet(CashSheetRecord sheet) {
        return accessRepo
                .findAllByCashSheet(sheet)
                .stream()
                .map(e -> new UserRs(e.getUser(), e.getAccess()) )
                .collect(Collectors.toList());
    }
}
