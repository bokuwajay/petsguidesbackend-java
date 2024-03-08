package live.codeland.petsguidesbackend.controller;

import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/all-profiles")
    public ResponseEntity<ApiResponseDto<PaginationDto<User>>> getAllUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy) {

        return super.findAll(page, limit, sortBy, orderBy);

    }

    @GetMapping("/single-profile/{id}")
    public ResponseEntity<ApiResponseDto<Optional<User>>> getUserById(@PathVariable("id") String id) {

        return super.findById(id);
    }

    //save all (no need for User)

    // save 1  (no need, This is registration)


    // update all
    @PatchMapping("/profiles-list")
     public ResponseEntity<ApiResponseDto<List<User>>> updateUserList(@RequestBody List<User> users) {

        return super.updateAll(users);

    }

    // update 1
    @PatchMapping("/single-profile/{id}")
    public ResponseEntity<ApiResponseDto<User>> updateUser(@RequestBody User user, @PathVariable("id") String id){
        return super.updateOne(user,id);
    }

    // soft delete all
    @DeleteMapping("/deletion/profiles-list")
    public ResponseEntity<ApiResponseDto<List<User>>> softDeleteUserList(@RequestBody List<String> idList){
        return super.softDeleteAll(idList);
    }

    //  soft delete 1
    @DeleteMapping("/deletion/single-profile/{id}")
    public ResponseEntity<ApiResponseDto<User>> softDeleteUserById(@PathVariable String id) {

        return super.softDeleteOne(id);
    }

//    @PatchMapping("/profile/{id}")
//    public User updateUser(@PathVariable String id, @RequestBody User user) {
//
//        return userService.updateUser(id, user);
//    }

}
