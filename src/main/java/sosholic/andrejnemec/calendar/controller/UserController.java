package sosholic.andrejnemec.calendar.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.mappers.StatusMapper;
import sosholic.andrejnemec.calendar.mappers.UserMapper;
import sosholic.andrejnemec.calendar.mappers.exceptions.BadPasswordException;
import sosholic.andrejnemec.calendar.mappers.exceptions.BadUsernameException;
import sosholic.andrejnemec.calendar.mappers.payload.UserPayloadMapper;
import sosholic.andrejnemec.calendar.security.user.UserPrincipal;
import sosholic.andrejnemec.calendar.service.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity getUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        User user = this.userService.findUserById(currentUser.getId());
        if (user == null)
            return new ResponseEntity(new StatusMapper(404, "The current logged user not found!"), HttpStatus.NOT_FOUND);
        return new ResponseEntity(UserMapper.map(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity getUser(@PathVariable("id") long id) {
        User user = this.userService.findUserById(id);
        if (user == null) return new ResponseEntity(new StatusMapper(404, "The user not found!"), HttpStatus.NOT_FOUND);
        return new ResponseEntity(UserMapper.map(user), HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(UserMapper.map(this.userService.findAllUsers()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserPayloadMapper userPayloadMapper) {
        try {
            User user = this.userService.save(UserPayloadMapper.map(userPayloadMapper));
            if (user != null) return new ResponseEntity(new StatusMapper(200, "The user successfully registered!"), HttpStatus.OK);
            else return new ResponseEntity(new StatusMapper(500, "Choose other name!"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadPasswordException e) {
            return new ResponseEntity(new StatusMapper(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (BadUsernameException e) {
            return new ResponseEntity(new StatusMapper(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
