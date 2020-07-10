package lk.crystal.asset.commonAsset.service;


import lk.crystal.asset.employee.controller.EmployeeRestController;
import lk.crystal.asset.item.service.ItemService;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier.service.SupplierService;
import lk.crystal.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
    private final SupplierService supplierService;
    private final ItemService itemService;

    @Autowired
    public CommonService(MakeAutoGenerateNumberService makeAutoGenerateNumberService, SupplierService supplierService, ItemService itemService) {
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
        this.supplierService = supplierService;
        this.itemService = itemService;
    }

    public String supplierItemAndPurchaseOrderSearch(Supplier supplier, Model model, String htmlFileLocation) {
        List<Supplier> suppliers;
        if (supplier.getContactOne() != null) {
            String contactNumber = makeAutoGenerateNumberService.phoneNumberLengthValidator(supplier.getContactOne());
//all match with supplier contact number one
            supplier.setContactOne(contactNumber);
            supplier.setContactTwo(null);
            suppliers = new ArrayList<>(supplierService.search(supplier));
//all match with contact number two
            supplier.setContactOne(null);
            supplier.setContactTwo(contactNumber);
            suppliers.addAll(supplierService.search(supplier));

        } else {
            suppliers = supplierService.search(supplier);
        }
        if (supplier.getContactOne() != null) {
            suppliers = suppliers
                    .stream()
                    .filter(supplier1 ->
                            supplier.getContactOne().equals(supplier1.getContactTwo()) ||
                                    supplier.getContactOne().equals(supplier1.getContactOne()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("searchAreaShow", false);

        if (suppliers.size() == 1) {
            model.addAttribute("supplierDetail", suppliers.get(0));
            model.addAttribute("supplierDetailShow", false);
            model.addAttribute("items", itemService.findAll());
            return htmlFileLocation;
        }
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierDetailShow", true);
        return htmlFileLocation;
    }

    public void supplierItemAndPurchaseOrderView(Model model, Integer id) {
        model.addAttribute("searchAreaShow", false);
        model.addAttribute("supplierDetail", supplierService.findById(id));
        model.addAttribute("supplierDetailShow", false);
    }

    public String commonMobileNumberLengthValidator(String mobileTwo) {
    return mobileTwo;}

    //common things to employee and offender - start
    public void commonUrlBuilder(Model model) {
        model.addAttribute("addStatus", true);
       // model.addAttribute("designations", Designation.values());
/*        model.addAttribute("provinces", Province.values());
        model.addAttribute("districtUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getDistrict", "")
                .build()
                .toString());
        model.addAttribute("stationUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getStation", "")
                .build()
                .toString());*/
        Object[] arg = {"designation", "id"};
        model.addAttribute("employeeUrl", MvcUriComponentsBuilder
                .fromMethodName(EmployeeRestController.class, "getEmployeeByWorkingPlace", arg)
                .build()
                .toString());
    }



}
