package ru.alwertus.alwserv.fins;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FinOperationRs {
    private final FinOperation repoObj;

    public FinOperationRs(FinOperation op) {
        repoObj = op;
    }

    public Long getId() {return repoObj.getId();}
    public Long getCreated() {return repoObj.getCreated().getTimeInMillis();}
    public Long getCreator() {return repoObj.getCreator().getId(); }
    public Long getSheet() {return repoObj.getSheet();}
    public String getName() {return repoObj.getName();}
    public String getDescription() {return repoObj.getDescription();}
    public Long getPlannedDate() {return repoObj.getPlannedDate().getTimeInMillis();}
    public int getPlanned() {return repoObj.getPlanned();}
    public Long getActualDate() {return repoObj.getActualDate().getTimeInMillis();}
    public int getActual() {return repoObj.getActual();}
    public char getSign() {return repoObj.getSign();}
    public char getIsGroupFlag() {return repoObj.getIsGroup();}
    public Long getParentId() {return repoObj.getParent().getId();}
    public int getPartNumber() {return repoObj.getPartNumber();}
}
