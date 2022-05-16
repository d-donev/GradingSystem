package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class StudentActivityPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer index;

    private Long code;

    private Double points;

    public StudentActivityPoints() {
    }

    public StudentActivityPoints(Integer index, Long code, Double points) {
        this.index = index;
        this.code = code;
        this.points = points;
    }
}
