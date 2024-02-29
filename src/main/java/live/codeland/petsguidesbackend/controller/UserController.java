package live.codeland.petsguidesbackend.controller;

import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController extends BaseController<User, String>{
    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<PaginationDto<User>>> getAllUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy) {

        return super.findAll(page, limit, sortBy, orderBy);

    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<Optional<User>>> getUserById(@PathVariable String id) {

        return super.findById(id);
    }

    //save all

    // save 1


    // update all
        @PatchMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<User>> updateUser(@PathVariable String id, @RequestBody User user) {

        return super.updateOne()

    }

    // update 1

    // soft delete all


    //  soft delete 1
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<User>> softDeleteUserById(@PathVariable String id) {

        return super.softDeleteOne(id);
    }

//    @PatchMapping("/profile/{id}")
//    public User updateUser(@PathVariable String id, @RequestBody User user) {
//
//        return userService.updateUser(id, user);
//    }

}
