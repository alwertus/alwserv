package ru.alwertus.alwserv.management_apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LinuxTerminal {

    List<String> runCommandSHReadResults(String command) {
        return runCommandReadResults("/bin/sh", "-c", command);
    }

    List<String> runCommandReadResults(String ...command) {
        List<String> results = new ArrayList<>();
        try {
            Process p = new ProcessBuilder(command).start();
//            log.info("PROCESS: " + p.info());
            results = readResults(p.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error run command: " + e.getMessage());
        }
//        log.info("RESULTS RUN COMMAND BEGIN ----------------------");
//        results.forEach(System.out::println);
//        log.info("RESULTS RUN COMMAND END ----------------------");
        return results;
    }

    List<String> readResults(InputStream in) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Error read results. " + e.getMessage());
        }

        return lines;
    }
}
