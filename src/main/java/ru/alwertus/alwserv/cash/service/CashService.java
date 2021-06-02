package ru.alwertus.alwserv.cash.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.cash.model.*;
import ru.alwertus.alwserv.cash.view.CashGroupRecordRs;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CashService {

    private final CashSheetService sheetService;
    private final CashGroupRepo groupRepo;
    private final CashColumnService columnService;
    private final CashLineRepo lineRepo;

    @Autowired
    public CashService(CashSheetService sheetService, CashGroupRepo groupRepo, CashColumnService columnService, CashLineRepo lineRepo) {
        this.sheetService = sheetService;
        this.groupRepo = groupRepo;
        this.columnService = columnService;
        this.lineRepo = lineRepo;
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

    public void autoFill(int year, int month, int columnNum) {
        CashColumnRecord columnTemplate = columnService
                .find(sheetService.getActiveSheet(), 0, 0, columnNum)
                .orElseThrow(() -> new RuntimeException("Template for column '" + columnNum + "' not found"));

        CashColumnRecord columnCurrent = columnService
                .find(sheetService.getActiveSheet(), year, month, columnNum)
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Current column '%d' for %d.%d not found", columnNum, year, month)));

        if (columnTemplate.getName() != null && !columnTemplate.getName().equals(""))
            columnService.rename(columnCurrent, columnTemplate.getName());

        List<CashGroupRecord> groupsTemplate = groupRepo.findAllByColumn(columnTemplate);
        if (groupsTemplate.size() == 0) return;

        // delete all copied groups from current column
        groupRepo.findAllByColumn(columnCurrent)
                .stream()
                .filter(e -> e.getCopySign() == 'Y')
                .forEach(groupRepo::delete);

        // copy all groups
        groupsTemplate.forEach(origGroup -> {
            CashGroupRecord copiedGroup = new CashGroupRecord(origGroup);
            copiedGroup.setColumn(columnCurrent);
            groupRepo.save(copiedGroup);

            // copy lines
            origGroup.getLines().forEach(origLine -> {
                CashLineRecord copiedLine = new CashLineRecord(origLine);
                copiedLine.setGroup(copiedGroup);
                lineRepo.save(copiedLine);
            });

        });

    }

}