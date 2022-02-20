package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> listAll();
    List<User> search(String text);
}
