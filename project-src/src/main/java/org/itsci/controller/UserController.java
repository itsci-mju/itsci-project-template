package org.itsci.controller;

import org.itsci.model.Authority;
import org.itsci.model.AuthorityType;
import org.itsci.model.User;
import org.itsci.service.UserService;
import org.itsci.utils.UIValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    ResourceBundleMessageSource messageSource;

    @Autowired
    UserService userService;

    @InitBinder
    public void initBuilder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listShop(Model model) {
        model.addAttribute("title", messageSource.getMessage("page.user.list", null, Locale.getDefault()));
        model.addAttribute("users", userService.getUsers());
        return "user/list";
    }

    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {
        User currUser = (User) authentication.getPrincipal();
        User user = userService.getUser(currUser.getId());
        model.addAttribute("user", user);
        model.addAttribute("title", messageSource.getMessage("page.user.profile", null, Locale.getDefault()));
        return "user/profile";
    }

    @PostMapping("/profile")
    public String userSave(@ModelAttribute("user") User userFrm,
                             BindingResult bindingResult,
                             Model model,
                             Locale locale,
                             RedirectAttributes redirectAttrs) {
        User user = userService.getUser(userFrm.getId());

        if (!UIValidator.FieldNotNullValidator(userFrm, "firstName")) {
            bindingResult.rejectValue("firstName", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(userFrm, "lastName")) {
            bindingResult.rejectValue("lastName", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(userFrm, "address")) {
            bindingResult.rejectValue("address", "NotNull");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("page.error", null, Locale.getDefault()));
            return "user/profile";
        } else {
            user.setFirstName(userFrm.getFirstName());
            user.setLastName(userFrm.getLastName());
            user.setAddress(userFrm.getAddress());
            userService.saveUser(user);
            String message = messageSource.getMessage("status.save.success", null, locale);
            redirectAttrs.addFlashAttribute("status", message);
            return "redirect:/";
        }
    }

    @GetMapping("/create")
    public String showFormForAdd(Locale locale, Model model) {
        model.addAttribute("title", messageSource.getMessage("page.user.add", null, Locale.getDefault()));
        model.addAttribute("authorities", getAuthorityOptions(locale));
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("user", new User());
        model.addAttribute("disabled", "false");
        return "user/form";
    }

    @GetMapping("/{id}/update")
    public String showFormForUpdate(@PathVariable("id") int id, Locale locale, Model model) {
        User user = userService.getUser(Long.valueOf(id));
        model.addAttribute("title", messageSource.getMessage("page.user.update", null, Locale.getDefault()));
        model.addAttribute("authorities", getAuthorityOptions(locale));
        model.addAttribute("user", user);
        model.addAttribute("disabled", "true");
        return "user/form";
    }

    private Map<String, String> getAuthorityOptions(Locale locale) {
        List<String> authorities = AuthorityType.getAuthorities();
        Map<String, String> authorityOptions = new HashMap<>();
        for (String authority : authorities) {
            String label = messageSource.getMessage("enum.AuthorityType." + authority, null, locale);
            authorityOptions.put(authority, label);
        }
        return authorityOptions;
    }

    @RequestMapping(path="/save", method = RequestMethod.POST)
    public String processForm(@ModelAttribute("user") User user,
                              BindingResult bindingResult,
                              Model model) {
        List<Authority> authorityToAdd = new ArrayList<>();
        List<Authority> authorityToRemove = new ArrayList<>();

        if (!UIValidator.FieldNotNullValidator(user, "firstName")) {
            bindingResult.rejectValue("firstName", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(user, "lastName")) {
            bindingResult.rejectValue("lastName", "NotNull");
        }
        if (!UIValidator.FieldNotNullValidator(user, "address")) {
            bindingResult.rejectValue("address", "NotNull");
        }
        if (user.getLogin().getAuthorities().size() <= 0) {
            bindingResult.rejectValue("authoritiyOptions", "NotNull");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("page.error", null, Locale.getDefault()));
            model.addAttribute("authorities", AuthorityType.getAuthorities());
            model.addAttribute("user", user);
            model.addAttribute("disabled", "true");
            return "user/form";
        }
        else {
            User dbUser = userService.getUser(user.getId());
            if (dbUser == null) {
                dbUser = new User();
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String encrypted = bCryptPasswordEncoder.encode(user.getUsername().trim());
                dbUser.getLogin().setPassword("{bcrypt}" + encrypted);
            }

            dbUser.getLogin().setUsername(user.getUsername());
            dbUser.setFirstName(user.getFirstName());
            dbUser.setLastName(user.getLastName());
            dbUser.setAddress(user.getAddress());
            dbUser.getLogin().setEnabled(true);

            Set<Authority> authorities = dbUser.getLogin().getAuthorities();
            for (Authority authority : authorities) {
                boolean found = false;
                for (Authority auth : user.getLogin().getAuthorities()) {
                    if (authority.equals(auth)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    authorityToRemove.add(authority);
                }
            }
            for (Authority auth : user.getLogin().getAuthorities()) {
                boolean found = false;
                for (Authority authority : authorities) {
                    if (authority.equals(auth)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    authorityToAdd.add(auth);
                }
            }

            userService.updateUser(dbUser, authorityToRemove, authorityToAdd);
            return "redirect:/user/list";
        }
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }
}
