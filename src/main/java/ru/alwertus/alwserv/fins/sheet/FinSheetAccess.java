package ru.alwertus.alwserv.fins.sheet;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "fin_sheet_access")
public class FinSheetAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "sheet_id", referencedColumnName = "id")
    private FinSheet sheet;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "access")
    private SheetAccess access;
}