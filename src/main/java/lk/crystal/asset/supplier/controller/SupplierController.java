package lk.crystal.asset.supplier.controller;

import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier.service.SupplierService;
import lk.crystal.util.interfaces.AbstractController;
import lk.crystal.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/supplier")
public  class SupplierController implements AbstractController<Supplier, Integer> {
    private final SupplierService supplierService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

    @Autowired
    public SupplierController(SupplierService supplierService, MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
        this.supplierService = supplierService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    }

    private String commonThings(Model model, Supplier supplier, Boolean addState) {
        model.addAttribute("supplier", supplier);
        model.addAttribute("addStatus", addState);
        return "supplier/addSupplier";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("suppliers", supplierService.findAll().stream()
                .filter(x-> LiveDead.ACTIVE.equals(x.getLiveDead()))
                .collect(Collectors.toList()));
        return "supplier/supplier";
    }

    @Override
    public String findById(Integer id, Model model) {
        return null;
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new Supplier(), true);
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute Supplier supplier, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThings(model, supplier, true);
        }
        if (supplier.getContactOne() != null) {
            supplier.setContactOne(makeAutoGenerateNumberService.phoneNumberLengthValidator(supplier.getContactOne()));
        }
        if (supplier.getContactTwo() != null) {
            supplier.setContactTwo(makeAutoGenerateNumberService.phoneNumberLengthValidator(supplier.getContactTwo()));
        }
        //if supplier has id that supplier is not a new supplier
        if (supplier.getId() == null) {
            //if there is not supplier in db
            Supplier DBSupplier = supplierService.lastSupplier();

            if (DBSupplier == null) {
                //need to generate new one
                supplier.setCode("CTSS"+makeAutoGenerateNumberService.numberAutoGen(null).toString());
            } else {
                System.out.println("last supplier not null");
                //if there is supplier in db need to get that supplier's code and increase its value
                String previousCode = DBSupplier.getCode().substring(4);
                supplier.setCode("CTSS"+makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
            }
            //send welcome message and email
            if (supplier.getEmail() != null) {
                //  emailService.sendEmail(supplier.getEmail(), "Welcome Message", "Welcome to Kmart Super...");
            }
        }
        redirectAttributes.addFlashAttribute("supplierDetail",
                supplierService.persist(supplier));
        return "redirect:/supplier";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, supplierService.findById(id), false);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        supplierService.delete(id);
        return "redirect:/supplier";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("supplierDetail", supplierService.findById(id));
        return "supplier/supplier-detail";
    }
}
