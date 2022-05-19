package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.StudentNotFoundException;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.User;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.UserRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.StudentService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepositoryJPA studentRepositoryJPA;
    private final CourseRepositoryJPA courseRepositoryJPA;
    private final UserRepositoryJPA userRepositoryJPA;

    public StudentServiceImpl(StudentRepositoryJPA studentRepositoryJPA, CourseRepositoryJPA courseRepositoryJPA, UserRepositoryJPA userRepositoryJPA) {
        this.studentRepositoryJPA = studentRepositoryJPA;
        this.courseRepositoryJPA = courseRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public Student findById(Integer index) {
        return this.studentRepositoryJPA.findById(index).orElseThrow(() -> new StudentNotFoundException(index));
    }

    @Override
    public List<Student> listAll() {
        return this.studentRepositoryJPA.findAll();
    }

    @Override
    public Student create(Integer index, String name, String surname, Role role, Long courseId) {
        Course course = this.courseRepositoryJPA.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        if (this.findById(index) != null) {
            Student student = this.findById(index);
            student.getUser().getCourseList().add(course);
            course.getStudentList().add(student);
            return this.studentRepositoryJPA.save(student);
        }
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        User user = new User(name, surname, role, courses);
        Student student = new Student(index, user);
        course.getStudentList().add(student);
        return this.studentRepositoryJPA.save(student);
    }

    @Override
    @Transactional
    public Student createManual(Integer index, String name, String surname, Role role) {
        List<Course> courses = new ArrayList<>();
        User user = new User(name, surname, role,courses);
        this.userRepositoryJPA.save(user);
        Student student = new Student(index, user);
        return this.studentRepositoryJPA.save(student);
    }

    @Override
    public Student edit(Integer index, String name, String surname) {
        Student student = findById(index);
        student.getUser().setName(name);
        student.getUser().setSurname(surname);
        return this.studentRepositoryJPA.save(student);
    }

    @Override
    public Student deleteStudent(Integer index) {
        Student student = this.findById(index);
        this.studentRepositoryJPA.delete(student);
        return student;
    }

    @Override
    public Student addStudentToCourse(Integer index, Long courseId) {
        Student student = this.findById(index);
        Course course = this.courseRepositoryJPA.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        student.getUser().getCourseList().add(course);
        course.getStudentList().add(student);
        return this.studentRepositoryJPA.save(student);
    }
}
