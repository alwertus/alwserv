package ru.alwertus.alwserv.cash.controller;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.cash.model.CashColumnRecord;
import ru.alwertus.alwserv.cash.model.CashSheetAccessRecord;
import ru.alwertus.alwserv.cash.model.SheetAccessType;
import ru.alwertus.alwserv.cash.service.*;
import ru.alwertus.alwserv.common.JSONObjectExtended;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/v1/cash")
public class CashController {

    private final CashSheetService sheetService;
    private final CashSheetAccessService accessService;
    private final UserService userService;
    private final CashService cashService;
    private final CashLineService lineService;
    private final CashColumnService columnService;

    @Autowired
    public CashController(CashSheetService cashSheetService, CashSheetAccessService accessService, UserService userService, CashService cashService, CashLineService lineService, CashColumnService columnService) {
        this.sheetService = cashSheetService;
        this.accessService = accessService;
        this.userService = userService;
        this.cashService = cashService;
        this.lineService = lineService;
        this.columnService = columnService;
    }

    @PostMapping()
    public String request(@RequestBody String body) {
        log.info("Incoming request: " + body);
        JSONObjectExtended rq = new JSONObjectExtended(body);
        JSONObject rs = new JSONObject();

        try {
            switch (rq.getString("Operation", "")) {
                case "NewSheet" -> sheetService.createSheet(rq.getString("Name", ""));
                case "GetSheets" -> rs.put("List", accessService.getAllAvailableSheets());
                case "SetActiveSheet" -> userService.setSheetId(userService.getCurrentUser(), rq.getLong("SheetId"));
                case "DeleteSheet" -> sheetService.deleteSheet(rq.getLong("SheetId"));
                case "GetUsers" -> rs.put("List", sheetService
                        .getActiveSheet()
                        .getAccessList()
                        .stream()
                        .map(CashSheetAccessRecord::getUser)
                        .collect(Collectors.toList())
                        .contains(userService.getCurrentUser())
                        ? accessService.getUsersBySheet(sheetService.getActiveSheet())
                        : Collections.emptyList());
                case "NewAccessUser" -> accessService.addAccess(userService.getUserByEmail(
                        rq.getString("Email")),
                        sheetService.getActiveSheet(),
                        SheetAccessType.R);
                case "ChangeAccess" -> accessService.addAccess(
                        userService.getUserById(rq.getLong("UserId")),
                        sheetService.getActiveSheet(),
                        SheetAccessType.valueOf(rq.getString("NewAccess"))
                );
                case "RemoveAccess" -> accessService.removeAccess(
                        userService.getUserById(rq.getLong("UserId")),
                        sheetService.getActiveSheet());

                case "GetSheetParam" -> {
                    rs.put("ColumnCount", sheetService.getActiveSheet().getColumnCount());
                    rs.put("Name", sheetService.getActiveSheet().getName());
                }
                case "CreateCashGroup" -> cashService
                        .createCashGroup(
                                rq.getString("Sign"),
                                rq.getString("Name"),
                                rq.getInt("Limit", 0),
                                rq.getInt("Year"),
                                rq.getInt("Month"),
                                rq.getInt("Column"));
                case "GetColumnData" -> {
                    Optional<CashColumnRecord> column = columnService.find(
                            sheetService.getActiveSheet(),
                            rq.getInt("Year"),
                            rq.getInt("Month"),
                            rq.getInt("Column"));

                    rs.put("List", column.isEmpty()
                            ? Collections.emptyList()
                            : cashService.getGroupList(column.get()));
                    column.ifPresent(cashColumnRecord -> rs.put("ColumnName", cashColumnRecord.getName()));
                }
                case "ColumnRename" -> columnService.rename(
                        columnService.getOrCreate(
                                sheetService.getActiveSheet(),
                                rq.getInt("Year"),
                                rq.getInt("Month"),
                                rq.getInt("Column")),
                        rq.getString("Name"));
                case "ChangeGroupLimit" -> cashService.changeGroupLimit(
                        rq.getLong("Id"),
                        rq.getInt("Limit")
                );
                case "ChangeGroupName" -> cashService.changeGroupName(
                        rq.getLong("Id"),
                        rq.getString("Name")
                );
                case "ChangeGroupSign" -> cashService.changeGroupSign(
                        rq.getLong("Id"),
                        rq.getString("Sign")
                );
                case "DeleteGroup" -> cashService.deleteGroup(
                        rq.getLong("Id")
                );
                case "AddLine" -> lineService.addLine(
                        rq.getLong("GroupId"),
                        rq.getString("Name"),
                        rq.getInt("Planned"),
                        rq.getInt("Actual")
                );
                case "LineUpdateField" -> lineService.update(
                        rq.getLong("Id"),
                        rq.getString("Field"),
                        rq.get("NewValue")
                );
                case "DeleteLine" -> lineService.delete(rq.getLong("Id"));
                case "AutoFill" -> cashService.autoFill(
                        rq.getInt("Year"),
                        rq.getInt("Month"),
                        rq.getInt("Column"));

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
