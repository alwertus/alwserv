package ru.alwertus.alwserv.fins.sheet;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.common.JSONObjectExtended;

@Log4j2
@RestController
@RequestMapping("/api/v1/cash_options")
public class FinSheetRestController {

    private final FinSheetService service;
    private final UserService userService;

    @Autowired
    public FinSheetRestController(FinSheetService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    public String request(@RequestBody String body) {
        log.info("Incomming request: " + body);
        JSONObjectExtended rq = new JSONObjectExtended(body);
        JSONObject rs = new JSONObject();

        String operation = rq.getString("Operation", "");
        try {
            switch (operation) {
                case "GetList" -> {
                    /*Calendar d = new GregorianCalendar();
                    d.setTimeInMillis(rq.getLong("Date", 0L));
                    rs.put("List", service.getOperationsPerMonth(d));*/
                }
                case "CreateSheet" -> {
                    service.create();
                }
                case "Update" -> {
//                    String field = rq.getString("Field");
                    switch (rq.getString("Field")) {
                        case "Selected" -> {
                            userService.setSheetId(userService.getCurrentUser(), rq.getLong("NewId"));
                        }
                        case "Name" -> {
                            service.changeName(rq.getLong("Id"), rq.getString("NewName"));
                        }
                        default -> throw new Exception("Unknown updated Field");
                    }
                }
                case "Add User" -> {
                    if (!service.addUserToActiveSheet(userService.getUserByEmail(rq.getString("Email"))))
                        throw new Exception("User not added");
                }
                case "Remove User" -> {
                    if (!service.removeUserFromActiveSheet(userService.getUserById(rq.getLong("UserId"))))
                        throw new Exception("User not removed");
                }
                case "Remove" -> {
                   /* rs.put("List", service.getOperationsPerMonth(
                            service.remove(
                                    rq.getLong("Id")
                            )));*/
                }
                default -> throw new Exception("Unknown Operation '" + operation + "'");
            }

            rs.put("List", service.getAllAvailable());

            FinSheet finSheet = service.getActiveSheet();
            rs.put("Active", finSheet == null ? "" : finSheet.getId());

            rs.put("Users", service.getUsersForActiveSheet());

            rs.put("Result", "Ok");

        } catch(Exception e) {
            log.error(e.getMessage());
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

        return rs.toString();
    }
}
