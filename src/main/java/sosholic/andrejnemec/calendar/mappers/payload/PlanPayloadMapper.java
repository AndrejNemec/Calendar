package sosholic.andrejnemec.calendar.mappers.payload;

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
public class PlanPayloadMapper {

    private String title;
    private String description;
    private String color;

    private LocalDateTime start;
    private LocalDateTime end;

    public static Plan map(PlanPayloadMapper planPayloadMapper) {
        return new Plan(0, planPayloadMapper.title, planPayloadMapper.description, planPayloadMapper.color, planPayloadMapper.start, planPayloadMapper.end, null);
    }

    public static List<Plan> map(List<PlanPayloadMapper> planPayloadMappers) {
        return planPayloadMappers.stream().map(PlanPayloadMapper::map).collect(Collectors.toList());
    }

}
