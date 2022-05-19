package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.model.Grades;
import mk.ukim.finki.gradingsystem.model.Student;

import java.util.List;

public interface GradesService {
    void updatePoints(List<Student> students, Long courseId);
}
