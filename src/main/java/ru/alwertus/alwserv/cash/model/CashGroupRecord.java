package ru.alwertus.alwserv.cash.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "cash_group")
public class CashGroupRecord {

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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashLineRecord> lines;
}
