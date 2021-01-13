package ru.alwertus.alwserv.info.list;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/infolist")
public class InfoListRestController {

    private final InfoCurdService infoCurdService;

    @Autowired
    public InfoListRestController(InfoCurdService infoCurdService) {
        this.infoCurdService = infoCurdService;
    }

/*    public void test() {
        InfoStructure newRecord = new InfoStructure();
        newRecord.setTitle("TEST TITLE");
        newRecord.setCreator(currentUser.getCurrentUser());
        log.error(newRecord);
        infoReposiroty.save(newRecord);
    }*/

    @PostMapping()
    public String requestParser(@RequestBody String body) {

        JSONObject rs = new JSONObject();
        try {
            JSONObject rq = new JSONObject(body);

            String operation = rq.getString("Operation");
            switch (operation) {
                case "Upsert":
                    Long id;
                    try {
                        id = rq.getLong("Id");
                    } catch (JSONException e) {
                        id = -1L;
                    }

                    Long newParent;
                    try {
                        newParent = rq.getLong("Parent");
                    } catch(JSONException e) {
                        newParent = null;
                    }

                    String newTitle;
                    try {
                        newTitle = rq.getString("Title");
                    } catch(JSONException e) {
                        newTitle = null;
                    }

                    if (id <= 0)
                        id = infoCurdService.create(
                            newParent,
                            newTitle);
                    else {
                        infoCurdService.update(
                                id,
                                newParent,
                                newTitle
                        );
                    }

                    rs.put("Id", id);
                    rs.put("PRIVATE", infoCurdService.getAll(InfoAccess.PRIVATE));
                    break;
                case "GetAll":
                    rs.put("PRIVATE", infoCurdService.getAll(InfoAccess.PRIVATE));
                    rs.put("PUBLIC", infoCurdService.getAll(InfoAccess.PUBLIC));
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
