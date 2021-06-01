package ru.alwertus.alwserv.cash.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cash_column")
public class CashColumnRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "date_year")
    private Integer year;

    @NonNull
    @Column(name = "date_month")
    private Integer month;

    @NonNull
    @Column(name = "part")
    private Integer part;

    @Column(name = "name")
    private String name;

    @NonNull
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "sheet_id", referencedColumnName = "id")
    private CashSheetRecord sheet;

}
