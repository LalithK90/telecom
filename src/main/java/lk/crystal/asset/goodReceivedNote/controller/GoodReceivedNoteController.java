package lk.crystal.asset.goodReceivedNote.controller;

import lk.crystal.asset.goodReceivedNote.service.GoodReceivedNoteService;
import lk.crystal.asset.purchaseOrder.entity.Enum.PurchaseOrderStatus;
import lk.crystal.asset.purchaseOrder.service.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goodReceivedNote")
public class GoodReceivedNoteController {
    private final GoodReceivedNoteService goodReceivedNoteService;
    private final PurchaseOrderService purchaseOrderService;

    public GoodReceivedNoteController(GoodReceivedNoteService goodReceivedNoteService, PurchaseOrderService purchaseOrderService) {
        this.goodReceivedNoteService = goodReceivedNoteService;
        this.purchaseOrderService = purchaseOrderService;
    }


    @GetMapping
    public String notCompleteAll(Model model) {
        model.addAttribute("notCompleteAll", purchaseOrderService.findByGoodReceivedNoteState(PurchaseOrderStatus.NOT_COMPLETED));
        return "goodReceivedNote/addGoodReceivedNote";
    }


}
