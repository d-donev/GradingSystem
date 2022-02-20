package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Professor;

import java.util.List;

public interface ProfessorService {
    Professor findByEmail(String email);
    List<Professor> listAll();
    Professor create(String email, String name, String surname, Role role, Long courseId);
    Professor edit(String email, String name, String surname, List<Long> courseId);
    Professor delete(String email);
    Professor addCourseToProfessor(String email, Long courseId);
}
