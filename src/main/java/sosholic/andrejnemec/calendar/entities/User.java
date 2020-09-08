package sosholic.andrejnemec.calendar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;
import javax.persistence.*;


import static javax.persistence.GenerationType.AUTO;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false, nullable = false)
    private long id;

    @Column(updatable = false, unique = true, length = 19)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String roles = "";

    public List<String> getRoleList() {
        if (this.roles.length() > 0)
            return Arrays.asList(this.roles.split(","));
        return Collections.emptyList();
    }

    public boolean hasRoles(String... roles) {
        return this.getRoleList().containsAll(Arrays.asList(roles));
    }

    public boolean hasAnyRoles(String... roles) {
        for (String r : roles)
            if (this.getRoleList().contains(r)) return true;
        return false;
    }

}
