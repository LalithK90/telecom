package lk.crystal.util.interfaces;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

public interface AbstractController<E, I> {
    /**
     * 1. Find All relevant things belongs provided entity {}
     */
    String findAll(Model model);

    /**
     * 2. Find One relevant things belongs provided entity {} id
     */
    String findById(I id, Model model);

    /**
     * 3. Find One and send data to frontend to Edit relevant things belongs provided entity {} id
     */
    String edit(I id, Model model);

    /**
     * 4. Save and Update {} data using relevant entity belongs to model Attribute
     */
    String persist(@Valid @ModelAttribute E e, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model);

    /**
     * 5. Remove One relevant things belongs provided entity {} id
     */
    String delete(I id, Model model);

}
