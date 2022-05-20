package mk.ukim.finki.gradingsystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String year;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Activity> activityList;

    @ManyToMany
    private List<Student> studentList;


    public Course() {
    }

    public Course(String name, String year, List<Activity> activityList) {
        this.name = name;
        this.year = year;
        this.activityList = activityList;
    }
}
