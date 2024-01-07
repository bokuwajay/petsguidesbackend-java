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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }



    @GetMapping("/all")
    public ResponseEntity<ApiResponse<UserListDto>> getAllUser(
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
            ApiResponse<UserListDto> response = new ApiResponse<>(status, 200, userResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        } else {
            String message = "No user found";
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiResponse<UserListDto> response = new ApiResponse<>(status, 404, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
    } catch (Exception exception){
        String message = "Catch in controller getAllUser: " + exception.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<UserListDto> response = new ApiResponse<>(status, 500, null, message, LocalDateTime.now());
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
    public ResponseEntity<ApiResponse<User>> softDeleteUser(@PathVariable String id) {
        try {
            User deletedUser = userService.softDeleteUser(id);
            if (deletedUser != null) {
                String message = "Successfully deleted";
                HttpStatus status = HttpStatus.OK;
                ApiResponse<User> response = new ApiResponse<>(status, 200, deletedUser, message, LocalDateTime.now());
                return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
            } else {
                String message = "Cannot found the user by id";
                HttpStatus status = HttpStatus.NOT_FOUND;
                ApiResponse<User> response = new ApiResponse<>(status, 404, null, message, LocalDateTime.now());
                return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
            }
        } catch (Exception exception) {
            String message = "Catch in controller softDeleteUser: " + exception.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse<User> response = new ApiResponse<>(status, 500, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
    }
}
