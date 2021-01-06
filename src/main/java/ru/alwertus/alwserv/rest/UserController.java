package ru.alwertus.alwserv.rest;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alwertus.alwserv.auth.Role;
import ru.alwertus.alwserv.auth.Status;
import ru.alwertus.alwserv.auth.User;
import ru.alwertus.alwserv.auth.UserRepository;

@Log4j2
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/current")
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email " + authentication.getName() + " not found"));

        JSONObject rs = new JSONObject();
        rs.put("FIRSTNAME", user.getFirstName());
        rs.put("LASTNAME", user.getLastName());
        rs.put("AUTHORITIES", authentication.getAuthorities());
        return rs.toString();
    }

    @PostMapping("/create")
    public String createNewUser(@RequestBody String body) {
        JSONObject rq = new JSONObject(body);
        JSONObject rs = new JSONObject();

        try {
            if (userRepository
                    .findByEmail(rq.getString("email"))
                    .isPresent())
                throw new RuntimeException(String.format("User with email=%s already exists", rq.getString("email")));

            User newUser = new User();
            newUser.setEmail(rq.getString("email"));
            newUser.setFirstName(rq.getString("firstname"));
            newUser.setLastName(rq.getString("lastname"));
            newUser.setEmail(rq.getString("email"));
            newUser.setPassword(passwordEncoder.encode(rq.getString("password")));
            newUser.setRole(Role.USER);
            newUser.setStatus(Status.ACTIVE);

            userRepository.save(newUser);

            rs.put("result", "OK");
        } catch (Exception e) {
            rs.put("result", "Error");
            rs.put("error", e.getMessage());
        }
        return rs.toString();
    }
}
