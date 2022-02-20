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
    @GeneratedValue
    private Long id;

    private String name;

    private String username;

    private Role role;

    @ManyToMany
    private List<Course> courseList;

    public User() {
    }

    public User(String name, String username, Role role, List<Course> courseList) {
        this.name = name;
        this.username = username;
        this.role = role;
        this.courseList = courseList;
    }
}
