package ru.alwertus.alwserv.fins;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.CurrentUser;
import ru.alwertus.alwserv.auth.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FinOperationCrudService {

    private final FinOperationRepo repo;
    private final CurrentUser currentUser;

    @Autowired
    public FinOperationCrudService(FinOperationRepo repo, CurrentUser currentUser) {
        this.repo = repo;
        this.currentUser = currentUser;
    }

    public List<FinOperationRs> getOperationsPerMonth(Calendar d) {
        Calendar c1 = new GregorianCalendar(d.get(Calendar.YEAR), d.get(Calendar.MONTH), 1);
        Calendar c2 = new GregorianCalendar(d.get(Calendar.YEAR), d.get(Calendar.MONTH), 1);
        c2.add(Calendar.MONTH, 1);

        User user = currentUser.getCurrentUser();

        List<FinOperation> queryList = repo.findAllBetweenDates(user, c1, c2);

        log.info(String.format("Get All. USER=%s, SIZE=%s, START='%s', END='%s'",
                user.getId(),
                queryList.size(),
                c1.get(Calendar.YEAR)+"-"+c1.get(Calendar.MONTH)+"-"+c1.get(Calendar.DAY_OF_MONTH),
                c2.get(Calendar.YEAR)+"-"+c2.get(Calendar.MONTH)+"-"+c1.get(Calendar.DAY_OF_MONTH)
        ));

        return queryList
                .stream()
                .map(FinOperationRs::new)
                .collect(Collectors.toList());
    }

    private FinOperation findRecord(Long id) throws Exception{
        return repo
                .findById(id)
                .orElseThrow(() -> new Exception("Record id = " + id + " not found"));
    }

    public Long create(
            Long sheet_id,
            String description,
            String name,
            Calendar plannedDate,
            Integer planned,
            String sign
            ) {
        if (sheet_id < 0) throw new RuntimeException("Wrong SheedId (" + sheet_id + ")");
        FinOperation op = new FinOperation();
        op.setSheet(sheet_id);
        op.setCreator(currentUser.getCurrentUser());
        op.setDescription(description);
        op.setName(name);
        op.setPlannedDate(plannedDate);
        op.setPlanned(planned);
        op.setCreated(new Date());
        op.setSign(sign.charAt(0));
        repo.save(op);
        log.info("Save operation -> OK. ID=" + op.getId());
        return op.getId();
    }

    public FinOperation change(Long id, String field, Object newValue) throws Exception {
        log.info("Change record [id] = " + id + " [" + field + "] = " + newValue.toString());
        FinOperation op = findRecord(id);
        switch (field) {
            case "completed" -> {
                op.setActualDate((Boolean) newValue ? new Date() : null);
                if (op.getActual() == 0 && (Boolean) newValue)
                    op.setActual(op.getPlanned());
            }
            case "name" -> op.setName((String)newValue);
            case "description" -> op.setDescription((String)newValue);
            case "planned" -> op.setPlanned(Integer.parseInt((String)newValue));
            case "actual" -> op.setActual(Integer.parseInt((String)newValue));
            case "sign" -> op.setSign(((String)newValue).charAt(0));

        }
        repo.save(op);
        return op;
    }
    public Calendar remove(Long id) throws Exception {
        log.info("Remove record [id] = " + id);
        FinOperation op = findRecord(id);
        Calendar date = op.getPlannedDate();
        repo.delete(op);
        return date;
    }
}