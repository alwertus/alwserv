package ru.alwertus.alwserv.info.list;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This structure will be send to client
 */
@Data
@AllArgsConstructor
public class InfoTreeNode {
    private Long id;
    private String title;
    private InfoAccess access;
    private Long parent;
    private String creator;
}
