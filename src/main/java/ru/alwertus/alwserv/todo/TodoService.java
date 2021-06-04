package ru.alwertus.alwserv.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.UserService;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepo todoRepo;
    private final UserService userService;

    @Autowired
    public TodoService(TodoRepo todoRepo, UserService userService) {
        this.todoRepo = todoRepo;
        this.userService = userService;
    }

    public void create(String description) {
        TodoRecord record = new TodoRecord();
        record.setRequestor(userService.getCurrentUser());
        record.setDescription(description);
        record.setCreated(Calendar.getInstance());
        todoRepo.save(record);
    }

    public List<TodoRecordRs> getAll() {
        return todoRepo
                .findAll()
                .stream()
                .map(e -> new TodoRecordRs(e, userService.getCurrentUser()))
                .collect(Collectors.toList());
    }
    public void updateField(Long id, String fieldName, Object newValue) {
        TodoRecord record = todoRepo.findById(id).orElseThrow(RuntimeException::new);

        switch (fieldName) {
            case "decision" -> {
                record.setExecutor(userService.getCurrentUser());
                record.setCompleted(Calendar.getInstance());
                record.setDecision((String) newValue);
            }
            case "description" -> record.setDescription((String) newValue);
            case "completed" -> {
                if (!((Boolean) newValue)) {
                    record.setCompleted(null);
                    record.setExecutor(null);
                    record.setDecision(null);
                }
            }
        }

        todoRepo.save(record);
    }

}
