package lk.crystal.asset.item_color.controller;

import lk.crystal.asset.item_color.entity.ItemColor;
import lk.crystal.asset.item_color.service.ItemColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/itemColor")
public class ItemColorController {
    private final ItemColorService itemColorService;

    @Autowired
    public ItemColorController(ItemColorService itemColorService) {
        this.itemColorService = itemColorService;
    }
//read
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("itemColors", itemColorService.findAll());
        return "itemColor/itemColor";
    }
//add from
    @GetMapping("/add")
    public String form(Model model) {
        model.addAttribute("addStatus", true);
        model.addAttribute("itemColor", new ItemColor());
        return "itemColor/addItemColor";
    }
//
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("addStatus", false);
        model.addAttribute("itemColor", itemColorService.findById(id));
        return "itemColor/addItemColor";
    }
//create and update
    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute ItemColor itemColor, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addStatus", true);
            model.addAttribute("itemColor", bindingResult);
            return "itemColor/addItemColor";
        }
        itemColorService.persist(itemColor);
        return "redirect:/itemColor";
    }
//delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        itemColorService.delete(id);
        return "redirect:/itemColor";
    }
}
