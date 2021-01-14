package ru.alwertus.alwserv.info.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="info_page")
public class Page {
    @Id
    private Long id;

    @Lob
    @Column(name="html")
    private String html;
}
