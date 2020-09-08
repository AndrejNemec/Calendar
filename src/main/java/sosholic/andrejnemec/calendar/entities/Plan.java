package sosholic.andrejnemec.calendar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false, nullable = false)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String color;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime start;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
