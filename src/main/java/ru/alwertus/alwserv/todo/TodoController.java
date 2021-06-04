package ru.alwertus.alwserv.todo;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.common.JSONObjectExtended;

@Log4j2
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    public String request(@RequestBody String body) {
        log.info("Incoming request: " + body);
        JSONObjectExtended rq = new JSONObjectExtended(body);
        JSONObject rs = new JSONObject();

        try {
            switch (rq.getString("Operation", "")) {
                case "Create" -> todoService.create(rq.getString("Description"));
                case "GetTodoList" -> rs.put("List", todoService.getAll());
                case "UpdateField" -> todoService.updateField(
                        rq.getLong("Id"),
                        rq.getString("Field"),
                        rq.get("Value")
                );
            }
            rs.put("Result", "Ok");
        } catch (Exception e) {
            log.error(e.getMessage());
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

//        log.info("Send response: " + rs.toString());
        return rs.toString();
    }
}
