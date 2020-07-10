package lk.crystal.asset.ledger.controller;

import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.ledger.service.LedgerService;
import lk.crystal.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/ledger")
public class LedgerController implements AbstractController<Ledger,Integer> {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
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

    public String persist(@Valid Ledger ledger, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    public String delete(Integer id, Model model) {
        return null;
    }

    public String Form(Model model) {
        return null;
    }
}
