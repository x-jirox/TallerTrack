$(document).ready(function() {
    $('#verificarBtn').click(function() {
        const placa = $('#placasearch').val();

        if (!placa) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor ingrese una placa.',
            });
            return;
        }
        $.ajax({
            url: '/orden/vehiculo/' + placa,
            type: 'GET',
            success: function(data) {
                if (data) {
                    $('#placa').val(data.placa);
                    $('#marca').val(data.marca);
                    $('#kilometraje').val(data.kilometraje);
                    $('#idVehiculo').val(data.id);
                    $('#idCliente').val(data.clienteId);

                    $('#clienteNombre').val(data.clienteNombre);
                    $('#clienteTelefono').val(data.clienteTelefono);
                    $('#clienteDireccion').val(data.clienteDireccion);
                    $('#clienteEmail').val(data.clienteEmail);
                    $('#clienteCiRuc').val(data.clienteCiRuc);

                    Swal.fire({
                        icon: 'success',
                        title: 'Vehículo encontrado',
                        text: 'Los datos del vehículo se han autocompletado.',
                    });
                } else {
                    $('#placa').val('');
                    $('#marca').val('');
                    $('#kilometraje').val('');
                    $('#idVehiculo').val('');
                    $('#idCliente').val('');

                    $('#clienteNombre').val('');
                    $('#clienteTelefono').val('');
                    $('#clienteDireccion').val('');
                    $('#clienteEmail').val('');
                    $('#clienteCiRuc').val('');

                    Swal.fire({
                        icon: 'error',
                        title: 'Vehículo no encontrado',
                        text: 'No se encontraron datos para la placa ingresada.',
                    });
                }

                // Limpiar el campo de búsqueda de placa
                $('#placasearch').val('');
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un problema al verificar la placa.',
                });

                // Limpiar el campo de búsqueda de placa en caso de error
                $('#placasearch').val('');
            }
        });
    });

    $('#formOrden').submit(function(e) {
        let isValid = true;

        // Validar campo de fecha
        if ($('#fecha').val() === '') {
            isValid = false;
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor complete la fecha.',
            });
        }

        // Validar campo de personal encargado
        if ($('#personal').val() === '') {
            isValid = false;
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor seleccione un personal encargado.',
            });
        }

        // Validar que la tabla de datos adicionales tenga al menos una fila con todos los campos llenos
        let allFieldsFilled = true;
        $('#datosAdicionalesTable tbody tr').each(function() {
            const descripcion = $(this).find('input[name="descripcion[]"]').val();
            const detalle = $(this).find('input[name="detallesReparacion[]"]').val();
            const precio = $(this).find('input[name="costoEstimado[]"]').val();

            if (!descripcion || !detalle || !precio) {
                allFieldsFilled = false;
                return false; // Salir del bucle
            }
        });

        if (!allFieldsFilled) {
            isValid = false;
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor complete todos los campos en la tabla de datos adicionales.',
            });
        }

        // Si no es válido, evitar el envío del formulario
        if (!isValid) {
            e.preventDefault();
        }
    });
});