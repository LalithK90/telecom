package lk.crystal.asset.ledger.controller;

import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.ledger.service.LedgerService;
import lk.crystal.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/ledger" )
public class LedgerController {
    private final LedgerService ledgerService;
    private final DateTimeAgeService dateTimeAgeService;

    public LedgerController(LedgerService ledgerService, DateTimeAgeService dateTimeAgeService) {
        this.ledgerService = ledgerService;
        this.dateTimeAgeService = dateTimeAgeService;
    }

    //all ledgers
    @GetMapping
    public String findAllLed(Model model) {
        model.addAttribute("title", "All Items In Stock");
        model.addAttribute("ledgers", ledgerService.findAll());
        return "ledger/ledger";
    }

    //reorder point < item count
    @GetMapping( "/reorderPoint" )
    public String reorderPoint(Model model) {
        model.addAttribute("title", "Reorder Point Limit Exceeded");
        model.addAttribute("ledgers", ledgerService.findAll()
                .stream()
                .filter(x -> Integer.parseInt(x.getQuantity()) < Integer.parseInt(x.getItem().getRop()))
                .collect(Collectors.toList()));
        return "ledger/ledger";
    }

    //near expired date
    @PostMapping( "/expiredDate" )
    public String expiredDate(@RequestAttribute( "startDate" ) LocalDate startDate,
                              @RequestAttribute( "endDate" ) LocalDate endDate, Model model) {
        model.addAttribute("title", "All items on given date range start at " + startDate + " end at " + endDate);
        model.addAttribute("ledgers",
                           ledgerService.findByCreatedAtIsBetween(dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate), dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate)));

        return "ledger/ledger";
    }

    @GetMapping( "/{id}" )
    @ResponseBody
   /* public MappingJacksonValue findId(@PathVariable Integer id) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(ledgerService.findById(id));
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "quantity", "item", "sellPrice");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("Ledger", simpleBeanPropertyFilter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }*/
    public Ledger find(@PathVariable Integer id){
        return ledgerService.findById(id);
    }
}
