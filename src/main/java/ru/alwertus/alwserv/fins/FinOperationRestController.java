package ru.alwertus.alwserv.fins;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.common.JSONObjectExtended;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Log4j2
@RestController
@RequestMapping("/api/v1/cash")
public class FinOperationRestController {

    private final FinOperationCrudService service;

    @Autowired
    public FinOperationRestController(FinOperationCrudService service) {
        this.service = service;
    }

    @PostMapping()
    public String request(@RequestBody String body) {
        log.info("Incomming request: " + body);
        JSONObjectExtended rq = new JSONObjectExtended(body);
        JSONObject rs = new JSONObject();

        String operation = rq.getString("Operation", "");
        try {
            Calendar date = new GregorianCalendar();
            switch (operation) {
                case "GetMonthOperations" -> {
                    date.setTimeInMillis(rq.getLong("Date", 0L));
                }
                case "Create" -> {
                    date.setTimeInMillis(rq.getLong("PlannedDate", 0L));
                    service.create(
                            rq.getString("Description", ""),
                            rq.getString("Name", ""),
                            date,
                            rq.getInt("Planned", 0),
                            rq.getString("Sign", "-"),
                            rq.getString("Group", "N")
                    );
                }
                case "CreateChild" -> {
                    date = service.createChild(
                            rq.getLong("ParentId"),
                            rq.getString("Description", ""),
                            rq.getString("Name", "")
                    );
                }
                case "Update" -> {
                    String field = rq.getString("Field","");
                    date = service.change(
                            rq.getLong("Id"),
                            field,
                            rq.get("newValue"))
                            .getPlannedDate();
                }
                case "FillFromTemplate" -> {
                    date.setTimeInMillis(rq.getLong("PlannedDate"));
                    service.fillDateFromTemplate(date);
                }
                case "Remove" -> {
                    date = service.remove(
                            rq.getLong("Id")
                    );
                }
                default -> throw new Exception("Unknown Operation '" + operation + "'");
            }
            rs.put("List", service.getOperationsPerMonthRs(date));
            rs.put("Result", "Ok");

        } catch(Exception e) {
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

        return rs.toString();
    }
}
