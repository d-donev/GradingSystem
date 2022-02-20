package mk.ukim.finki.gradingsystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Professor {

    @Id
    private String email;

    @OneToOne
    private User user;

    public Professor() {
    }

    public Professor(String email, User user) {
        this.email = email;
        this.user = user;
    }
}
