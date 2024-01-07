package live.codeland.petsguidesbackend.service;

import live.codeland.petsguidesbackend.helpers.dto.UserListDto;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserListDto getAllUser(int page, int limit, String sortBy, String orderBy) {
       try {

           PageRequest pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(orderBy))));
           Page<User> getAllUser = userRepository.findAll(pageable);

           List<User> userList = getAllUser.getContent();

           long total = userList.isEmpty()? 0: getAllUser.getTotalElements();
           int totalPages = userList.isEmpty()? 0: getAllUser.getTotalPages();
           int currentPage = userList.isEmpty()? 1: getAllUser.getNumber() + 1;
           Integer nextPage = getAllUser.hasNext() ? getAllUser.getNumber() + 2 : null;
           Integer prevPage = getAllUser.hasPrevious() && !userList.isEmpty() ? getAllUser.getNumber() : null;
            return new UserListDto(userList, total, totalPages, currentPage, nextPage, prevPage);

       } catch (Exception exception){
           String message = "Catch in service getAllUser:  " + exception.getMessage();
           throw new RuntimeException(message);
       }

    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }


    public User updateUser(String id, User user) {
        if (userRepository.existsById(id)) {
            return userRepository.save(user);
        }
        return null;
    }

    public User softDeleteUser(String id) {
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser != null) {
                existingUser.setDeleted(true);
                existingUser.setDeletedAt(LocalDateTime.now());
                return userRepository.save(existingUser);
            } else {
                return null;
            }
        } catch (Exception exception) {
            String message = "Catch in service softDeleteUser:  " + exception.getMessage();
            throw new RuntimeException(message);
        }
    }
}




