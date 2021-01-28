package lk.crystal.asset.user_management.user.controller;

import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.employee.entity.Employee;
import lk.crystal.asset.employee.entity.enums.Designation;
import lk.crystal.asset.employee.entity.enums.EmployeeStatus;
import lk.crystal.asset.employee.service.EmployeeService;
import lk.crystal.asset.user_management.role.service.RoleService;
import lk.crystal.asset.user_management.user.entity.User;
import lk.crystal.asset.user_management.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/user" )
public class UserController {
  private final UserService userService;
  private final RoleService roleService;
  private final EmployeeService employeeService;

  @Autowired
  public UserController(UserService userService, EmployeeService employeeService, RoleService roleService
                       ) {
    this.userService = userService;
    this.employeeService = employeeService;
    this.roleService = roleService;
  }

  @GetMapping
  public String userPage(Model model) {
//    List< User > users =
//        userService.findAll().stream().filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead())).collect(Collectors.toList());
    model.addAttribute("users", userService.findAll()
        .stream()
        .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
        .collect(Collectors.toList()));
//    List<User> allUsersc= userService.findAll();
//    System.out.println("dsdss all user "+allUsersc.size() );
//    System.out.println("dsdss  user "+userService.findByLiveDead(LiveDead.ACTIVE).size());
    model.addAttribute("users",userService.findByLiveDead(LiveDead.ACTIVE));
    return "user/user";
  }

  @GetMapping( value = "/{id}" )
  public String userView(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("userDetail", userService.findById(id));
    return "user/user-detail";
  }

  private String commonCode(Model model) {
    model.addAttribute("employeeDetailShow", true);
    model.addAttribute("employeeNotFoundShow", false);
    model.addAttribute("roleList", roleService.findAll());
        /*model.addAttribute("districtUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getDistrict", "")
                .build()
                .toString());
        model.addAttribute("stationUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getStation", "")
                .build()
                .toString());*/
    return "user/addUser";
  }

  @GetMapping( value = "/edit/{id}" )
  public String editUserFrom(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("user", userService.findById(id));
    model.addAttribute("addStatus", false);
    return commonCode(model);
  }

  @GetMapping( value = "/add" )
  public String userAddFrom(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("employeeDetailShow", false);
    model.addAttribute("employee", new Employee());
    return "user/addUser";
  }

  //Send a searched employee to add working place
  @PostMapping( value = "/workingPlace" )
  public String addUserEmployeeDetails(@ModelAttribute( "employee" ) Employee employee, Model model) {

    List< Employee > employees = employeeService.search(employee)
        .stream()
        .filter(userService::findByEmployee)
        .collect(Collectors.toList());

    if ( employees.size() == 1 ) {
      model.addAttribute("user", new User());
      model.addAttribute("employee", employees.get(0));
      model.addAttribute("addStatus", true);
      return commonCode(model);
    }
    model.addAttribute("addStatus", true);
    model.addAttribute("employee", new Employee());
    model.addAttribute("employeeDetailShow", false);
    model.addAttribute("employeeNotFoundShow", true);
    model.addAttribute("employeeNotFound", "There is not employee in the system according to the provided details" +
        " or that employee already be a user in the system" +
        " \n Could you please search again !!");

    return "user/addUser";
  }

  // Above method support to send data to front end - All List, update, edit
  //Bellow method support to do back end function save, delete, update, search

  @PostMapping( value = {"/add", "/update"} )
  public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {

    if ( userService.findUserByEmployee(user.getEmployee()) != null ) {
      ObjectError error = new ObjectError("employee", "This employee already defined as a user");
      result.addError(error);
    }
    if ( user.getId() != null ) {
      User dbUser = userService.findById(user.getId());
      dbUser.setEnabled(true);
      dbUser.setPassword(user.getPassword());
      dbUser.setEmployee(user.getEmployee());
      dbUser.setRoles(user.getRoles());
      userService.persist(dbUser);
      return "redirect:/user";
    }
    if ( result.hasErrors() ) {
      System.out.println("result to string    " + result.toString());
      model.addAttribute("addStatus", false);
      model.addAttribute("user", user);
      return commonCode(model);
    }
    //user is super senior office need to provide all working palace to check
    Employee employee = employeeService.findById(user.getEmployee().getId());
    Designation designation = employee.getDesignation();

    // userService.persist(user);
    user.setEnabled(employee.getEmployeeStatus().equals(EmployeeStatus.WORKING));
    user.setRoles(user.getRoles());
    user.setEnabled(true);
    userService.persist(user);
    return "redirect:/user";
  }


  @GetMapping( value = "/remove/{id}" )
  public String removeUser(@PathVariable Integer id) {
    // user can not be deleted
    //userService.delete(id);
    return "redirect:/user";
  }

  @GetMapping( value = "/search" )
  public String search(Model model, User user) {
    model.addAttribute("userDetail", userService.search(user));
    return "user/user-detail";
  }
}
