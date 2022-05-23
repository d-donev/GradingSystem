package mk.ukim.finki.gradingsystem.service.impl;

import mk.ukim.finki.gradingsystem.model.Activity;
import mk.ukim.finki.gradingsystem.model.Grades;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.repositoryJPA.ActivityRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.GradesRepositoryJPA;
import mk.ukim.finki.gradingsystem.repositoryJPA.StudentActivityPointsRepositoryJPA;
import mk.ukim.finki.gradingsystem.service.GradesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradesServiceImpl implements GradesService {

    private final GradesRepositoryJPA gradesRepositoryJPA;
    private final ActivityRepositoryJPA activityRepositoryJPA;
    private final StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA;

    public GradesServiceImpl(GradesRepositoryJPA gradesRepositoryJPA, ActivityRepositoryJPA activityRepositoryJPA, StudentActivityPointsRepositoryJPA studentActivityPointsRepositoryJPA) {
        this.gradesRepositoryJPA = gradesRepositoryJPA;
        this.activityRepositoryJPA = activityRepositoryJPA;
        this.studentActivityPointsRepositoryJPA = studentActivityPointsRepositoryJPA;
    }

    @Override
    public void updatePoints(List<Student> students, Long courseId) {
        List<Activity> activities = activityRepositoryJPA.findAll().stream()
                .filter(x -> x.getCourse().getId().equals(courseId)).collect(Collectors.toList());
        List<StudentActivityPoints> allStudentPoints = studentActivityPointsRepositoryJPA.findAll();
        List<StudentActivityPoints> studentPoints = new ArrayList<>();
        Double points = 0.0;
        Double totalPoints = 0.0;
        Integer grade = 5;

        for(int i=0; i<allStudentPoints.size(); i++) {
            for(int j=0; j<activities.size(); j++) {
                if(allStudentPoints.get(i).getCode().equals(activities.get(j).getCode())) {
                    studentPoints.add(allStudentPoints.get(i));
                }
            }
        }


        for(int i=0; i<students.size(); i++) {
            totalPoints = 0.0;
            for(int j=0; j<studentPoints.size(); j++) {
                if(students.get(i).getIndex().equals(studentPoints.get(j).getIndex())) {

                    for(int k=0; k<activities.size(); k++) {
                        if(studentPoints.get(j).getCode().equals(activities.get(k).getCode())) {
                            points = studentPoints.get(j).getPoints() * (activities.get(k).getPercentage()/100);
                            totalPoints += points;
                        }
                        else {
                            points = 0.0;
                            totalPoints += points;
                        }
                    }
                }
            }
            totalPoints = (double) Math.round(totalPoints * 100) / 100;
            if(totalPoints < 50) grade = 5;
            else if(totalPoints >= 50 && totalPoints < 60) grade = 6;
            else if(totalPoints >= 60 && totalPoints < 70) grade = 7;
            else if(totalPoints >= 70 && totalPoints < 80) grade = 8;
            else if(totalPoints >= 80 && totalPoints < 90) grade = 9;
            else if(totalPoints >= 90) grade = 10;

            Integer index = students.get(i).getIndex();
            Grades g = gradesRepositoryJPA.findAll().stream()
                    .filter(x -> x.getIndex().equals(index) && x.getCourseId().equals(courseId)).findFirst().orElse(null);
            g.setTotalPoints(totalPoints);
            g.setGrade(grade);
            gradesRepositoryJPA.save(g);
        }
    }
}
