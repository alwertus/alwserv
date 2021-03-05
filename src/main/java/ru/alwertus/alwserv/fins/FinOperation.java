package ru.alwertus.alwserv.fins;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(name = "fin_operations")
public class FinOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created")
    private Date created;

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
    private Date actualDate;

    @Column(name = "actual")
    private int actual;

    @Column(name = "sign")
    private char sign;
}
