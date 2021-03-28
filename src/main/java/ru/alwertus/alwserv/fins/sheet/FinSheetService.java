package ru.alwertus.alwserv.fins.sheet;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.User;
import ru.alwertus.alwserv.auth.UserRepository;
import ru.alwertus.alwserv.auth.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FinSheetService {
    private final FinSheetRepo dbSheet;
    private final FinSheetAccessRepo dbAccess;
    private final UserService userService;
    private final UserRepository dbUser;

    @Autowired
    public FinSheetService(FinSheetRepo dbSheet, FinSheetAccessRepo dbAccess, UserService userService, UserRepository dbUser) {
        this.dbSheet = dbSheet;
        this.dbAccess = dbAccess;
        this.userService = userService;
        this.dbUser = dbUser;
    }

    public List<FinSheetListRs> getAllAvailable() {
        return dbAccess.findAllByUser(userService.getCurrentUser()).stream().map(FinSheetListRs::new).collect(Collectors.toList());
    }

    public List<FinUsersListRs> getUsersForActiveSheet() {
        Long activeSheetId = userService.getCurrentUser().getFinSheetId();
        if (activeSheetId == null)
            return null;
        Optional<FinSheet> sheet = dbSheet.findById(activeSheetId);
        if (sheet.isEmpty())
            return null;
        return dbAccess.findAllBySheet(sheet.get()).stream().map(FinUsersListRs::new).collect(Collectors.toList());
    }

    public FinSheet getActiveSheet() throws Exception {
        if (userService.getCurrentUser().getFinSheetId() == null) return null;
        return dbSheet.findById(userService.getCurrentUser().getFinSheetId()).orElseThrow(() -> new Exception("Sheet not found"));
    }

    public boolean addUserToActiveSheet(User user){
        try {
            FinSheet activeSheet = getActiveSheet();
            Optional<FinSheetAccess> sheetAccess = dbAccess.findByUserAndSheet(activeSheet, user);
            if (sheetAccess.isPresent())
                return false;
            FinSheetAccess newSheetAccess = new FinSheetAccess();
            newSheetAccess.setSheet(activeSheet);
            newSheetAccess.setAccess(SheetAccess.W);
            newSheetAccess.setUser(user);
            dbAccess.save(newSheetAccess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeUserFromActiveSheet(User user) {
        try {
            Optional<FinSheetAccess> sheetAccess = dbAccess.findByUserAndSheet(getActiveSheet(), user);
            if (sheetAccess.isEmpty())
                return false;
            // if user active sheet = delete sheet -> user active sheet = null
            if (user.getFinSheetId().equals(sheetAccess.get().getSheet().getId()))
                userService.setSheetId(user, null);
            dbAccess.delete(sheetAccess.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void create() {
        User user = userService.getCurrentUser();

        FinSheet sheet = new FinSheet();
        sheet.setCreator(user);
        dbSheet.save(sheet);

        FinSheetAccess sheetAccess = new FinSheetAccess();
        sheetAccess.setSheet(sheet);
        sheetAccess.setUser(user);
        sheetAccess.setAccess(SheetAccess.W);
        dbAccess.save(sheetAccess);

        user.setFinSheetId(sheet.getId());
        dbUser.save(user);
        log.info(String.format("Create new sheet id = '%s' for User id = '%s'. AccessId = '%s'", sheet.getId(), user.getId(), sheetAccess.getId()));
    }

    public void changeName(Long id, String newName) throws Exception {
        log.info("Change sheet name [id] = " + id + ", newName = '" + newName + "'");
        FinSheet sheet = dbSheet
                .findById(id)
                .orElseThrow(() -> new Exception("Sheet id=" + id + " not found"));
        sheet.setName(newName);
        dbSheet.save(sheet);
    }
}
