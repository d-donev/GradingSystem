package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String year;

    @ManyToMany
    private List<Activity> activityList;

    public Course() {
    }

    public Course(String name, String year, List<Activity> activityList) {
        this.name = name;
        this.year = year;
        this.activityList = activityList;
    }
}
