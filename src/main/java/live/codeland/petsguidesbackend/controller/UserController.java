package live.codeland.petsguidesbackend.controller;

import live.codeland.petsguidesbackend.helpers.dto.PaginationDto;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse<PaginationDto<User>>> getAllUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy) {
        try {
            PaginationDto<User> userResponse = userService.getAllUser(page, limit, sortBy, orderBy);
            ApiResponse<PaginationDto<User>> response;
            if (!userResponse.getList().isEmpty()) {
                response = new ApiResponse<>(HttpStatus.OK, 200, userResponse, "Successfully get all users",
                        LocalDateTime.now());
            } else {
                response = new ApiResponse<>(HttpStatus.NOT_FOUND, 404, null, "No user found", LocalDateTime.now());
            }
            return response.toClient();
        } catch (Exception exception) {
            ApiResponse<PaginationDto<User>> exceptionResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    500, null, "Catch in controller getAllUser: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
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
            ApiResponse<User> response;
            if (deletedUser != null) {
                response = new ApiResponse<>(HttpStatus.OK, 200, deletedUser, "Successfully deleted a user",
                        LocalDateTime.now());
            } else {
                response = new ApiResponse<>(HttpStatus.NOT_FOUND, 404, null, "Cannot found the user by this id",
                        LocalDateTime.now());
            }
            return response.toClient();
        } catch (Exception exception) {
            ApiResponse<User> exceptionResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, 500, null,
                    "Catch in controller softDeleteUser: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }
    }
}
