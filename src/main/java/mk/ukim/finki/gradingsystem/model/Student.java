package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@Entity
public class Student {

    @Id
    private Integer index;

    @OneToOne
    private User user;

    @OneToMany
    List<Grades> grades;

    public Student() {

    }

    public Student(Integer index, User user) {
        this.index = index;
        this.user = user;
    }
}
