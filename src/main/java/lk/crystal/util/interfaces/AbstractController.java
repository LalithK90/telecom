package lk.crystal.util.interfaces;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AbstractController< E, I > {

    //1. findAll method create.
    //2. addForm method create.
    //3. persist method create.
    //4. edit method create.
    //5. delete method create.
    //6. view details.

    String findAll(Model model);

    String addForm(Model model);

    String persist(E e, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception;

    String edit(I id, Model model);

    String delete(I id, Model model);

    String view(I id, Model model);
}