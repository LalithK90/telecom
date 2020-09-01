package lk.crystal.asset.supplierItem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/supplierItem" )
public class SupplierItemController {
    private final ItemService itemService;
    private final SupplierService supplierService;
    private final CommonService commonService;
    private final SupplierItemService supplierItemService;
    private final LedgerDao ledgerDao;


    @Autowired
    public SupplierItemController(ItemService itemService, SupplierService supplierService,
                                  CommonService commonService, SupplierItemService supplierItemService,
                                  LedgerDao ledgerDao) {
        this.itemService = itemService;
        this.supplierService = supplierService;
        this.commonService = commonService;
        this.supplierItemService = supplierItemService;
        this.ledgerDao = ledgerDao;
    }

    @GetMapping
    public String addForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("searchAreaShow", true);
        List< Object > objects = new ArrayList<>();
        model.addAttribute("currentlyBuyingItems", objects);
        return "supplier/addSupplierItem";
    }

    @PostMapping( "/find" )
    public String search(@Valid @ModelAttribute Supplier supplier, Model model) {
        return commonService.supplierItem(supplier, model, "purchaseOrder/addPurchaseOrder");

    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        return commonService.supplierItem(model, id);
    }

    @GetMapping( "/supplier/{id}" )
    public String addPriceToSupplierItem(@PathVariable int id, Model model) {
        Supplier supplier = supplierService.findById(id);
        List< SupplierItem > supplierItems = supplierItemService.findBySupplier(supplier);
        model.addAttribute("itemSupplierStatus", ItemSupplierStatus.values());
        model.addAttribute("supplierDetail", supplier);
        model.addAttribute("supplierDetailShow", false);
        model.addAttribute("supplierItemEdit", false);
        model.addAttribute("currentlyBuyingItems", supplierItems);

        List< Item > items = itemService.findAll();

        if ( !supplierItems.isEmpty() ) {
            for ( Item item : itemService.findAll() ) {
                for ( SupplierItem supplierItem : supplierItems ) {
                    if ( item.equals(supplierItem.getItem()) ) {
                        items.remove(item);
                    }
                }
            }
        }

        model.addAttribute("items", items);
        return "supplier/addSupplierItem";
    }

    @GetMapping( "/edit/{id}" )
    public String addEditToSupplierItem(@PathVariable int id, Model model) {
        Supplier supplier = supplierService.findById(id);
        List< SupplierItem > supplierItems = supplierItemService.findBySupplier(supplier);
        model.addAttribute("itemSupplierStatus", ItemSupplierStatus.values());
        model.addAttribute("supplierDetail", supplier);
        model.addAttribute("supplierDetailShow", false);
        model.addAttribute("currentlyBuyingItems", supplierItems);
        model.addAttribute("supplierItemEdit", true);
        return "supplier/addSupplierItem";
    }

    @PostMapping
    public String supplierItemPersist(@Valid @ModelAttribute( "supplier" ) Supplier supplier,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if ( bindingResult.hasErrors() ) {
            boolean value = false;
            for ( SupplierItem supplierItem : supplier.getSupplierItems() ) {
                if ( supplierItem.getId() != null ) {
                    value = true;
                    break;
                }
            }
            if ( value ) {
                return "redirect:/supplierItem/edit/" + supplier.getId();
            } else {
                return "redirect:/supplierItem/" + supplier.getId();
            }
        }
        //items from front item relevant to supplier
        List< SupplierItem > supplierItems = supplier.getSupplierItems();
        for ( SupplierItem supplierItem : supplierItems ) {
            if ( supplierItem.getId() == null && supplierItem.getPrice() != null ) {
                supplierItem.setSupplier(supplier);
                supplierItemService.persist(supplierItem);
            } else if ( supplierItem.getPrice() != null ) {
                supplierItemService.persist(supplierItem);
            }
        }
        return "redirect:/supplier";
    }


    @GetMapping( value = "/supplierItem", params = {"supplierId", "itemId"} )
    @ResponseBody
    public PurchaseOrderItemLedger purchaseOrderSupplierItem(@RequestParam( "supplierId" ) Integer supplierId,
                                                             @RequestParam( "itemId" ) Integer itemId) {
        SupplierItem supplierItem = supplierItemService.findBySupplierAndItemItemSupplierStatus(
                supplierService.findByIdAndItemSupplierStatus(supplierId, ItemSupplierStatus.CURRENTLY_BUYING),
                itemService.findById(itemId), ItemSupplierStatus.CURRENTLY_BUYING);
        PurchaseOrderItemLedger purchaseOrderItemLedger = new PurchaseOrderItemLedger();
        /* 1. item ID   2. Item name 3. Rop 4. Price 5. Available Quantity. */
        purchaseOrderItemLedger.setItemId(supplierItem.getItem().getId());
        purchaseOrderItemLedger.setItemName(supplierItem.getItem().getName());
        purchaseOrderItemLedger.setRop(supplierItem.getItem().getRop());
        purchaseOrderItemLedger.setPrice(supplierItem.getPrice());

//normal comparator
//    Comparator<Ledger> ledgerComparator = new Comparator<Ledger>() {
//            @Override
//            public int compare(Ledger ledger, Ledger ledger2) {
//                return ledger.getId().compareTo(ledger2.getId());
//            }
//        };
//Lambda comparator
        // Comparator<Ledger> ledgerComparator = (ledger, ledger2) -> ledger.getId().compareTo(ledger2.getId());

        //comparing
        Comparator< Ledger > ledgerComparator = Comparator.comparing(AuditEntity::getId);
        List< Ledger > ledgers =
                ledgerDao.findByItem(supplierItem.getItem())
                        .stream()
                        .filter(x -> x.getItem().getItemStatus().equals(supplierItem.getItem().getItemStatus()))
                        .sorted(ledgerComparator)
                        .collect(Collectors.toList());
        if ( ledgers.size() != 0 ){
            purchaseOrderItemLedger.setAvailableQuantity(ledgers.get(0).getQuantity());}
        else {
            purchaseOrderItemLedger.setAvailableQuantity(String.valueOf(0));
        }


        return purchaseOrderItemLedger;
    }


}
