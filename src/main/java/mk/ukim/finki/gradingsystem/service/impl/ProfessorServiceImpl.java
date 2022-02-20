package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.exceptions.CourseNotFoundException;
import mk.ukim.finki.gradingsystem.exceptions.ProfessorNotFoundException;
import mk.ukim.finki.gradingsystem.model.Course;
import mk.ukim.finki.gradingsystem.model.Professor;
import mk.ukim.finki.gradingsystem.model.User;
import mk.ukim.finki.gradingsystem.repositoryJPA.CourseRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.ProfessorRepositiryJPA;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepositiryJPA professorRepositiryJPA;
    private final CourseRepositoryJPA courseRepositoryJPA;

    public ProfessorServiceImpl(ProfessorRepositiryJPA professorRepositiryJPA, CourseRepositoryJPA courseRepositoryJPA) {
        this.professorRepositiryJPA = professorRepositiryJPA;
        this.courseRepositoryJPA = courseRepositoryJPA;
    }

    @Override
    public Professor findByEmail(String email) {
        return this.professorRepositiryJPA.findById(email).orElseThrow(() -> new ProfessorNotFoundException(email));
    }

    @Override
    public List<Professor> listAll() {
        return this.professorRepositiryJPA.findAll();
    }

    @Override
    public Professor create(String email, String name, String surname, Role role, Long courseId) {
        Course course = this.courseRepositoryJPA.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        if(this.professorRepositiryJPA.findById(email).isPresent()){
            Professor professor = this.professorRepositiryJPA.findById(email).orElseThrow(() -> new ProfessorNotFoundException(email));
            professor.getUser().getCourseList().add(course);
            return this.professorRepositiryJPA.save(professor);
        }
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        User user = new User(name, surname, role, courses);
        Professor professor = new Professor(email, user);
        return this.professorRepositiryJPA.save(professor);
    }

    @Override
    public Professor edit(String email, String name, String surname, List<Long> courseId) {
        List<Course> courses = this.courseRepositoryJPA.findAllById(courseId);
        Professor professor = this.findByEmail(email);
        professor.getUser().setName(name);
        professor.getUser().setSurname(surname);
        professor.getUser().setCourseList(courses);
        return this.professorRepositiryJPA.save(professor);
    }

    @Override
    public Professor delete(String email) {
        Professor professor = this.findByEmail(email);
        this.professorRepositiryJPA.delete(professor);
        return professor;
    }

    @Override
    public Professor addCourseToProfessor(String email, Long courseId) {
        Professor professor = this.findByEmail(email);
        Course course = this.courseRepositoryJPA.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        professor.getUser().getCourseList().add(course);
        return this.professorRepositiryJPA.save(professor);
    }
}
