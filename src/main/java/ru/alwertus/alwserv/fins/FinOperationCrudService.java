package ru.alwertus.alwserv.fins;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.UserService;
import ru.alwertus.alwserv.auth.User;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FinOperationCrudService {

    private final FinOperationRepo repo;
    private final UserService userService;

    @Autowired
    public FinOperationCrudService(FinOperationRepo repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    public List<FinOperation> getOperationsPerMonth(Calendar d) {
        Calendar c1 = new GregorianCalendar(d.get(Calendar.YEAR), d.get(Calendar.MONTH), 1);
        Calendar c2 = new GregorianCalendar(d.get(Calendar.YEAR), d.get(Calendar.MONTH), 1);
        c2.add(Calendar.MONTH, 1);

        User user = userService.getCurrentUser();

        //TODO добавить проверку, что лист доступен пользователю

        List<FinOperation> queryList = repo.findAllBetweenDates(user.getFinSheetId(), c1, c2);

        log.info(String.format("Get All Per Month (USER=%s, START='%s', END='%s'). Return %s records",
                user.getId(),
                c1.get(Calendar.YEAR)+"-"+c1.get(Calendar.MONTH)+"-"+c1.get(Calendar.DAY_OF_MONTH),
                c2.get(Calendar.YEAR)+"-"+c2.get(Calendar.MONTH)+"-"+c1.get(Calendar.DAY_OF_MONTH),
                queryList.size()
        ));

        return queryList;
    }

    // TODO [3] Сделать число - настраеваемым для каждой таблицы
    public List<FinOperationRs> getOperationsPerMonthRs(Calendar d) {
        return getOperationsPerMonth(d)
                .stream()
                .map(FinOperationRs::new)
                .collect(Collectors.toList());
    }

    private FinOperation findRecord(Long id) throws Exception {
        return repo
                .findById(id)
                .orElseThrow(() -> new Exception("Record id = " + id + " not found"));
    }

    public Long create(
            String description,
            String name,
            Calendar plannedDate,
            Integer planned,
            String sign,
            String group,
            Integer partNum
            ) {
        FinOperation op = new FinOperation();
        op.setCreator(userService.getCurrentUser());
        op.setDescription(description);
        op.setName(name);
        op.setPlannedDate(plannedDate);
        op.setPlanned(planned);
        op.setCreated(new GregorianCalendar());
        op.setSign(sign.charAt(0));
        op.setSheet(userService.getCurrentUser().getFinSheetId());
        op.setIsGroup(group.charAt(0));
        op.setPartNumber(partNum);
        repo.save(op);
        log.info("Save operation -> OK. ID=" + op.getId());
        return op.getId();
    }
    public Calendar createChild (
            Long parentId,
            String description,
            String name) throws Exception {
        FinOperation parent = repo.findById(parentId).orElseThrow(() -> new Exception("Parent id='" + parentId + "' not found"));
        FinOperation op = new FinOperation();
        op.setCreator(parent.getCreator());
        op.setDescription(description);
        op.setName(name);
        op.setPlannedDate(parent.getPlannedDate());
        op.setCreated(new GregorianCalendar());
        op.setSign(parent.getSign());
        op.setSheet(parent.getSheet());
        op.setIsGroup('N');
        op.setParent(parent);
        repo.save(op);
        log.info("Save child operation -> OK. ID=" + op.getId());
        return op.getPlannedDate();
    }

    public FinOperation change(Long id, String field, Object newValue) throws Exception {
        log.info("Change record [id] = " + id + " [" + field + "] = " + newValue.toString());
        FinOperation op = findRecord(id);
        switch (field) {
            case "completed" -> {
                op.setActualDate((Boolean) newValue ? new GregorianCalendar() : null);
                if (op.getActual() == 0 && (Boolean) newValue)
                    op.setActual(op.getPlanned());
            }
            case "name" -> op.setName((String)newValue);
            case "description" -> op.setDescription((String)newValue);
            case "planned" -> op.setPlanned(Integer.parseInt((String)newValue));
            case "actual" -> op.setActual(Integer.parseInt((String)newValue));
            case "sign" -> {
                char newSign = ((String)newValue).charAt(0);
                op.setSign(newSign);
                // set sign on all child records
                if (op.getIsGroup() == 'Y') {
                    repo.findAllChildrenByParent(op)
                        .forEach((e) -> {
                            e.setSign(newSign);
                            repo.save(e);
                        });
                }
            }
        }
        repo.save(op);
        return op;
    }
    public void fillDateFromTemplate(Calendar d) {
        Calendar zero = new GregorianCalendar();
        zero.setTimeInMillis(0);
        List<FinOperation> templateLit = getOperationsPerMonth(zero);

        // копируем все НЕ дочерние записи
        templateLit
                .stream().filter(e -> e.getParent() == null)
                .forEach(e -> {

                    FinOperation fo = FinOperation.copyFinOperation(e);
                    fo.setPlannedDate(d);
                    repo.save(fo);

                    // если запись = группа -> копируем всех деток и указываем её как родителя
                    if (e.getIsGroup() == 'Y') {

                        templateLit
                                .stream().filter(child -> child.getParent() != null && child.getParent().getId().equals(e.getId()))
                                .forEach(child -> {
                                    FinOperation copyChild = FinOperation.copyFinOperation(child);
                                    copyChild.setPlannedDate(d);
                                    copyChild.setParent(fo);
                                    repo.save(copyChild);
                        });
                    }
        });

    }

    public Calendar remove(Long id) throws Exception {
        log.info("Remove record [id] = " + id);
        FinOperation op = findRecord(id);
        // remove all child records
        if (op.getIsGroup() == 'Y') {
            repo.findAllChildrenByParent(op)
                    .forEach(repo::delete);
        }

        Calendar date = op.getPlannedDate();
        repo.delete(op);
        return date;
    }
}