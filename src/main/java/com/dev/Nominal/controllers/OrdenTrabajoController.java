package com.dev.Nominal.controllers;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.OrdenTrabajo;
import com.dev.Nominal.models.entity.Personal;
import com.dev.Nominal.models.entity.Vehiculo;
import com.dev.Nominal.models.service.IClienteService;
import com.dev.Nominal.models.service.IOrdenTrabajoService;
import com.dev.Nominal.models.service.IPersonalService;
import com.dev.Nominal.models.service.IVehiculoService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/orden")
public class OrdenTrabajoController {

    @Autowired
    private IOrdenTrabajoService ordenTrabajoService;
    @Autowired
    private IPersonalService personalService;
    @Autowired
    private IVehiculoService vehiculoService;
    @Autowired
    private IClienteService clienteService;


    @GetMapping(value = "/listOrden")
    public String listOrden(Model model) {
        model.addAttribute("orden", ordenTrabajoService.findAll());
        return "/orden/ListOrden";
    }

    @GetMapping("/formOrden")
    public String createOrden(Map<String, Object> model) {
        OrdenTrabajo ordenTrabajo = new OrdenTrabajo();
        model.put("orden", ordenTrabajo);
        List<Personal> personalList = personalService.findAll(); // Obtener la lista de empleados
        model.put("personalList", personalList); // Agregar la lista de empleados al modelo
        return "/orden/formOrden";
    }

    @RequestMapping(value = "/formOrden", method = RequestMethod.POST)
    public String saveOrden(Map<String, Object> model,
                            @ModelAttribute OrdenTrabajo ordenTrabajo,
                            @RequestParam(value = "descripcion[]", required = false) List<String> descripciones,
                            @RequestParam(value = "detallesReparacion[]", required = false) List<String> detallesReparacion,
                            @RequestParam(value = "costoEstimado[]", required = false) List<Double> costosEstimados) {

        // Obtener vehículo y cliente basado en los IDs
        if (ordenTrabajo.getVehiculo() != null && ordenTrabajo.getVehiculo().getId() != null) {
            Vehiculo vehiculo = vehiculoService.findOne(ordenTrabajo.getVehiculo().getId());
            Client cliente = clienteService.findOne(vehiculo.getCliente().getId());
            ordenTrabajo.setVehiculo(vehiculo);
            ordenTrabajo.setCliente(cliente);
        }

        // Establecer el estado como ACTIVO
        ordenTrabajo.setEstado("ACTIVO");

        // Limpiar listas actuales
        ordenTrabajo.setDescripcion(new ArrayList<>());
        ordenTrabajo.setDetallesReparacion(new ArrayList<>());
        ordenTrabajo.setCostoEstimado(new ArrayList<>());

        // Agregar los datos a la entidad solo si las listas no son nulas
        if (descripciones != null && detallesReparacion != null && costosEstimados != null) {
            for (int i = 0; i < descripciones.size(); i++) {
                if (i < detallesReparacion.size() && i < costosEstimados.size()) {
                    ordenTrabajo.getDescripcion().add(descripciones.get(i));
                    ordenTrabajo.getDetallesReparacion().add(detallesReparacion.get(i));
                    ordenTrabajo.getCostoEstimado().add(costosEstimados.get(i));
                }
            }
        }

        // Guardar o actualizar la orden
        if (ordenTrabajo.getId() != null) {
            OrdenTrabajo existingOrden = ordenTrabajoService.findOne(ordenTrabajo.getId());
            existingOrden.setCodigo(ordenTrabajo.getCodigo());
            existingOrden.setFechaCreacion(ordenTrabajo.getFechaCreacion());
            existingOrden.setEstado(ordenTrabajo.getEstado());
            existingOrden.setDetallesReparacion(ordenTrabajo.getDetallesReparacion());
            existingOrden.setDescripcion(ordenTrabajo.getDescripcion());
            existingOrden.setCostoEstimado(ordenTrabajo.getCostoEstimado());
            existingOrden.setCostoTotal(ordenTrabajo.getCostoTotal());
            existingOrden.setPersonal(ordenTrabajo.getPersonal());
            ordenTrabajoService.save(existingOrden);
        } else {
            // Guardar la nueva orden y actualizar el código
            ordenTrabajoService.save(ordenTrabajo);
            ordenTrabajo.setCodigo(String.format("%07d", ordenTrabajo.getId())); // Luego establece el código
            ordenTrabajoService.save(ordenTrabajo);
        }

        return "redirect:/orden/listOrden";
    }
    @RequestMapping(value = "/formOrden/{id}")
    public String updateOrden(Map<String, Object> model, @PathVariable(value = "id") Long id) {
        OrdenTrabajo orden = ordenTrabajoService.findOne(id);
        if (orden == null) {
            return "redirect:/orden/listOrden";
        }
        model.put("orden", orden);

        // Añadir la lista de personal al modelo
        List<Personal> personalList = personalService.findAll();
        model.put("personalList", personalList);

        return "/orden/formOrden"; // Asegúrate de que esta ruta esté correcta
    }



    @GetMapping("/verOrden/{id}")
    public String verOrden(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        OrdenTrabajo orden = ordenTrabajoService.findOne(id);
        if (orden == null) {
            return "redirect:/orden/listOrden";
        }

        // Convertir listas a cadenas separadas por comas
        String descripcion = String.join(", ", orden.getDescripcion());
        String detallesReparacion = String.join(", ", orden.getDetallesReparacion());
        String costoEstimado = orden.getCostoEstimado().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        // Añadir las cadenas procesadas al modelo
        model.put("orden", orden);
        model.put("descripcion", descripcion);
        model.put("detallesReparacion", detallesReparacion);
        model.put("costoEstimado", costoEstimado);

        return "/orden/verOrden";
    }

    @GetMapping("/vehiculo/{placa}")
    @ResponseBody
    public Map<String, Object> getVehiculoByPlaca(@PathVariable String placa) {
        List<Vehiculo> vehiculos = vehiculoService.findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(placa);
        if (!vehiculos.isEmpty()) {
            Vehiculo vehiculo = vehiculos.get(0); // Retorna el primer vehículo encontrado
            Client cliente = vehiculo.getCliente();
            Map<String, Object> response = new HashMap<>();
            response.put("id", vehiculo.getId());
            response.put("marca", vehiculo.getMarca());
            response.put("modelo", vehiculo.getModelo());
            response.put("kilometraje", vehiculo.getKilometraje());
            response.put("placa", vehiculo.getPlaca());
            response.put("clienteId", cliente.getId());
            response.put("clienteNombre", cliente.getNombre());
            response.put("clienteTelefono", cliente.getTelefono());
            response.put("clienteDireccion", cliente.getDireccion());
            response.put("clienteEmail", cliente.getEmail());
            response.put("clienteCiRuc", cliente.getCi_ruc());
            return response;
        }
        return null; // Retorna null si no se encuentra ningún vehículo
    }

    @GetMapping("/finalizarOrden/{id}")
    public String finalizarOrden(@PathVariable Long id) {
        OrdenTrabajo orden = ordenTrabajoService.findOne(id);
        if (orden != null && "ACTIVO".equals(orden.getEstado())) {
            orden.setEstado("FINALIZADO");
            ordenTrabajoService.save(orden);
        }
        return "redirect:/orden/listOrden";
    }
    //buscar por cedula y placa
    @GetMapping("/buscar")
    public String buscarOrdenes(@RequestParam String term, Model model) {
        List<OrdenTrabajo> orden = ordenTrabajoService.findByClienteNombreOrVehiculoPlacaContainingIgnoreCase(term);
        model.addAttribute("orden", orden);  // Asegúrate de usar el mismo nombre que en el HTML (es "orden" en el th:each)
        return "orden/listOrden";  // Asegúrate de que esta ruta corresponda a la vista correcta
    }

    //Filtros por estado
    @GetMapping("/filtrarPorEstado")
    public String filtrarPorEstado(@RequestParam(required = false) String estado, Model model) {
        List<OrdenTrabajo> ordenes;
        if (estado == null || estado.isEmpty()) {
            ordenes = ordenTrabajoService.findAll(); // Muestra todas las órdenes si no se selecciona un estado
        } else {
            ordenes = ordenTrabajoService.findByEstado(estado); // Filtra por estado
        }
        model.addAttribute("orden", ordenes);
        return "/orden/listOrden";
    }

}


//
//    //Imprimir orde
//    @GetMapping("/reporte/pdf")
//    public void exportToPDF(@RequestParam(required = false) Long ordenId,
//                            HttpServletResponse response) throws IOException {
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=orden_trabajo.pdf");
//
//        // Crear el writer y el documento PDF
//        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
//        PdfDocument pdfDoc = new PdfDocument(pdfWriter);
//        Document document = new Document(pdfDoc);
//
//        // Obtener la orden de trabajo desde el servicio
//        OrdenTrabajo ordenTrabajo = ordenTrabajoService.findOne(ordenId); // Asegúrate de tener este método implementado
//
//        // Agregar contenido al PDF
//        document.add(new Paragraph("Reporte de Orden de Trabajo"));
//
//        // Crear una tabla para la información de la orden
//        Table table = new Table(2); // 2 columnas: una para el campo y otra para el valor
//        table.addCell("ID");
//        table.addCell(String.valueOf(ordenTrabajo.getId()));
//
//        table.addCell("Código");
//        table.addCell(String.valueOf(ordenTrabajo.getCodigo()));
//
//        table.addCell("Fecha de Creación");
//        table.addCell(String.valueOf(ordenTrabajo.getFechaCreacion()));
//
//        table.addCell("Estado");
//        table.addCell(ordenTrabajo.getEstado());
//
//        table.addCell("Descripción");
//        table.addCell((Cell) ordenTrabajo.getDescripcion());
//
//        table.addCell("Detalles de Reparación");
//        table.addCell((Cell) ordenTrabajo.getDetallesReparacion());
//
//        table.addCell("Costo Total");
//        table.addCell(String.valueOf(ordenTrabajo.getCostoTotal()));
//
//        // Si es necesario, también puedes agregar los datos relacionados como Vehículo, Cliente y Personal
//        table.addCell("Vehículo");
//        table.addCell(ordenTrabajo.getVehiculo().getMarca() + " " + ordenTrabajo.getVehiculo().getModelo());
//
//        table.addCell("Cliente");
//        table.addCell(ordenTrabajo.getCliente().getNombre() + " " + ordenTrabajo.getCliente().getNombre());
//
//        table.addCell("Personal Responsable");
//        table.addCell(ordenTrabajo.getPersonal().getNombre() + " " + ordenTrabajo.getPersonal().getApellido());
//
//        // Agregar la tabla al documento
//        document.add(table);
//
//        // Cerrar el documento
//        document.close();
//    }



//@RequestMapping(value = "/formOrden", method = RequestMethod.POST)
//public String saveOrden(Map<String, Object> model, @ModelAttribute OrdenTrabajo ordenTrabajo) {
//    // Obtener vehículo y cliente basado en los IDs
//    if (ordenTrabajo.getVehiculo() != null && ordenTrabajo.getVehiculo().getId() != null) {
//        Vehiculo vehiculo = vehiculoService.findOne(ordenTrabajo.getVehiculo().getId());
//        Client cliente = clienteService.findOne(vehiculo.getCliente().getId());
//        ordenTrabajo.setVehiculo(vehiculo);
//        ordenTrabajo.setCliente(cliente);
//    }
//    // Establecer el estado como ACTIVO
//    ordenTrabajo.setEstado("ACTIVO");
//
//    if (ordenTrabajo.getId() != null) {
//        OrdenTrabajo existingOrden = ordenTrabajoService.findOne(ordenTrabajo.getId());
//        existingOrden.setCodigo(ordenTrabajo.getCodigo());
//        existingOrden.setFechaCreacion(ordenTrabajo.getFechaCreacion());
//        existingOrden.setEstado(ordenTrabajo.getEstado());
//        existingOrden.setDetallesReparacion(ordenTrabajo.getDetallesReparacion());
//        existingOrden.setDescripcion(ordenTrabajo.getDescripcion());
//        existingOrden.setCostoEstimado(ordenTrabajo.getCostoEstimado());
//        existingOrden.setCostoTotal(ordenTrabajo.getCostoTotal());
//        existingOrden.setPersonal(ordenTrabajo.getPersonal());
//        ordenTrabajoService.save(existingOrden);
//    } else {
//        ordenTrabajoService.save(ordenTrabajo);
//        ordenTrabajo.setCodigo(String.format("%07d", ordenTrabajo.getId())); // Luego establece el código
//        ordenTrabajoService.save(ordenTrabajo);
//    }
//    return "redirect:/orden/listOrden";
//}
