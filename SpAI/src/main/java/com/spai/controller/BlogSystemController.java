package com.spai.controller;


import com.spai.pojo.User;
import com.spai.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Blogs")
public class BlogSystemController {

    @Autowired
    private UserServiceImpl userservice;

    /*@Autowired
    private AdminServiceImpl adminservice;*/

    @GetMapping("/ShowUser")
    public String ShowUser(Model model) {
        List<User> users = userservice.getUsers();
        model.addAttribute("users", users);
        return "main/ShowUser";
    }

    @PostMapping("/addUser")
    public String addUser(User user, Model model) {
        user.setArticleNum(0);
        userservice.addUser(user);
        return "redirect:/Blogs/ShowUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(User user) {
        User u = userservice.selectById(user.getId());
        user.setArticleNum(u.getArticleNum());
        userservice.updateUser(user);
        return "redirect:/Blogs/ShowUser";
    }

    @GetMapping("/SelectUser")
    public String SelectUser(String username, Model model) {
        List<User> users = userservice.selectUser(username);
        model.addAttribute("users", users);
        return "main/SearchUser";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userservice.deleteUser(id);
        return "redirect:/Blogs/ShowUser";
    }

    @GetMapping("/home")
    public String home() {
        return "main/home";
    }

/*
    @PostMapping("/updateadmin")
    public String updateadmin(Admin admin, HttpSession session, RedirectAttributes redirectAttributes) {
        adminservice.updateadmin(admin);
        session.removeAttribute("admin");
        session.setAttribute("admin", admin);
        redirectAttributes.addFlashAttribute("msg", "updated");
        return "redirect:/Blogs/home";
    }
*/

    @GetMapping("/goupdateadminpassword")
    public String goupdateadminpassword() {

        return "main/updatePassword";
    }

/*    @PostMapping("/updateadminPassword")
    public String updatepassword(String username, String oldpassword, String newpassword1, String newpassword2, Model model) {
        String password = adminservice.getpassword(username);
        if (oldpassword.trim().equals("")) {
            model.addAttribute("state", "oldpasswordNull");
            model.addAttribute("newpassword1", newpassword1);
            model.addAttribute("newpassword2", newpassword2);
            return "main/updatePassword";
        } else if (!oldpassword.equals(password)) {
            model.addAttribute("state", "oldpasswordNotequal");
            model.addAttribute("oldpassword", oldpassword);
            model.addAttribute("newpassword1", newpassword1);
            model.addAttribute("newpassword2", newpassword2);
            return "main/updatePassword";
        } else if (newpassword1.equals(newpassword2) && !newpassword1.trim().equals("")) {
            adminservice.updatepassword(username, newpassword1);
            model.addAttribute("msg", "updated");
            return "main/updatePassword";
        } else if (newpassword1.trim().equals("") && newpassword2.trim().equals("")) {
            model.addAttribute("state", "passwordNull");
            model.addAttribute("oldpassword", oldpassword);
            model.addAttribute("newpassword1", newpassword1);
            model.addAttribute("newpassword2", newpassword2);
            return "main/updatePassword";
        } else {
            model.addAttribute("state", "Notequal");
            model.addAttribute("oldpassword", oldpassword);
            model.addAttribute("newpassword1", newpassword1);
            model.addAttribute("newpassword2", newpassword2);
            return "main/updatePassword";
        }
    }*/
}
