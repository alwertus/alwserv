package ru.alwertus.alwserv.cash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cash_group")
public class CashGroupRecord {

    public CashGroupRecord(CashGroupRecord orig) {
        setSign(orig.getSign());
        setName(orig.getName());
        setSumLimit(orig.getSumLimit());
        setColumn(orig.getColumn());
        setSequence(orig.getSequence());
        setCopySign('Y');
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sum_limit")
    private int sumLimit;

    @JoinColumn(name = "column_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.DETACH)
    private CashColumnRecord column;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "sign")
    private char sign;

    @Column(name = "name")
    private String name;

    @Column(name = "copy_sign")
    private char copySign;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashLineRecord> lines;
}
