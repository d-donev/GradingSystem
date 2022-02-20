package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.User;

import java.util.List;

public interface StudentService {
    Student findById(Integer id);
    List<Student> listAll();
    Student create(Integer index, String name, String surname, Role role, Long courseId);
    Student edit(Integer index, String name, String surname, List<Long> courseId);
    Student deleteStudent(Integer index);
    Student addStudentToCourse(Integer index, Long courseId);
}
