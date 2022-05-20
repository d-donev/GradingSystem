package mk.ukim.finki.gradingsystem.service;

import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;

import java.util.List;

public interface StudentActivityPointsService {
    List<StudentActivityPoints> findAll();
    List<StudentActivityPoints> saveAll(List<StudentActivityPoints> listStudents);
    List<StudentActivityPoints> filterDuplicates(List<StudentActivityPoints> studentActivityPoints);
    List<StudentActivityPoints> filterStudentActivityPoints(String searchText, Long activityCode);
    void editStudentPoints(Long id, Double points);
    StudentActivityPoints findById(Long id);
}
