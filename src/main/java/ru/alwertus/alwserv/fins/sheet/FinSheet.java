package ru.alwertus.alwserv.fins.sheet;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "fin_sheet")
public class FinSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @Column(name = "start_day")
    private Integer startDay = 1;

    @Column(name = "name")
    private String name;
}