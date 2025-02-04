package com.dev.Nominal.controllers;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private IClienteService clienteService;

    //Lista de Clientes
    @GetMapping(value = "/listClient")
    public String listClient(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "/client/listClient";
    }
    @RequestMapping(value = "/formClient")
    public String createClient(Map<String, Object> model) {
        Client client = new Client();
        model.put("client", client);
        return "/client/formClient";
    }
    //Guardar al Cliente
// Guardar o Actualizar Cliente
    @RequestMapping(value = "/formClient", method = RequestMethod.POST)
    public String saveClient(Map<String, Object> model, @ModelAttribute Client client) {
        if (client.getId() != null) {
            // Cliente existente, actualizar
            clienteService.save(client); // Asegúrate de tener un método update en tu servicio
        } else {
            // Nuevo cliente, crear
            clienteService.save(client);
        }
        return "redirect:/client/listClient";
    }

    //Edicion de datos del cliente
    @RequestMapping(value = "/formClient/{id}")
    public String updateClient(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        Client client = clienteService.findOne(id);
        if (client == null) {
            return "redirect:/client/listClient";
        }
        model.put("client", client);
        return "/client/formClient";
    }

    //Eliminar Clientes
    @RequestMapping(value = "deleteClient/{id}")
    public String deleteClient(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        if (id >0) {
            clienteService.delete(id);
        }
        return "redirect:/client/listClient";
    }

    //Buscar al cliente por nombre y ci
    @GetMapping("/searchClient")
    public String searchClient(Model model, Client client, @RequestParam("term") String term) {
        List<Client> clients = clienteService.findByNombreOrCiRucContainingIgnoreCase(term);
        model.addAttribute("clientes", clients);
        return "/client/listClient"; // Cambia el nombre de la vista si es necesario

    }

}
