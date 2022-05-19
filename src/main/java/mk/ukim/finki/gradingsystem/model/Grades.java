package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Grades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer index;

    private Long courseId;

    private Double totalPoints;

    private Integer grade;

    public Grades() {

    }

    public Grades(Integer index, Long courseId, Double totalPoints, Integer grade) {
        this.index = index;
        this.courseId = courseId;
        this.totalPoints = totalPoints;
        this.grade = grade;
    }
}
