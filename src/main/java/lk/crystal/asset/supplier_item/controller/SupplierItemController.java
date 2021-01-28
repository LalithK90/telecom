package lk.crystal.asset.supplier_item.controller;


import lk.crystal.asset.common_asset.service.CommonService;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.item.service.ItemService;
import lk.crystal.asset.ledger.dao.LedgerDao;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.purchase_order.common_model.PurchaseOrderItemLedger;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier.service.SupplierService;
import lk.crystal.asset.supplier_item.entity.SupplierItem;
import lk.crystal.asset.supplier_item.entity.enums.ItemSupplierStatus;
import lk.crystal.asset.supplier_item.service.SupplierItemService;
import lk.crystal.util.audit.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
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
      SupplierItem supplierItemOne = new SupplierItem();
      if ( supplierItem.getId() != null ) {
        supplierItemOne.setId(supplierItem.getId());
      }
      if ( !supplierItem.getPrice().equals(BigDecimal.ZERO) ) {
        supplierItemOne.setSupplier(supplierService.findById(supplierItem.getSupplier().getId()));
        supplierItemOne.setItem(itemService.findById(supplierItem.getItem().getId()));
        supplierItemOne.setPrice(supplierItem.getPrice());
      }
      supplierItemService.persist(supplierItemOne);
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

    //comparing to learn comparator
    Comparator< Ledger > ledgerComparator = Comparator.comparing(AuditEntity::getId);
    List< Ledger > ledgers =
        ledgerDao.findByItem(supplierItem.getItem())
            .stream()
            .sorted(ledgerComparator)
            .collect(Collectors.toList());

    if ( ledgers.size() != 0 ) {
      purchaseOrderItemLedger.setAvailableQuantity(ledgers.get(0).getQuantity());
    } else {
      purchaseOrderItemLedger.setAvailableQuantity(String.valueOf(0));
    }


    return purchaseOrderItemLedger;
  }


}
