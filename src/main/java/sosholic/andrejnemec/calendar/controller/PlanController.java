package sosholic.andrejnemec.calendar.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sosholic.andrejnemec.calendar.entities.Plan;
import sosholic.andrejnemec.calendar.mappers.PlanMapper;
import sosholic.andrejnemec.calendar.mappers.StatusMapper;
import sosholic.andrejnemec.calendar.mappers.payload.PlanPayloadMapper;
import sosholic.andrejnemec.calendar.security.user.UserPrincipal;
import sosholic.andrejnemec.calendar.service.PlanService;
import sosholic.andrejnemec.calendar.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/plan")
@AllArgsConstructor
public class PlanController {

    private PlanService planService;
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getPlanById(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable("id") long id) {
        try {
            Plan plan = this.planService.getPlanById(id);
            if (plan.getUser().getId() != currentUser.getId() && plan.getUser().hasAnyRoles("ADMIN"))
                return new ResponseEntity(PlanMapper.map(plan), HttpStatus.OK);
            else if (plan.getUser().getId() == currentUser.getId())
                return new ResponseEntity(PlanMapper.map(plan), HttpStatus.OK);
            else
                return new ResponseEntity(new StatusMapper(403, "You dont have permission to see this plan!"), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getPlanList(@AuthenticationPrincipal UserPrincipal currentUser) {
        try {
            List<Plan> plans = planService.getAllPlansByUser(currentUser.getId());
            return new ResponseEntity(PlanMapper.map(plans), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/by-month-and-year/{month}/{year}")
    public ResponseEntity getPlanListByMonthAndYear(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable("month") int month,
                                                    @PathVariable("year") int year) {
        if (month > 12 || month < 1)
            return new ResponseEntity(new StatusMapper(400, "The month or year is invalid!"), HttpStatus.BAD_REQUEST);

        try {
            List<Plan> plans = planService.getPlansByUserAndMonthAndYear(currentUser.getId(), month, year);
            return new ResponseEntity(PlanMapper.map(plans), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/all/by-user/{userId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity getPlanListByUser(@PathVariable("userId") Long userId) {
        try {
            List<Plan> plans = planService.getAllPlansByUser(userId);
            return new ResponseEntity(PlanMapper.map(plans), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/all/by-user-month-and-year/{userId}/{month}/{year}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity getPlanListByUserAndMonthAndYear(@PathVariable("userId") Long userId,
                                                           @PathVariable("month") int month,
                                                           @PathVariable("year") int year) {
        if (month > 12 || month < 1)
            return new ResponseEntity(new StatusMapper(400, "The month or year is invalid!"), HttpStatus.BAD_REQUEST);
        try {
            List<Plan> plans = planService.getPlansByUserAndMonthAndYear(userId, month, year);
            return new ResponseEntity(PlanMapper.map(plans), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity savePlan(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody PlanPayloadMapper planPayloadMapper) {
        try {
            Plan plan = this.planService.save(planPayloadMapper, currentUser.getId());
            return new ResponseEntity(PlanMapper.map(plan), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(500, "Error with creating plan!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePlan(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable("id") long id) {
        try {

            boolean hasPermission = false;

            Plan plan = this.planService.getPlanById(id);
            if (plan.getUser().getId() != currentUser.getId() && plan.getUser().hasAnyRoles("ADMIN"))
                hasPermission = true;
            else if (plan.getUser().getId() == currentUser.getId())
                hasPermission = true;
            else
                hasPermission = false;

            if (hasPermission)
                return this.planService.deletePlan(id) ? new ResponseEntity(new StatusMapper(200, "The plan was deleted"), HttpStatus.OK) : new ResponseEntity(new StatusMapper(500, "Error with deleting plan!"), HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity(new StatusMapper(403, "You dont have permission to delete this plan!"), HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(500, "Error with deleting plan!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePlan(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody PlanMapper planMapper) {
        try {

            boolean hasPermission = false;

            Plan plan = this.planService.getPlanById(planMapper.getId());
            if (plan.getUser().getId() != currentUser.getId() && plan.getUser().hasAnyRoles("ADMIN"))
                hasPermission = true;
            else if (plan.getUser().getId() == currentUser.getId())
                hasPermission = true;
            else
                hasPermission = false;

            if (hasPermission) {
                Plan planToUpdate = this.planService.updatePlan(planMapper);
                return new ResponseEntity(PlanMapper.map(planToUpdate), HttpStatus.OK);
            } else
                return new ResponseEntity(new StatusMapper(403, "You dont have permission to update this plan!"), HttpStatus.FORBIDDEN);


        } catch (Exception e) {
            return new ResponseEntity(new StatusMapper(500, "Error with creating plan!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
