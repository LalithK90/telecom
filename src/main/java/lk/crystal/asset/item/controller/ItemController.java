package lk.crystal.asset.item.controller;


import lk.crystal.asset.category.controller.CategoryRestController;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.item.entity.enums.ItemStatus;
import lk.crystal.asset.item.entity.enums.MainCategory;
import lk.crystal.asset.item.service.ItemService;
import lk.crystal.util.interfaces.AbstractController;
import lk.crystal.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping( "/item" )
public class ItemController implements AbstractController< Item, Integer > {
  private final ItemService itemService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  @Autowired
  public ItemController(ItemService itemService, MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.itemService = itemService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  private String commonThings(Model model, Item item, Boolean addState) {
    model.addAttribute("statuses", ItemStatus.values());
    model.addAttribute("item", item);
    model.addAttribute("addStatus", addState);
    model.addAttribute("mainCategories", MainCategory.values());
    model.addAttribute("urlMainCategory", MvcUriComponentsBuilder
        .fromMethodName(CategoryRestController.class, "getCategoryByMainCategory", "")
        .build()
        .toString());
    return "item/addItem";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("items", itemService.findAll().stream()
        .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
        .collect(Collectors.toList()));
    return "item/item";
  }

  @Override
  public String findById(Integer id, Model model) {
    return null;
  }

  @GetMapping( "/add" )
  public String addForm(Model model) {
    return commonThings(model, new Item(), true);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThings(model, item, true);
    }
    if ( item.getId() == null ) {
      //if there is not item in db
      if ( itemService.lastItem() == null ) {
        //need to generate new one
        item.setCode("CTSI" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
        item.setItemStatus(ItemStatus.JUSTENTERED);
      } else {
        //if there is item in db need to get that item's code and increase its value
        String previousCode = itemService.lastItem().getCode().substring(4);
        item.setCode("CTSI" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
      }
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
