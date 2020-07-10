package lk.crystal.asset.invoice.controller;

import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.asset.invoice.service.InvoiceService;
import lk.crystal.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/invoice")
public  class InvoiceController implements AbstractController<Invoice,Integer> {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    public String findAll(Model model) {
        return null;
    }

    public String findById(Integer id, Model model) {
        return null;
    }

    public String edit(Integer id, Model model) {
        return null;
    }

    public String persist(@Valid Invoice invoice, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    public String delete(Integer id, Model model) {
        return null;
    }

    public String Form(Model model) {
        return null;
    }
}
