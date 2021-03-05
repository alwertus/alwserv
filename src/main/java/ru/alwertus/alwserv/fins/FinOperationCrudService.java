package ru.alwertus.alwserv.fins;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alwertus.alwserv.auth.CurrentUser;

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

    public List<FinOperationRs> getOperationsPerMonth(Date d1) {
        Calendar cc = new GregorianCalendar();
        cc.setTimeInMillis(d1.getTime());
        cc.set(Calendar.MONTH, cc.get(Calendar.MONTH) + 1);
        Date d2 = cc.getTime();

        log.info("get all records (" + d1.toString() + ", " + d2.toString() + ") user=" + currentUser.getCurrentUser().getId());
//        log.info(c.getTime().toString());
        return repo
                .findAll(currentUser.getCurrentUser(), d1, d2)
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
            Date plannedDate,
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
    public Date remove(Long id) throws Exception {
        log.info("Remove record [id] = " + id);
        FinOperation op = findRecord(id);
        Date date = op.getPlannedDate();
        repo.delete(op);
        return date;
    }
}