package ru.alwertus.alwserv.management_apps.cfg;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.alwertus.alwserv.management_apps.LinuxProcessData;
import ru.alwertus.alwserv.management_apps.LinuxProcessHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Log4j2
@Component
public class AppsConfig {
    private static final String FILENAME = "programs.cfg";
    private static final String PREFIX = "app";

    @Getter
    private List<LinuxProcessData> applicationsList;

    public AppsConfig() {
        reloadFile();
    }

    public void reloadFile() {
        applicationsList = new ArrayList<>();
        try ( InputStream in = new FileInputStream(FILENAME)){
            Properties props = new Properties();
            props.load(in);

            int appCount = Integer.parseInt(props.getProperty("count", "0"));

            for (int i = 1; i <= appCount; i++) {
                String currentApp = PREFIX + i + ".";
                applicationsList.add(
                        new LinuxProcessData(
                                props.getProperty(currentApp + "title"),
                                new LinuxProcessHandler(
                                        props.getProperty(currentApp + "find"),
                                        props.getProperty(currentApp + "start"),
                                        props.getProperty(currentApp + "stop")
                                )));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
