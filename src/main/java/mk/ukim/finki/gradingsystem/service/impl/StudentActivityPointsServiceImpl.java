package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentActivityPointsRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.StudentActivityPointsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentActivityPointsServiceImpl implements StudentActivityPointsService {

    private final StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA;

    public StudentActivityPointsServiceImpl(StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA) {
        this.studentActivityPointsRepositoryJPA = studentActivityPointsRepositoryJPA;
    }

    @Override
    public List<StudentActivityPoints> findAll() {
        return studentActivityPointsRepositoryJPA.findAll();
    }

    @Override
    public List<StudentActivityPoints> saveAll(List<StudentActivityPoints> listStudents) {
        return this.studentActivityPointsRepositoryJPA.saveAll(listStudents);
    }

    @Override
    public List<StudentActivityPoints> filterDuplicates(List<StudentActivityPoints> studentActivityPoints) {

        List<StudentActivityPoints> allStudentActivityPoints = studentActivityPointsRepositoryJPA.findAll();

        for (int i=0; i<allStudentActivityPoints.size();i++) {
            for (int j=0;j<studentActivityPoints.size();j++) {
                if (allStudentActivityPoints.get(i).getIndex().equals(studentActivityPoints.get(j).getIndex()) &&
                    allStudentActivityPoints.get(i).getCode().equals(studentActivityPoints.get(j).getCode())) {
                    allStudentActivityPoints.get(i).setPoints(studentActivityPoints.get(j).getPoints());
                    studentActivityPoints.remove(j);
                }
            }
        }

        return studentActivityPoints;
    }
}
