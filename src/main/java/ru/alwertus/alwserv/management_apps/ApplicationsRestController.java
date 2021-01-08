package ru.alwertus.alwserv.management_apps;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.management_apps.cfg.AppsConfig;

import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationsRestController {
    final AppsConfig cfg;

    @Autowired
    public ApplicationsRestController(AppsConfig cfg) {
        this.cfg = cfg;
    }

    @PostMapping("/status")
    public List<LinuxProcessData> getAllStatuses() {
        log.info("Get statuses");
        return cfg.getApplicationsList();
    }

    @PostMapping({"/{action}/{id}"})
    public String runAction(/*@RequestBody String requestBody*/@PathVariable String action, @PathVariable int id) {
//        JSONObject rq = new JSONObject(requestBody);
//
//        String program = (String) rq.get("program");
//        String action = (String) rq.get("action");

//        log.info(id + " - " + action);


        LinuxProcess proc =
                (Objects.requireNonNull(cfg.getApplicationsList().stream()
                        .filter(serverProcessRest -> id == serverProcessRest.getId())
                        .findAny())
                        .get().sp);

        JSONObject rs = new JSONObject();

        String result = "OK";

        try {
            if (action.equals("start")) proc.start();
            if (action.equals("stop")) proc.stop();
        } catch (Exception e) {
            result = "ERROR";
            rs.put("error", e.getMessage());
        }

        rs.put("result", result);
        return rs.toString();
    }

}
