package ru.alwertus.alwserv.cash.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.cash.model.CashSheetRecord;
import ru.alwertus.alwserv.cash.model.CashSheetRepo;
import ru.alwertus.alwserv.cash.model.SheetAccessType;

@Log4j2
@Service
public class CashSheetService {

    private final CashSheetRepo sheetRepo;
    private final CashSheetAccessService accessService;
    private final UserService userService;

    @Autowired
    public CashSheetService(CashSheetRepo sheetRepo, CashSheetAccessService accessService, UserService userService) {
        this.sheetRepo = sheetRepo;
        this.accessService = accessService;
        this.userService = userService;
    }

    public void createSheet(String name) {
        createSheet(name, 2);
    }

    public void createSheet(String name, int columnCount) {
        CashSheetRecord sheet = new CashSheetRecord();
        sheet.setName(name);
        sheet.setColumnCount(columnCount);
        sheetRepo.save(sheet);
        accessService.addAccess(userService.getCurrentUser(), sheet, SheetAccessType.W);
    }

    public void deleteSheet(Long id) {
        CashSheetRecord sheet = sheetRepo.findById(id).orElseThrow(RuntimeException::new);
        sheetRepo.delete(sheet);
    }

    public CashSheetRecord getActiveSheet() {
        return sheetRepo
                .findById(userService.getCurrentUser().getFinSheetId())
                .orElseThrow(RuntimeException::new);
    }

}
