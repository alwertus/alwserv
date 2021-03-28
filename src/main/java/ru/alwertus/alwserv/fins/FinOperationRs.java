package ru.alwertus.alwserv.fins;

public class FinOperationRs {
    private final FinOperation repo;

    public FinOperationRs(FinOperation op) {
        repo = op;
    }

    public Long getId() {return repo.getId();}
    public Long getCreated() {return repo.getCreated().getTimeInMillis();}
    public Long getCreator() {return repo.getCreator().getId(); }
    public Long getSheet() {return repo.getSheet();}
    public String getName() {return repo.getName();}
    public String getDescription() {return repo.getDescription();}
    public Long getPlannedDate() {return repo.getPlannedDate().getTimeInMillis();}
    public int getPlanned() {return repo.getPlanned();}
    public Long getActualDate() {return repo.getActualDate().getTimeInMillis();}
    public int getActual() {return repo.getActual();}
    public char getSign() {return repo.getSign();}
    public char getIsGroupFlag() {return repo.getIsGroup();}
    public Long getParentId() {return repo.getParent().getId();}
}
