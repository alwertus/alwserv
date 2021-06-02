package ru.alwertus.alwserv.cash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cash_line")
public class CashLineRecord {

    public CashLineRecord(CashLineRecord orig) {
        setName(orig.getName());
        setSumPlan(orig.getSumPlan());
        setSumActual(orig.getSumActual());
        setCompleted(orig.getCompleted());
        setComment(orig.getComment());
        setSequence(orig.getSequence());
        setGroup(orig.getGroup());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sum_plan")
    private int sumPlan;

    @Column(name = "sum_actual")
    private int sumActual;

    @Column(name = "completed")
    private Calendar completed;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sequence")
    private int sequence;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private CashGroupRecord group;

}
