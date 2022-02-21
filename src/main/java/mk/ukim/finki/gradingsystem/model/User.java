package mk.ukim.finki.gradingsystem.model;

import jdk.jfr.Enabled;
import lombok.Data;
import mk.ukim.finki.gradingsystem.enumerations.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "UsersTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private Role role;

    @ManyToMany
    private List<Course> courseList;

    public User() {
    }

    public User(String name, String surname, Role role, List<Course> courseList) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.courseList = courseList;
    }
}
