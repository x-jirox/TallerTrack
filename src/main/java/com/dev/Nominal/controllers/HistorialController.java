package com.dev.Nominal.controllers;


import com.dev.Nominal.models.entity.OrdenTrabajo;
import com.dev.Nominal.models.service.IOrdenTrabajoService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/historial")
public class HistorialController {
    @Autowired
    private IOrdenTrabajoService ordenTrabajoService;

    //Lista de Historial
    @GetMapping(value = "/listHistorial")
    public String listHistorial(Model model) {
       model.addAttribute("orden", ordenTrabajoService.findAll());
        return "/historial/listHistorial";
    }

    @GetMapping("/buscar")
    public String buscarFiltro(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        // Lógica para obtener las órdenes filtradas
        List<OrdenTrabajo> orden = ordenTrabajoService.buscarPorFiltros(term.trim(), startDate, endDate);

        // Agregar el resultado al modelo
        model.addAttribute("orden", orden);
        // Añadir los filtros usados al modelo para prellenar los inputs
        model.addAttribute("term", term);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "historial/listHistorial";  // Asegúrate de que esta ruta corresponda a la vista correcta
    }

    // Método para exportar los datos filtrados o todos los datos a Excel
    @GetMapping("/export")
    public void exportToExcel(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletResponse response) throws IOException {

        // Configuración de la respuesta
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=orden_trabajo.xlsx";
        response.setHeader(headerKey, headerValue);

        // Obtener las órdenes de trabajo, todas o filtradas
        List<OrdenTrabajo> ordenes;
        if (term == null && startDate == null && endDate == null) {
            ordenes = ordenTrabajoService.findAll();  // Obtener todas las órdenes si no hay filtros
        } else {
            ordenes = ordenTrabajoService.buscarPorFiltros(term != null ? term.trim() : null, startDate, endDate);
        }

        // Crear el archivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Órdenes de Trabajo");

        // Crear la fila de cabeceras
        Row headerRow = sheet.createRow(0);
        String[] columnas = {
                "Código", "Estado", "Personal Encargado", "Fecha de Creación",
                "Cédula/RUC", "Nombre Cliente", "Dirección", "Teléfono",
                "Email", "Placa", "Marca", "Modelo", "Año",
                "Kilometraje", "Descripción", "Detalle", "Precio Estimado", "Precio Total"
        };
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Llenar los datos
        int rowNum = 1;
        for (OrdenTrabajo orden : ordenes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(orden.getCodigo()); // Código de la orden
            row.createCell(1).setCellValue(orden.getEstado()); // Estado de la orden
            row.createCell(2).setCellValue(orden.getPersonal().getNombre() + " " + orden.getPersonal().getApellido()); // Personal encargado
            row.createCell(3).setCellValue(orden.getFechaCreacion().toString()); // Fecha de creación
            row.createCell(4).setCellValue(orden.getCliente().getCi_ruc()); // Cédula/RUC
            row.createCell(5).setCellValue(orden.getCliente().getNombre()); // Nombre del cliente
            row.createCell(6).setCellValue(orden.getCliente().getDireccion()); // Dirección
            row.createCell(7).setCellValue(orden.getCliente().getTelefono()); // Teléfono
            row.createCell(8).setCellValue(orden.getCliente().getEmail()); // Email
            row.createCell(9).setCellValue(orden.getVehiculo().getPlaca()); // Placa
            row.createCell(10).setCellValue(orden.getVehiculo().getMarca()); // Marca
            row.createCell(11).setCellValue(orden.getVehiculo().getModelo()); // Modelo
            row.createCell(12).setCellValue(orden.getVehiculo().getAnyo()); // Año
            row.createCell(13).setCellValue(orden.getVehiculo().getKilometraje()); // Kilometraje

            // Detalles y descripción
            String detalles = String.join(", ", orden.getDetallesReparacion());
            row.createCell(14).setCellValue(detalles); // Detalles de reparación

            String descripcion = String.join(", ", orden.getDescripcion());
            row.createCell(15).setCellValue(descripcion); // Descripción

            row.createCell(16).setCellValue(orden.getCostoEstimado().toString()); // Precio estimado
            row.createCell(17).setCellValue(orden.getCostoTotal()); // Precio total
        }

        // Autoajustar las columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir el archivo Excel en la respuesta
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // Método para definir el estilo de las cabeceras
    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

}
