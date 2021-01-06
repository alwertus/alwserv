package ru.alwertus.alwserv.rest;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/test")
public class TempRest {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(method= RequestMethod.POST, path="login/{id}")
    public String request(@PathVariable String id) {
        System.out.println("TEST REQUEST. ID=" + id);

        JSONObject rs = new JSONObject();
        rs.put("METHOD", "login+id");
        rs.put("RESULT", id);
        return rs.toString();
    }

    @PostMapping("/login")
    public String trylogin(@RequestBody String body) {
        JSONObject rq = new JSONObject(body);
        log.info(rq.toString());

        JSONObject rs = new JSONObject();
        rs.put("METHOD", "login");
        rs.put("RESULT", "OK");
        return rs.toString();
    }

    @PostMapping("/loginauth")
    public String tryloginAuth(@RequestBody String body) {
        JSONObject rq = new JSONObject(body);
        log.info(rq.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(currentPrincipalName);


        JSONObject rs = new JSONObject();
        rs.put("METHOD", "loginauth");
        rs.put("RESULT", authentication.getName());
        rs.put("AUTHORITIES", authentication.getAuthorities());
        return rs.toString();
    }

    @PostMapping("/loginauthadmin")
    public String tryloginAuthAdmin(@RequestBody String body) {
        JSONObject rq = new JSONObject(body);
        log.info(rq.toString());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(currentPrincipalName);


        JSONObject rs = new JSONObject();
        rs.put("METHOD", "loginauth");
        rs.put("RESULT", authentication.getName());
        rs.put("AUTHORITIES", authentication.getAuthorities());
        return rs.toString();
    }
}
