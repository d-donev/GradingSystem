package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.model.*;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.GradesRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentActivityPointsRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.StudentActivityPointsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentActivityPointsServiceImpl implements StudentActivityPointsService {

    private final StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA;
    private final StudentRepositoryJPA studentRepositoryJPA;
    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final GradesRepositoryJPA gradesRepositoryJPA;

    public StudentActivityPointsServiceImpl(StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA, StudentRepositoryJPA studentRepositoryJPA, ActivityRepositoryJPA activityRepositoryJPA, GradesRepositoryJPA gradesRepositoryJPA) {
        this.studentActivityPointsRepositoryJPA = studentActivityPointsRepositoryJPA;
        this.studentRepositoryJPA = studentRepositoryJPA;
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.gradesRepositoryJPA = gradesRepositoryJPA;
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

        for (int i = 0; i < allStudentActivityPoints.size(); i++) {
            for (int j = 0; j < studentActivityPoints.size(); j++) {
                if (allStudentActivityPoints.get(i).getIndex().equals(studentActivityPoints.get(j).getIndex()) &&
                        allStudentActivityPoints.get(i).getCode().equals(studentActivityPoints.get(j).getCode())) {
                    allStudentActivityPoints.get(i).setPoints(studentActivityPoints.get(j).getPoints());
                    studentActivityPoints.remove(j);
                }
            }
        }

        return studentActivityPoints;
    }

    @Override
    public List<StudentActivityPoints> filterStudentActivityPoints(String searchText, Long activityCode) {
        List<StudentActivityPoints> currentStudentsAtActivity = studentActivityPointsRepositoryJPA.findAll()
                .stream().filter(x -> x.getCode().equals(activityCode))
                .collect(Collectors.toList());
        return currentStudentsAtActivity.stream().filter(x -> x.getIndex().toString().contains(searchText)).collect(Collectors.toList());
    }

    @Override
    public void editStudentPoints(Long id, Double points) {
        StudentActivityPoints studentActivityPoints = studentActivityPointsRepositoryJPA.findById(id).orElse(null);
        System.out.println(studentActivityPoints.getCode());

        Activity activity = activityRepositoryJPA.findById(studentActivityPoints.getCode()).orElse(null);

        Long courseId = activity.getCourse().getId();
        Student student = studentRepositoryJPA.findById(studentActivityPoints.getIndex()).orElse(null);

        Grades grade = student.getGrades().stream()
                .filter(x -> x.getCourseId().equals(courseId))
                .findFirst().orElse(null);

        Double oldPoints = studentActivityPoints.getPoints() * (activity.getPercentage() / 100);

        Double total = grade.getTotalPoints();
        total -= oldPoints;

        Double newPoints = points * (activity.getPercentage() / 100);
        studentActivityPoints.setPoints(points);
        total += newPoints;

        Integer g = 5;

        if(total < 50) g = 5;
        else if(total >= 50 && total < 60) g = 6;
        else if(total >= 60 && total < 70) g = 7;
        else if(total >= 70 && total < 80) g = 8;
        else if(total >= 80 && total < 90) g = 9;
        else if(total >= 90) g = 10;

        grade.setTotalPoints(total);
        grade.setGrade(g);

        studentActivityPointsRepositoryJPA.save(studentActivityPoints);
        gradesRepositoryJPA.save(grade);
    }

    @Override
    public StudentActivityPoints findById(Long id) {
        StudentActivityPoints student = studentActivityPointsRepositoryJPA.findById(id).orElse(null);
        return student;
    }
}
