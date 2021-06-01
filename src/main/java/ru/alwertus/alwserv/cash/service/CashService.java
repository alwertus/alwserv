package ru.alwertus.alwserv.cash.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.cash.model.CashColumnRecord;
import ru.alwertus.alwserv.cash.model.CashGroupRecord;
import ru.alwertus.alwserv.cash.model.CashGroupRepo;
import ru.alwertus.alwserv.cash.view.CashGroupRecordRs;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CashService {

    private final CashSheetService sheetService;
    private final CashGroupRepo groupRepo;
    private final CashColumnService columnService;

    @Autowired
    public CashService(CashSheetService sheetService, CashGroupRepo groupRepo, CashColumnService columnService) {
        this.sheetService = sheetService;
        this.groupRepo = groupRepo;
        this.columnService = columnService;
    }

    public void createCashGroup(String sign,
                                String name,
                                int limit,
                                int year,
                                int month,
                                int part) {
        CashGroupRecord group = new CashGroupRecord();
        group.setColumn(columnService.getOrCreate(sheetService.getActiveSheet(), year, month, part));
        group.setSign(sign.charAt(0));
        group.setSumLimit(limit);
        group.setName(name);

        groupRepo.save(group);
    }

    public List<CashGroupRecordRs> getGroupList(CashColumnRecord column) {
        return groupRepo.findAllByColumn(column)
                .stream()
                .map(CashGroupRecordRs::new)
                .collect(Collectors.toList());
    }

    public void changeGroupLimit(Long id, int newLimit) {
        CashGroupRecord group = groupRepo.findById(id).orElseThrow(RuntimeException::new);
        if (newLimit == group.getSumLimit())
            return;
        group.setSumLimit(newLimit);
        groupRepo.save(group);
    }

    public void changeGroupName(Long id, String newName) {
        CashGroupRecord group = groupRepo.findById(id).orElseThrow(RuntimeException::new);
        if (newName.equals(group.getName()))
            return;
        group.setName(newName);
        groupRepo.save(group);
    }

    public void changeGroupSign(Long id, String newSign) {
        if (newSign.length() == 0) throw new RuntimeException("Sign must be + or -. Current value='" + newSign + "'");
        CashGroupRecord group = groupRepo.findById(id).orElseThrow(RuntimeException::new);
        if (newSign.charAt(0) == group.getSign())
            return;
        group.setSign(newSign.charAt(0));
        groupRepo.save(group);
    }

    public void deleteGroup(Long id) {
        CashGroupRecord group = groupRepo.findById(id).orElseThrow(RuntimeException::new);
        groupRepo.delete(group);
    }

}