package lk.crystal.asset.category.controller;



import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.crystal.asset.category.entity.Category;
import lk.crystal.asset.category.service.CategoryService;
import lk.crystal.asset.item.entity.Enum.MainCategory;
import lk.crystal.util.interfaces.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController implements AbstractController< Category, Integer> {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private String commonThings(Model model, Category category, Boolean addState) {
        model.addAttribute("mainCategories", MainCategory.values());
        model.addAttribute("category", category);
        model.addAttribute("addStatus", addState);
        return "category/addCategory";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("categorys", categoryService.findAll());
        return "category/category";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new Category(), true);
    }

    @PostMapping(value = {"/add", "/update"})
    public String persist(Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThings(model, category, true);
        }
category.setName(category.getName().toUpperCase());
        categoryService.persist(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, categoryService.findById(id), false);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        categoryService.delete(id);
        return "redirect:/category";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("categoryDetail", categoryService.findById(id));
        return "category/category-detail";
    }

    @GetMapping(value = "/getCategory/{mainCategory}")
    @ResponseBody
    public MappingJacksonValue getCategoryByMainCategory(@PathVariable String mainCategory) {
        Category category = new Category();
        if (mainCategory != null) {
            category.setMainCategory(MainCategory.valueOf(mainCategory));
        } else {
            category.setMainCategory(MainCategory.PROCESSED_MEAT);
        }

        //MappingJacksonValue
        List<Category> categories = categoryService.search(category);
        //Create new mapping jackson value and set it to which was need to filter
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(categories);

        //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
        // what parameter's necessary to provide
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name");
        //filters :-  set front end required value to before filter

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("Category", simpleBeanPropertyFilter);
        //Employee :- need to annotate relevant class with JsonFilter  {@JsonFilter("Employee") }
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}