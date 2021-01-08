package ru.alwertus.alwserv.management_apps.cfg;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/applications/config")
public class AppsConfigRestController {
    AppsConfig cfg;

    @Autowired
    public AppsConfigRestController(AppsConfig cfg) {
        this.cfg = cfg;
    }

    @PostMapping("/reload")
    public String reloadConfig() {
        log.info("reload config");

        cfg.reloadFile();

        JSONObject rs = new JSONObject();
        rs.put("result", "OK");
        return rs.toString();
    }
}
