package ru.alwertus.alwserv.todo;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "todo")
public class TodoRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "requestor", referencedColumnName = "id")
    private User requestor;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "executor", referencedColumnName = "id")
    private User executor;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Calendar created;

    @Column(name = "completed")
    private Calendar completed;

    @Column(name = "decision")
    private String decision;
}
