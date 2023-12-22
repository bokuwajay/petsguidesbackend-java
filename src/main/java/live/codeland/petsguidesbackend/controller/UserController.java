package live.codeland.petsguidesbackend.controller;

import jakarta.validation.Valid;
import live.codeland.petsguidesbackend.helpers.dto.UserListDto;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public User createUser(@Valid @RequestBody User user) {
        try {
            String userPassword = user.getPassword();
            System.out.println("now pass--------" + userPassword);
            String encodedPassword = passwordEncoder.encode(userPassword);
            System.out.println("hashed password-------" + encodedPassword);
            user.setPassword(encodedPassword);
            return userService.createUser(user);
        } catch (Exception exception){
            System.out.println("validate-------" + exception);
        }

        return userService.createUser(user);
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy
    ) {
    try {
        UserListDto userResponse = userService.getAllUser(page, limit, sortBy, orderBy);
        if(!userResponse.getUserList().isEmpty()){
            String message = "Successfully get all users";
            HttpStatus status = HttpStatus.OK;
            ApiResponse response = new ApiResponse(status, 200, userResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        } else {
            String message = "No user found";
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiResponse response = new ApiResponse(status, 404, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
    } catch (Exception exception){
        String message = "Catch in controller getAllUser: " + exception.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse response = new ApiResponse(status, 500, null, message, LocalDateTime.now());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }
    }

    @GetMapping("/profile/{id}")
    public User getUserById(@PathVariable String id) {

        return userService.getUserById(id).orElse(null);
    }


    @PatchMapping("/profile/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<ApiResponse> softDeleteUser(@PathVariable String id) {
        try {
            User deletedUser = userService.softDeleteUser(id);
            if (deletedUser != null) {
                String message = "Successfully deleted";
                HttpStatus status = HttpStatus.OK;
                ApiResponse response = new ApiResponse(status, 200, deletedUser, message, LocalDateTime.now());
                return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
            } else {
                String message = "Cannot found the user by id";
                HttpStatus status = HttpStatus.NOT_FOUND;
                ApiResponse response = new ApiResponse(status, 404, null, message, LocalDateTime.now());
                return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
            }
        } catch (Exception exception) {
            String message = "Catch in controller softDeleteUser: " + exception.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse response = new ApiResponse(status, 500, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
    }
}
