package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Student {

    @Id
    private Integer index;

    @OneToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    List<Grades> grades;

    public Student() {

    }

    public Student(Integer index, User user) {
        this.index = index;
        this.user = user;
    }

    public Student(Integer index, User user, List<Grades> grades) {
        this.index = index;
        this.user = user;
        this.grades = grades;
    }
}
