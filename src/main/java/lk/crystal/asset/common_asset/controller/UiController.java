package lk.crystal.asset.common_asset.controller;

import lk.crystal.asset.ledger.service.LedgerService;
import lk.crystal.asset.user_management.user.service.UserService;
import lk.crystal.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
public class UiController {

    private final UserService userService;
    private final DateTimeAgeService dateTimeAgeService;
    private final LedgerService ledgerService;

    @Autowired
    public UiController(UserService userService, DateTimeAgeService dateTimeAgeService, LedgerService ledgerService) {
        this.userService = userService;
        this.dateTimeAgeService = dateTimeAgeService;
        this.ledgerService = ledgerService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"/home", "/mainWindow"})
    public String getHome(Model model) {

        model.addAttribute("ropList", ledgerService.findAll()
                .stream()
                .filter(x -> Integer.parseInt(x.getQuantity()) < Integer.parseInt(x.getItem().getRop()))
                .collect(Collectors.toList()));


        //do some logic here if want something to be done
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


}
