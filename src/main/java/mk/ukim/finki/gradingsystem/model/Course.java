package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String year;

    @ManyToMany
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
