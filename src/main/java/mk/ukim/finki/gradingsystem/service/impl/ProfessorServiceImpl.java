package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Professor;
import mk.ukim.finki.gradingsystem.model.User;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.ProfessorRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.UserRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.ProfessorService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepositoryJPA professorRepositoryJPA;
    private final CourseRepositoryJPA courseRepositoryJPA;
    private final UserRepositoryJPA userRepositoryJPA;

    public ProfessorServiceImpl(ProfessorRepositoryJPA professorRepositoryJPA, CourseRepositoryJPA courseRepositoryJPA, UserRepositoryJPA userRepositoryJPA) {
        this.professorRepositoryJPA = professorRepositoryJPA;
        this.courseRepositoryJPA = courseRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public Professor findByEmail(String email) {
        return this.professorRepositoryJPA.findById(email).orElseThrow(() -> new ProfessorNotFoundException(email));
    }

    @Override
    public List<Professor> listAll() {
        return this.professorRepositoryJPA.findAll();
    }

    @Override
    @Transactional
    public Professor create(String email, String name, String surname, Role role, List<Long> courseId) {
        List<Course> courses = this.courseRepositoryJPA.findAllById(courseId);
        User user = new User(name, surname, role, courses);
        userRepositoryJPA.save(user);
        Professor professor = new Professor(email, user);
        return this.professorRepositoryJPA.save(professor);
    }

    @Override
    public Professor edit(String email, String name, String surname, List<Long> courseId) {
        List<Course> courses = this.courseRepositoryJPA.findAllById(courseId);
        Professor professor = this.findByEmail(email);
        professor.getUser().setName(name);
        professor.getUser().setSurname(surname);
        professor.getUser().setCourseList(courses);
        return this.professorRepositoryJPA.save(professor);
    }

    @Override
    public Professor delete(String email) {
        Professor professor = this.findByEmail(email);
        this.professorRepositoryJPA.delete(professor);
        return professor;
    }

    @Override
    public Professor addCourseToProfessor(String email, Long courseId) {
        Professor professor = this.findByEmail(email);
        Course course = this.courseRepositoryJPA.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        professor.getUser().getCourseList().add(course);
        return this.professorRepositoryJPA.save(professor);
    }
}
