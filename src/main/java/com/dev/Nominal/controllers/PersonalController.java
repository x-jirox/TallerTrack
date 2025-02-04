package com.dev.Nominal.controllers;

import com.dev.Nominal.models.entity.Personal;
import com.dev.Nominal.models.service.IPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/person")
public class PersonalController {

    @Autowired
    private IPersonalService personalService;


    @GetMapping(value = "/listPerson")
    public String listPersonal(Model model) {
        model.addAttribute("personal", personalService.findAll());
        return "/person/listPerson";
    }

    @RequestMapping(value = "/formPerson")
    public String createPerson(Map<String, Object> model) {
        Personal personal = new Personal();
        model.put("personal", personal);
        return "/person/formPerson";
    }
    @RequestMapping(value = "/formPerson", method = RequestMethod.POST)
    public String savePerson(Map<String, Object> model, @ModelAttribute Personal personal) {
        if (personal.getId() != null) {
            personalService.save(personal);
        }else {
            personalService.save(personal);
        }
        return "redirect:/person/listPerson";
    }
    @RequestMapping(value = "/formPerson/{id}")
    public String updatePerson(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        Personal personal = personalService.findOne(id);
        if (personal == null) {
            return "redirect:/person/listPerson";
        }
        model.put("personal", personal);
        return "/person/formPerson";
    }
    @RequestMapping(value = "deletePerson/{id}")
    public String deletePerson(Map<String, Object> model, @PathVariable(value = "id") long id) {
        if (id>0) {
            personalService.delete(id);
        }
        return "redirect:/person/listPerson";
    }
}
