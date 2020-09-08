package sosholic.andrejnemec.calendar.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sosholic.andrejnemec.calendar.entities.Plan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanMapper {

    private long id;
    private String title;
    private String description;
    private String color;
    private LocalDateTime start;
    private LocalDateTime end;
    private long userId;

    public static PlanMapper map(Plan plan) {
        return new PlanMapper(plan.getId(),
                plan.getTitle(),
                plan.getDescription(),
                plan.getColor(),
                plan.getStart(),
                plan.getEnd(),
                plan.getUser().getId());
    }

    public static List<PlanMapper> map(List<Plan> plans) {
        return plans.stream().map(PlanMapper::map).collect(Collectors.toList());
    }

}
