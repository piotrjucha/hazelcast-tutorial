package pl.devopsi.hazelcasttutorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.devopsi.hazelcasttutorial.exception.UserNotFoundException;
import pl.devopsi.hazelcasttutorial.model.User;
import pl.devopsi.hazelcasttutorial.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/" + Paths.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @GetMapping(
            path = Paths.Users.GET_SINGLE_USER,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User getUser(@NotBlank
                                      @PathVariable("pesel") final String pesel) throws UserNotFoundException {
        return userService.getUser(pesel);
    }

    @PutMapping(
            path = Paths.Users.ADD_SINGLE_USER
    )
    public void addUser(@NotBlank @RequestBody @Valid User user) {
        userService.addUser(user);
    }

    @PostMapping(path = Paths.Users.UPDATE_SINGLE_USER,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@NotBlank @RequestBody @Valid User user) throws UserNotFoundException {
        userService.updateUser(user);
    }
}
