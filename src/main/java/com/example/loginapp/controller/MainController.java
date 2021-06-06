package com.example.loginapp.controller;

import com.example.loginapp.DAO.CrudNoteEntity;
import com.example.loginapp.DAO.CrudUserEntity;
import com.example.loginapp.DAO.NoteEntityRepo;
import com.example.loginapp.model.NoteEntity;
import com.example.loginapp.model.UserEntity;
import com.example.loginapp.service.LoginServiceImpl;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    LoginServiceImpl service;

    @Autowired
    CrudUserEntity userRepo;

    @Autowired
    CrudNoteEntity noteRepo;

    @Autowired
    NoteEntityRepo noteEntityRepo;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            return "login_sucess";
        }
        return "home";
    }


    @GetMapping("/login")
    public String showLogin(Model model, Principal principal) {
        model.addAttribute("user", new UserEntity());
        return "login";
    }


    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }


    @PostMapping("/signup")
    public String register(@ModelAttribute("user") UserEntity userEntity, Model model, HttpServletRequest request) {
        userEntity.setPhoneNumber(request.getParameter("phone_number"));
        if (service.checkEmail(userEntity.getEmail())) {
            if (service.save(userEntity)) {
                return "redirect:/login";
            }
            model.addAttribute("error", "Tài khoản đã tồn tại.");
        } else {
            model.addAttribute("error", "Email đã tồn tại.");
        }
        return "register";
    }

    @GetMapping("/addnote")
    public String showAddnote(Model model) {
        model.addAttribute("note", new NoteEntity());
        return "addnote";
    }

    @PostMapping("/addnote")
    public String addNote(@ModelAttribute("note") NoteEntity noteEntity, Principal principal) {
        UserEntity userEntity = userRepo.findByUsername(principal.getName());
        service.addnote(noteEntity, userEntity);
        return "redirect:/listnote";
    }

    @GetMapping("/listnote")
    public String showListnote(Model model, Principal principal) {
        model.addAttribute("notes", noteRepo.findByUserEntity(userRepo.findByUsername(principal.getName()).getId()));
        return "listnote";
    }

    @PostMapping("/listnote")
    public String seacrh(Model model, HttpServletRequest request) {
        return "redirect:/listnote/searchName=" + request.getParameter("search");
    }

    @GetMapping("/listnote/searchName={name}")
    public String showSearch(Model model, @PathVariable("name") String name, Principal principal) {
      model.addAttribute("notes",noteEntityRepo.findByUserEntityAndTitleContaining(userRepo.findByUsername(principal.getName()),name));
        return "listnote";
    }

    @GetMapping("/listnote={id}")
    public String showNote(Model model, @PathVariable("id")Long id) {
        model.addAttribute("note", noteRepo.findById(id).get());
        return "showNote";
    }

    @PostMapping("/listnote={id}")
    public String actionNote(HttpServletRequest request, @ModelAttribute("note")NoteEntity noteEntity, @PathVariable("id")Long id) {
       if (request.getParameter("update") != null) {
           noteEntity.setTitle(noteEntity.getTitle());
           noteEntity.setContent(request.getParameter("content"));
           service.updateNote(noteEntity,id);
       } else {
           NoteEntity noteEntity1 = noteRepo.findById(id).get();
           noteRepo.delete(noteEntity1);
       }
        return "redirect:/listnote";
    }

    @GetMapping("/setting")
    public String setting(Model model, Principal principal){
        model.addAttribute("user", userRepo.findByUsername(principal.getName()));
        return "setting";
    }

    @PostMapping("/setting")
    public String settingUpdate(@ModelAttribute("user")UserEntity userEntity, Principal principal){
        service.updateUser(userEntity, principal.getName());
        return "redirect:/setting";
    }
    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("notes", noteRepo.findAll());
        return "admin";
    }

    @PostMapping("/admin")
    public String seacrhAD(Model model, HttpServletRequest request) {
        return "redirect:/admin/searchName=" + request.getParameter("search");
    }

    @GetMapping("admin/searchName={name}")
    public String listUser(Model model, @PathVariable("name")String name) {
        model.addAttribute("notes",noteRepo.search("%"+name+"%"));
        return "admin";
    }
    @GetMapping("/admin/memberName={name}")
    public String adminSetting(Model model, @PathVariable("name")String name) {
        model.addAttribute("user",userRepo.findByUsername(name));
        return "setting";
    }
    @GetMapping("/403")
    public String _403Page() {
        return "403";
    }
}
