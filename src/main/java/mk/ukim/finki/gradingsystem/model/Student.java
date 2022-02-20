package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Student {

    @Id
    private Integer index;

    @OneToOne
    private User user;

    public Student() {

    }

    public Student(Integer index, User user) {
        this.index = index;
        this.user = user;
    }
}
