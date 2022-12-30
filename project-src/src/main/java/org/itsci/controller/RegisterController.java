package org.itsci.controller;

import org.itsci.model.User;
import org.itsci.service.UserService;
import org.itsci.utils.UIValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
public class RegisterController {
    @Autowired
    ResourceBundleMessageSource messageSource;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showFormForRegister(Model model) {
        model.addAttribute("title", messageSource.getMessage("page.register.title", null, Locale.getDefault()));
        model.addAttribute("user", new User());
        return "register/register-form";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {
        if (!UIValidator.FieldNotNullValidator(user, "username")) {
            bindingResult.rejectValue("username", "NotNull");
        } else if (!UIValidator.FieldPatternValidator(user, "username")) {
            bindingResult.rejectValue("username", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(user, "password")) {
            bindingResult.rejectValue("password", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(user, "confirmPassword")) {
            bindingResult.rejectValue("confirmPassword", "NotNull");
        } else if (!UIValidator.FieldsValueMatchValidator(user, "password", "confirmPassword")) {
            bindingResult.rejectValue("confirmPassword", "MisMatch");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("page.error", null, Locale.getDefault()));
            return "register/register-form";
        } else {
            userService.register(user);
            return "redirect:/";
        }
    }
}
