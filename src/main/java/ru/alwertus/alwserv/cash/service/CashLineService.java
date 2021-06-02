package ru.alwertus.alwserv.cash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.cash.model.CashGroupRecord;
import ru.alwertus.alwserv.cash.model.CashGroupRepo;
import ru.alwertus.alwserv.cash.model.CashLineRecord;
import ru.alwertus.alwserv.cash.model.CashLineRepo;

import java.util.Calendar;

@Service
public class CashLineService {
    private final CashLineRepo lineRepo;
    private final CashGroupRepo groupRepo;

    @Autowired
    public CashLineService(CashLineRepo lineRepo, CashGroupRepo groupRepo) {
        this.lineRepo = lineRepo;
        this.groupRepo = groupRepo;
    }

    public void addLine(Long groupId, String name, int planned, int actual) {
        CashGroupRecord group = groupRepo.findById(groupId).orElseThrow(RuntimeException::new);
        CashLineRecord line = new CashLineRecord();
        line.setGroup(group);
        line.setName(name);
        line.setSumPlan(planned);
        line.setSumActual(actual);
        lineRepo.save(line);
    }

    public void delete(Long id) {
        CashLineRecord line = lineRepo
                .findById(id)
                .orElseThrow(RuntimeException::new);

        lineRepo.delete(line);
    }

    public void update(Long id, String fieldName, Object newValue) {
        CashLineRecord line = lineRepo
                .findById(id)
                .orElseThrow(RuntimeException::new);

        switch (fieldName) {
            case "name" -> line.setName((String) newValue);
            case "comment" -> line.setComment((String) newValue);
            case "sumPlan" -> line.setSumPlan(Integer.parseInt((String) newValue));
            case "sumActual" -> line.setSumActual(Integer.parseInt((String) newValue));
            case "completed" -> {
                boolean compl = (boolean) newValue;
                line.setCompleted(compl
                        ? Calendar.getInstance()
                        : null);
                if (compl && line.getSumActual() == 0)
                    line.setSumActual(line.getSumPlan());
            }
        }

        lineRepo.save(line);
    }
}
