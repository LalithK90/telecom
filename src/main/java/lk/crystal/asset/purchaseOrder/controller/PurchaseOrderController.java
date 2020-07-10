package lk.crystal.asset.purchaseOrder.controller;

import lk.crystal.asset.commonAsset.service.CommonService;
import lk.crystal.asset.purchaseOrder.entity.Enum.PurchaseOrderStatus;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrder;
import lk.crystal.asset.purchaseOrder.service.PurchaseOrderService;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier.service.SupplierService;
import lk.crystal.util.service.EmailService;
import lk.crystal.util.service.MakeAutoGenerateNumberService;
import lk.crystal.util.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final CommonService commonService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
    private final EmailService emailService;
    private final OperatorService operatorService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, SupplierService supplierService, CommonService commonService, MakeAutoGenerateNumberService makeAutoGenerateNumberService, EmailService emailService, OperatorService operatorService) {
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.commonService = commonService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
        this.emailService = emailService;
        this.operatorService = operatorService;
    }

    @GetMapping
    public String Form(Model model) {
        model.addAttribute("purchaseOrder", new PurchaseOrder());
        model.addAttribute("searchAreaShow", true);
        return "purchaseOrder/addPurchaseOrder";
    }

    @PostMapping("/find")
    public String search(@Valid @ModelAttribute Supplier supplier, Model model) {
        return commonService
                .supplierItemAndPurchaseOrderSearch(supplier, model, "purchaseOrder/addPurchaseOrder");
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        commonService.supplierItemAndPurchaseOrderView(model, id);
        return "purchaseOrder/addPurchaseOrder";
    }

    @PostMapping
    public String supplierItemPersist(@Valid @ModelAttribute PurchaseOrder purchaseOrder, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/supplierItem/" + purchaseOrder.getId();
        }
        purchaseOrder.getPurchaseOrderItems()
                .forEach(purchaseOrderItem -> {
                    purchaseOrderItem.setPurchaseOrder(purchaseOrder);
                    //todo need to create batch number
                });
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_COMPLETED);
        if (purchaseOrder.getId() != null) {
            if (purchaseOrderService.lastPurchaseOrder() == null) {
                //need to generate new one
                purchaseOrder.setCode("KMPO" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
            } else {
                System.out.println("last customer not null");
                //if there is customer in db need to get that customer's code and increase its value
                String previousCode = purchaseOrderService.lastPurchaseOrder().getCode().substring(3);
                purchaseOrder.setCode("KMPO" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
            }
        }
        PurchaseOrder purchaseOrderSaved = purchaseOrderService.persist(purchaseOrder);
/*        if (purchaseOrderSaved.getSupplier().getEmail() != null) {
            StringBuilder message = new StringBuilder("Item Name\t\t\t\t\tQuantity\t\t\tItem Price\t\t\tTotal(Rs)\n");
            for (int i = 0; i < purchaseOrder.getPurchaseOrderItems().size(); i++) {
                message
                        .append(purchaseOrder.getPurchaseOrderItems().get(i).getItem().getName())
                        .append("\t\t\t\t\t")
                        .append(purchaseOrderSaved.getPurchaseOrderItems().get(i).getQuantity())
                        .append("\t\t\t")
                        .append(purchaseOrderSaved.getPurchaseOrderItems().get(i).getPrice()).append("\t\t\t")
                        .append(operatorService.multiply(
                                purchaseOrderSaved.getPurchaseOrderItems().get(i).getPrice(),
                                new BigDecimal(Integer.parseInt(purchaseOrderSaved.getPurchaseOrderItems().get(i).getQuantity()))
                        ))
                        .append("\n");
            }
            emailService.sendEmail(purchaseOrderSaved.getSupplier().getEmail(), "Requesting Items According To PO Code " + purchaseOrder.getCode(), message.toString());
        }*/
        return "redirect:/purchaseOrder/all";
    }

    @GetMapping("/all")
    public String findAll(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.findAll());
        return "purchaseOrder/purchaseOrder";
    }

    @GetMapping("view/{id}")
    public String viewPurchaseOrderDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("purchaseOrder-details", purchaseOrderService.findById(id));
        return "purchaseOrder/purchaseOrder-detail";
    }

    @GetMapping("delete/{id}")
    public String deletePurchaseOrderDetail(@PathVariable Integer id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.findById(id);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_PROCEED);
        purchaseOrderService.persist(purchaseOrder);
        //model.addAttribute("purchaseOrder-details", purchaseOrderService.findById(id));
        return "redirect:/purchaseOrder/all";
    }

    //todo -> need to  manage item price displaying option and amount calculation

}
