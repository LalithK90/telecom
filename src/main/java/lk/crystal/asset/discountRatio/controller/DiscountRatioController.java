package lk.crystal.asset.discountRatio.controller;
import lk.crystal.asset.discountRatio.entity.DiscountRatio;
import lk.crystal.asset.discountRatio.entity.Enum.DiscountRatioStatus;
import lk.crystal.asset.discountRatio.service.DiscountRatioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/discountRatio" )
public class DiscountRatioController {
    private final DiscountRatioService discountRatioService;

    public DiscountRatioController(DiscountRatioService discountRatioService) {
        this.discountRatioService = discountRatioService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("discountRatios", discountRatioService.findAll());
        return "discountRatio/discountRatio";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("addStatus", false);
        model.addAttribute("discountRatioStatuses", DiscountRatioStatus.values());
        model.addAttribute("discountRatio", discountRatioService.findById(id));
        return "discountRatio/addDiscountRatio";
    }

    @PostMapping( value = {"/save", "/update"} )
    public String persist(@Valid @ModelAttribute DiscountRatio discountRatio, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            model.addAttribute("addStatus", false);
            model.addAttribute("discountRatioStatuses", DiscountRatioStatus.values());
            model.addAttribute("discountRatio", discountRatio);
            return "discountRatio/addDiscountRatio";
        }
        redirectAttributes.addFlashAttribute("discountRatio", discountRatioService.persist(discountRatio));
        return "redirect:/discountRatio";
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        discountRatioService.delete(id);
        return "redirect:/discountRatio";
    }

    @GetMapping( "/add" )
    public String form(Model model) {
        model.addAttribute("addStatus", true);
        model.addAttribute("discountRatioStatuses", DiscountRatioStatus.values());
        model.addAttribute("discountRatio", new DiscountRatio());
        return "discountRatio/addDiscountRatio";
    }
}
