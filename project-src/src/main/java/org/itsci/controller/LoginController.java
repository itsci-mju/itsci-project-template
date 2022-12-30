package org.itsci.attendance.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController extends CommonController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "ลงชื่อเข้าสู่ระบบ");
        return "login-page";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied(Model model) {
        model.addAttribute("title", "Access Denied");
        return "access-denied";
    }
}
