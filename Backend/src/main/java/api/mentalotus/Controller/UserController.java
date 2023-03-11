package api.mentalotus.Controller;

import api.mentalotus.Domain.User;
import api.mentalotus.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {this.userService = userService;}

    @PostMapping(path = "/signin")
    public ResponseEntity<User> signin(@RequestBody String accessToken)
    {
        User user = userService.Authenticate(accessToken);
        if(user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        else return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/signup")
    public User signup(@RequestBody User user)
    {
        userService.save(user);
        return user;
    }

    @DeleteMapping(path = "/users/{userKey}")
    public ResponseEntity<String> delete(@PathVariable("userKey") String userKey)
    {
        userService.delete(userKey);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 회원이 탈퇴 되었습니다.");
    }
}
