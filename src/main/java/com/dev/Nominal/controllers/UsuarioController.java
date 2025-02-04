package com.dev.Nominal.controllers;

import com.dev.Nominal.models.entity.Rol;
import com.dev.Nominal.models.entity.User;
import com.dev.Nominal.models.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;


// por revisar debido a la creacion de global controller
    @GetMapping("/index")
    public String index(@RequestParam(defaultValue = "0") int page, Model model, Principal principal) {
        return "index";
    }

    @GetMapping("/registro")
    public String formRegistro(Model model) {
        model.addAttribute("usuario", new User());
        return "user/registro";
    }

    @GetMapping("/registroEmpleados")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new User());
        return "user/registroEmpleados";
    }


    @GetMapping("/login")
    public String login(Model model) {
        return "user/login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "Access Denied");
        return "user/accessDenied";
    }

}
