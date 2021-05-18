package ru.alwertus.alwserv.cash;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="cash_sheet")
public class CashSheetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "column_count")
    private int columnCount;

    @OneToMany(mappedBy = "cashSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashSheetAccessRecord> accessList;

}