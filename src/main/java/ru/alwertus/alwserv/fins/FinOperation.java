package ru.alwertus.alwserv.fins;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "fin_operations")
public class FinOperation {

    public static FinOperation copyFinOperation(FinOperation orig) {
        FinOperation fo = new FinOperation();
        fo.setCreated(orig.getCreated());
        fo.setCreator(orig.getCreator());
        fo.setSheet(orig.getSheet());
        fo.setName(orig.getName());
        fo.setDescription(orig.getDescription());
        fo.setPlannedDate(orig.getPlannedDate());
        fo.setPlanned(orig.getPlanned());
        fo.setActualDate(orig.getActualDate());
        fo.setActual(orig.getActual());
        fo.setSign(orig.getSign());
        fo.setIsGroup(orig.getIsGroup());
        fo.setParent(orig.getParent());
        return fo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created")
    private Calendar created;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @Column(name = "sheet")
    private Long sheet;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "planned_date")
    private Calendar plannedDate;

    @Column(name = "planned")
    private int planned;

    @Column(name = "actual_date")
    private Calendar actualDate;

    @Column(name = "actual")
    private int actual;

    @Column(name = "sign")
    private char sign;

    @Column(name = "is_group")
    private char isGroup;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FinOperation parent;
}
