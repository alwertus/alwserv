package ru.alwertus.alwserv.info.list;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfoTreeNode {
    private Long id;
    private String title;
    private InfoAccess access;
    private Long parent;
    private String creator;
}
