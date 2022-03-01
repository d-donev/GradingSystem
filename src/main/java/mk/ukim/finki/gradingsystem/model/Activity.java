package mk.ukim.finki.gradingsystem.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@SQLDelete(sql = "UPDATE activity SET deleted = true WHERE code=?")
@Where(clause = "deleted = false")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    private String name;

    @ManyToOne
    private Course course;

    private Double percentage;

    private Double minimum;

    private boolean deleted = false;

    public Activity() {
    }

    public Activity(String name, Course course, Double percentage, Double minimum) {
        this.name = name;
        this.course = course;
        this.percentage = percentage;
        this.minimum = minimum;
    }

}
