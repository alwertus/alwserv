package ru.alwertus.alwserv.cash;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.UserService;

@Log4j2
@Service
public class CashService {

    private final CashSheetRepo sheetRepo;
    private final CashSheetAccessService accessService;
    private final UserService userService;

    @Autowired
    public CashService(CashSheetRepo sheetRepo, CashSheetAccessService accessService, UserService userService) {
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
