package ru.alwertus.alwserv.management_apps;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
public class LinuxProcessHandler extends LinuxTerminal implements LinuxProcess {
    private final String findString;
    private final String startScript;
    private final String stopScript;

    public LinuxProcessHandler(String findString, String startScript, String stopScript) {
        this.findString = findString;
        this.startScript = startScript;
        this.stopScript = stopScript;
    }

    @Override
    public boolean isExists() {
        return getCmdline().length() > 0;
    }

    @Override
    public String getStatus() {
        return null;
    }


    /** @return
     *      0 -> not found
     *      -1 -> error
     *      -2 -> too many results
     *      or process PID
     */
    @Override
    public int getPID() {
        List<String> results = runCommandReadResults("pgrep", "-f", findString);

        if (results.size() == 0) return 0;

        if (results.size() > 1) return -2;

        int resultInt;
        try {
            resultInt = Integer.parseInt(results.get(0));
        } catch (NumberFormatException e) {
            resultInt = -1;
        }

        return resultInt;
    }

    @Override
    public String getCmdline() {
        int pid = getPID();
        if (pid < 1) return "";

        List<String> results = runCommandReadResults("/bin/sh", "-c", "ps -q " + pid + " -o command");

        return results.size() > 1
                ? results.get(1)
                : (results.size() == 1
                    ? results.get(0)
                    : "");
    }

    @Override
    public void start() {
        boolean addSH = startScript.contains("zzzSHzzz");

        String startComand = replaceAllOccurrences(startScript, 0);

        log.info("START: << " + startComand + " >>");

        if (startScript.equals(""))
            throw new NullPointerException("Script 'START' not defined");
//        log.info("RUN COMMAND NEXT> " + startComand);
        if (!isExists())
            if (addSH)
                runCommandSHReadResults(startComand);
            else
                runCommandReadResults(startComand);
    }

    @Override
    public void stop() {
        int pid = getPID();
        if (pid < 1) throw new NullPointerException("PID " + pid + " not exists");

        if (stopScript.equals("zzzKILLzzz")) {
            Optional<ProcessHandle> p = ProcessHandle.of(pid);
            p.ifPresent(ProcessHandle::destroy);
            log.info("STOP application PID=" + pid + " programmatically");
            return;
        }

        boolean addSH = startScript.contains("zzzSHzzz");

        String stopCommand = replaceAllOccurrences(stopScript, pid);
        log.info("STOP: " + stopCommand);

        if (stopScript.equals(""))
            throw new NullPointerException("Script 'STOP' not defined");
        if (isExists())
            if (addSH)
                runCommandSHReadResults(stopCommand);
            else
                runCommandReadResults(stopCommand);
    }

    private String replaceAllOccurrences(String rawString, int pid) {
        return rawString
                .replaceAll("zzzPIDzzz", String.valueOf(pid))
                .replaceAll("zzzSHzzz", "");
    }

    @Override
    public String toString() {
        return "PID=" + getPID() + (isExists() ? " (alive)" : " (die)") + " | Status=" + getStatus() + " | CMD=" + getCmdline();
    }
}
