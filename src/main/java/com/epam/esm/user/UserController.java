package com.epam.esm.user;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Class UserController which contain method related with User
 *
 * @author Vlad Storoshchuk
 */
@Validated
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserHateoasMapper userHateoasMapper;

    public UserController(UserService userService, UserHateoasMapper userHateoasMapper) {
        this.userService = userService;
        this.userHateoasMapper = userHateoasMapper;
    }

    /**
     * A controller get method for getting all users
     *
     * @param page - number of page (min value 0)
     * @param size - count of tags (min value 1)
     * @see UserService#getAllUsers(int, int)
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "0")
                                         @Min(value = 0, message = "Page index should be >= 0.") int page,
                                         @RequestParam(value = "size", defaultValue = "10")
                                         @Min(value = 1, message = "Size should be should be >= 1.") int size) {
        Page<User> allUsers = userService.getAllUsers(page, size);
        PagedModel<User> allUsersPagedModel = userHateoasMapper
                .getAllUsersHateoas(allUsers);
        return ResponseEntity.ok(Map.of("users", allUsersPagedModel));
    }

    /**
     * A controller get method for getting user by id
     *
     * @param user - user
     * @see UserService#getUserById(long)
     */
    @GetMapping("by-user-id")
    public ResponseEntity<?> getUserById(@AuthenticationPrincipal User user) {
        User userById = userService.getUserById(user.getId());
        CollectionModel<User> userByIdModel = userHateoasMapper
                .getUserByIdHateoas(userById);
        return ResponseEntity.ok(Map.of("user", userByIdModel));
    }

    /**
     * A controller get method for getting user by id (Admin Tool)
     *
     * @param id - user id (min value 0)
     * @see UserService#getUserById(long)
     */
    @GetMapping("by-user-id-for-admin/{id}")
    public ResponseEntity<?> getUserByIdAdminTool(@PathVariable("id")
                                                  @Min(value = 1, message = "Id should be >= 1.") long id) {
        User user = userService.getUserById(id);
        CollectionModel<User> userByIdModel = userHateoasMapper
                .getUserByIdAdminToolHateoas(user);
        return ResponseEntity.ok(Map.of("user", userByIdModel));
    }
}