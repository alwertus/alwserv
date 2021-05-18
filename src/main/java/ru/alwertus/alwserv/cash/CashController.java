package ru.alwertus.alwserv.cash;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.common.JSONObjectExtended;

import java.util.Collections;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/v1/cash")
public class CashController {

    private final CashService cashService;
    private final CashSheetAccessService accessService;
    private final UserService userService;

    @Autowired
    public CashController(CashService cashService, CashSheetAccessService accessService, UserService userService) {
        this.cashService = cashService;
        this.accessService = accessService;
        this.userService = userService;
    }

    @PostMapping()
    public String request(@RequestBody String body) {
        log.info("Incoming request: " + body);
        JSONObjectExtended rq = new JSONObjectExtended(body);
        JSONObject rs = new JSONObject();

        try {
            switch (rq.getString("Operation", "")) {
                case "NewSheet" -> {
                    cashService.createSheet(rq.getString("Name", ""));
                }
                case "GetSheets" -> {
                    rs.put("List", accessService.getAllAvailableSheets());
                }
                case "SetActiveSheet" -> {
                    userService.setSheetId(userService.getCurrentUser(), rq.getLong("SheetId"));
                }
                case "DeleteSheet" -> {
                    cashService.deleteSheet(rq.getLong("SheetId"));
                }
                case "GetUsers" -> {
                    log.info(cashService
                            .getActiveSheet()
                            .getAccessList()
                            .stream()
                            .map(CashSheetAccessRecord::getUser)
                            .collect(Collectors.toList())
                            .contains(userService.getCurrentUser())
                            ? "YES"
                            : "NO"
                    );

                    rs.put("List", cashService
                            .getActiveSheet()
                            .getAccessList()
                            .stream()
                            .map(CashSheetAccessRecord::getUser)
                            .collect(Collectors.toList())
                            .contains(userService.getCurrentUser())
                            ? accessService.getUsersBySheet(cashService.getActiveSheet())
                            : Collections.emptyList());
                }
                case "NewAccessUser" -> {
                    accessService.addAccess(userService.getUserByEmail(
                            rq.getString("Email")),
                            cashService.getActiveSheet(),
                            SheetAccessType.R);
                }
                case "ChangeAccess" -> {
                    accessService.addAccess(
                            userService.getUserById(rq.getLong("UserId")),
                            cashService.getActiveSheet(),
                            SheetAccessType.valueOf(rq.getString("NewAccess"))
                    );
                }
                case "RemoveAccess" -> {
                    accessService.removeAccess(
                            userService.getUserById(rq.getLong("UserId")),
                            cashService.getActiveSheet());
                }
                default -> throw new Exception("Unknown operation");
            }
            rs.put("Result", "Ok");
        } catch (Exception e) {
            log.error(e.getMessage());
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

        return rs.toString();
    }
}
