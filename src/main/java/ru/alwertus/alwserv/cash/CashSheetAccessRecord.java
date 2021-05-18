package ru.alwertus.alwserv.cash;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cash_sheet_access")
public class CashSheetAccessRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "sheet_id", referencedColumnName = "id")
    private CashSheetRecord cashSheet;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "access")
    private SheetAccessType access;
}
