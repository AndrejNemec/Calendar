package sosholic.andrejnemec.calendar.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sosholic.andrejnemec.calendar.entities.Plan;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.mappers.PlanMapper;
import sosholic.andrejnemec.calendar.mappers.payload.PlanPayloadMapper;
import sosholic.andrejnemec.calendar.repository.PlanRepository;
import sosholic.andrejnemec.calendar.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlanService {

    private PlanRepository planRepository;
    private UserService userService;

    public Plan getPlanById(long id) throws Exception {
        Optional<Plan> plan = planRepository.findById(id);
        if (!plan.isPresent()) throw new Exception("This plan is not exists!");
        return plan.get();
    }

    @Transactional
    public List<Plan> getAllPlansByUser(long userId) throws Exception {

        User user = userService.findUserById(userId);

        if (user == null) throw new Exception("This user is not exists!");

        return planRepository.findAllByUser(user);
    }

    @Transactional
    public Plan save(Plan plan) throws Exception {

        User user = userService.findUserById(plan.getUser().getId());

        if (user == null) throw new Exception("This user is not exists!");

        return planRepository.save(plan);
    }

    @Transactional
    public Plan save(PlanPayloadMapper planPayloadMapper, long userId) throws Exception {
        Plan plan = PlanPayloadMapper.map(planPayloadMapper);

        User user = this.userService.findUserById(userId);

        if (user == null) throw new Exception("This user is not exists!");

        plan.setUser(user);

        return planRepository.save(plan);
    }

    @Transactional
    public Plan updatePlan(PlanMapper planMapper) throws Exception {
        Plan plan = this.getPlanById(planMapper.getId());

        plan.setTitle(planMapper.getTitle());
        plan.setDescription(planMapper.getDescription());
        plan.setColor(planMapper.getColor());
        plan.setStart(planMapper.getStart());
        plan.setEnd(planMapper.getEnd());

        return planRepository.save(plan);

    }

    @Transactional
    public boolean deletePlan(long id) {
        try {
            this.planRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public List<Plan> getPlansByUserAndMonthAndYear(long userId, int month, int year) throws Exception {
        User user = userService.findUserById(userId);
        if (user == null) throw new Exception("This user is not exists!");

        LocalDate localDate = LocalDate.of(year, month, 1);

        LocalDate localDate1 = localDate.minusDays(1);
        LocalDate localDate2 = localDate.plusMonths(1);

        return planRepository.findAllByUserAndStartBetweenOrEndBetween(user, localDate1.atStartOfDay(), localDate2.atStartOfDay(), localDate1.atStartOfDay(), localDate2.atStartOfDay());

    }

    @Transactional
    public List<Plan> getPlansByMonthAndYear(int month, int year) throws Exception {
        return this.getPlansByUserAndMonthAndYear(1, month, year);
    }


}
