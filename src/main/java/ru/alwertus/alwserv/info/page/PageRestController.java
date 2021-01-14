package ru.alwertus.alwserv.info.page;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/info")
public class PageRestController {

    private final PageCurdService curdService;

    @Autowired
    public PageRestController(PageCurdService curdService) {
        this.curdService = curdService;
    }

    @PostMapping()
    public String requestHandler(@RequestBody String body) {
        JSONObject rs = new JSONObject();

        try {
            JSONObject rq = new JSONObject(body);

            Long id;

            String operation = rq.getString("Operation");
            switch (operation) {
                case "Get":
                    id = rq.getLong("Id");
                    rs.put("Html", curdService.getPage(id));
                    break;
                case "Set":
                    id = rq.getLong("Id");
                    String html = rq.getString("Html");
                    curdService.setPage(id, html);
                    break;
                default:
                    throw new RuntimeException("Unknow operation");
            }
            rs.put("Result", "OK");
        } catch (Exception e) {
            rs = new JSONObject();
            rs.put("Result", "Error");
            rs.put("Error", e.getMessage());
        }

        return rs.toString();
    }
}