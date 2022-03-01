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
}
