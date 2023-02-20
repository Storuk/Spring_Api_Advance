package com.epam.esm.user;

import com.epam.esm.exceptions.ItemNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author Vlad Storoshchuk
 */
@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * A service method for getting User by id
     *
     * @param id User id
     * @return User
     * @see UserRepo#findById(Object) for getting User
     */
    public User getUserById(long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new ItemNotFoundException("There are no user with id = " + id));
    }

    /**
     * A service method for getting all Users
     *
     * @param page number of page (min value 0)
     * @param size count of Tags (min value 1)
     * @return Page of Users
     * @see UserRepo#findAll() for getting all Users
     */
    public Page<User> getAllUsers(int page, int size) {
        return userRepo.findAll(PageRequest.of(page, size));
    }
}