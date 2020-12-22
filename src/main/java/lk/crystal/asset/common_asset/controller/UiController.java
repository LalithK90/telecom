package lk.crystal.asset.common_asset.controller;

import lk.crystal.asset.user_management.user.service.UserService;
import lk.crystal.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    private final UserService userService;
    private final DateTimeAgeService dateTimeAgeService;

    @Autowired
    public UiController(UserService userService, DateTimeAgeService dateTimeAgeService) {
        this.userService = userService;
        this.dateTimeAgeService = dateTimeAgeService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"/home", "/mainWindow"})
    public String getHome(Model model) {
        //do some logic here if you want something to be done whenever
        /*User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<Petition> petitionSet = new HashSet<>();
        minutePetitionService
                .findByEmployeeAndCreatedAtBetween(authUser.getEmployee(),
                        dateTimeAgeService
                                .dateTimeToLocalDateStartInDay(LocalDate.now()),
                        dateTimeAgeService
                                .dateTimeToLocalDateEndInDay(LocalDate.now())).forEach(
                minutePetition -> {
                    petitionSet.add(petitionService.findById(minutePetition.getPetition().getId()));
                });
        model.addAttribute("petitions", petitionSet.toArray());*/
        return "mainWindow";
    }

    @GetMapping(value = {"/login"})
    public String getLogin() {
        return "login/login";
    }

    @GetMapping(value = {"/login/error10"})
    public String getLogin10(Model model) {
        model.addAttribute("err", "You already entered wrong credential more than 10 times. \n Please meet the system" +
                " admin");
        return "login/login";
    }

    @GetMapping(value = {"/login/noUser"})
    public String getLoginNoUser(Model model) {
        model.addAttribute("err", "There is no user according to the user name. \n Please try again !!");
        return "login/login";
    }




}
