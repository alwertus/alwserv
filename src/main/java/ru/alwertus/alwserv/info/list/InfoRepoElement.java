package ru.alwertus.alwserv.info.list;

import lombok.Data;
import ru.alwertus.alwserv.auth.User;

import javax.persistence.*;

@Data
@Entity
@Table (name="infostruct")
public class InfoRepoElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="access")
    @Enumerated(value = EnumType.STRING)
    private InfoAccess access = InfoAccess.PRIVATE;

    @ManyToOne()
    @JoinColumn(name="parent")
    private InfoRepoElement parent;

    @ManyToOne()
    @JoinColumn(name="creator")
    private User creator;
}