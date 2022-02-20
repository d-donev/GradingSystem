package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.exceptions.UserNotFoundException;
import mk.ukim.finki.gradingsystem.model.User;
import mk.ukim.finki.gradingsystem.repositoryJPA.UserRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryJPA userRepositoryJPA;

    public UserServiceImpl(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public User findById(Long id) {
        return this.userRepositoryJPA.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> listAll() {
        return this.userRepositoryJPA.findAll();
    }

    @Override
    public List<User> search(String text) {
        return this.userRepositoryJPA.findAllByNameContainingOrSurnameContaining(text, text);
    }
}
