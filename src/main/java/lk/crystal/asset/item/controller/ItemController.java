package lk.crystal.asset.item.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/item" )
public class ItemController implements AbstractController< Item, Integer > {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    private String commonThings(Model model, Item item, Boolean addState) {
        model.addAttribute("itemStatuses", ItemStatus.values());
        model.addAttribute("item", item);
        model.addAttribute("addStatus", addState);
        model.addAttribute("mainCategories", MainCategory.values());
        model.addAttribute("urlMainCategory", MvcUriComponentsBuilder
                .fromMethodName(CategoryController.class, "getCategoryByMainCategory", "")
                .build()
                .toString());
        return "item/addItem";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "item/item";
    }

    @GetMapping( "/add" )
    public String addForm(Model model) {
        return commonThings(model, new Item(), true);
    }

    @PostMapping( value = {"/save", "/update"} )
    public String persist(@Valid @ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            return commonThings(model, item, true);
        }

        if ( item.getId() == null ) {
            //item code => MainCategory first two letters + category first two letters + price
            item.setCode(item.getCategory().getMainCategory() + item.getCategory().getName().substring(0, 2) + item.getSellPrice());
        } else if ( !itemService.findById(item.getId()).getSellPrice().equals(item.getSellPrice()) ) {
            item.setCode(item.getCategory().getMainCategory() + item.getCategory().getName().substring(0, 2) + item.getSellPrice());
        }

        itemService.persist(item);
        return "redirect:/item";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, itemService.findById(id), false);
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        itemService.delete(id);
        return "redirect:/item";
    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("itemDetail", itemService.findById(id));
        return "item/item-detail";
    }

}