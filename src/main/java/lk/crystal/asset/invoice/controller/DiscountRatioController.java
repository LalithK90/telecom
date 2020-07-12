package lk.crystal.asset.invoice.controller;

import lk.crystal.asset.invoice.entity.DiscountRatio;
import lk.crystal.asset.invoice.service.DiscountRatioService;
import lk.crystal.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import javax.validation.Valid;

@Controller
public class DiscountRatioController implements AbstractController<DiscountRatio ,Integer> {

    private final DiscountRatioService discountRatioService;

    public DiscountRatioController(DiscountRatioService discountRatioService) {
        this.discountRatioService = discountRatioService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("discountRatios" , discountRatioService.findAll());
        return "discountRatio/discountRatio";
    }

    public String findById(Integer id, Model model) {
        return null;
    }

    public String edit(Integer id, Model model) {
        return null;
    }

    public String persist(@Valid DiscountRatio discountRatio, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    public String delete(Integer id, Model model) {
        return null;
    }

    public String Form(Model model) {
        return null;
    }
}
