package com.dev.Nominal.controllers;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.Vehiculo;
import com.dev.Nominal.models.service.IClienteService;
import com.dev.Nominal.models.service.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/vehiculo")
public class VehiculoController {
    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IClienteService clienteService;

    //Lista de Vehiculos
    @GetMapping(value = "/listVehiculo")
    public String listVehiculo(Model model) {
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "/vehiculo/listVehiculo";
    }

    @RequestMapping(value = "/formVehiculo")
    public String createVehiculo(Map<String, Object> model) {
        Vehiculo vehiculo = new Vehiculo();
        List<Client> clientes = clienteService.findAll();
        model.put("vehiculo", vehiculo);
        model.put("clientes", clientes);
        return "/vehiculo/formVehiculo";
    }
    //Guardar al Vehiculo
   // Guardar o Actualizar Vehiculo
    @RequestMapping(value = "/formVehiculo", method = RequestMethod.POST)
    public String saveVehiculo(Map<String, Object> model, @ModelAttribute Vehiculo vehiculo) {
        if (vehiculo.getId() != null) {
            // Vehiculo existente, actualizar
            vehiculoService.save(vehiculo); // Asegúrate de tener un método update en tu servicio
        } else {
            // Nuevo Vehiculo, crear
            vehiculoService.save(vehiculo);
        }
        return "redirect:/vehiculo/listVehiculo";
    }

    //Buscar  de datos del vehiculo
    @RequestMapping(value = "/formVehiculo/{id}")
    public String updateVehiculo(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        Vehiculo vehiculo = vehiculoService.findOne(id);
        if (vehiculo == null) {
            return "redirect:/vehiculo/listVehiculo";
        }
        List<Client> clientes = clienteService.findAll();
        model.put("vehiculo", vehiculo);
        model.put("clientes", clientes);
        return "/vehiculo/formVehiculo";
    }

    //Eliminar Vehiculos
    @RequestMapping(value = "deleteVehiculo/{id}")
    public String deleteVehiculo(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        if (id >0) {
            vehiculoService.delete(id);
        }
        return "redirect:/vehiculo/listVehiculo";
    }

    //Buscar al vehiculo por sus parametros
    @GetMapping("/searchVehiculo")
    public String searchClient(Model model, Vehiculo vehiculo, @RequestParam("term") String term) {
        List<Vehiculo> vehiculos = vehiculoService.findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(term);
        model.addAttribute("vehiculos", vehiculos);
        return "/vehiculo/listVehiculo"; // Cambia el nombre de la vista si es necesario

    }


}
