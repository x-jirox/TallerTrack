// Función para calcular el total de los precios
function calculateTotal() {
    var total = 0;
    document.querySelectorAll('.precio-input').forEach(function (input) {
        var value = parseFloat(input.value.replace(",", ".")) || 0; // Convertir a número, si está vacío toma 0
        total += value;
    });
    document.getElementById('totalPrecio').value = total.toFixed(2); // Mostrar el total con dos decimales
}

// Función para verificar si hay más de una fila, para mostrar o esconder botones de eliminar
function toggleRemoveButtons() {
    var rows = document.querySelectorAll('#datosAdicionalesTable tbody tr');
    var removeButtons = document.querySelectorAll('.remove-row-btn');

    if (rows.length > 1) {
        removeButtons.forEach(function (button) {
            button.style.display = 'inline-block'; // Mostrar el botón si hay más de una fila
        });
    } else {
        removeButtons.forEach(function (button) {
            button.style.display = 'none'; // Ocultar el botón si solo hay una fila
        });
    }
}

// Función para agregar nuevas filas
document.getElementById('addRowBtn').addEventListener('click', function () {
    var table = document.getElementById('datosAdicionalesTable').getElementsByTagName('tbody')[0];
    var newRow = table.insertRow();

    // Columna de Descripción
    var cell1 = newRow.insertCell(0);
    var descripcionInput = document.createElement('input');
    descripcionInput.type = 'text';
    descripcionInput.name = 'descripcion[]';
    descripcionInput.className = 'form-control';
    descripcionInput.placeholder = 'Descripción';
    cell1.appendChild(descripcionInput);

    // Columna de Detalle
    var cell2 = newRow.insertCell(1);
    var detalleInput = document.createElement('input');
    detalleInput.type = 'text';
    detalleInput.name = 'detallesReparacion[]';
    detalleInput.className = 'form-control';
    detalleInput.placeholder = 'Detalle';
    cell2.appendChild(detalleInput);

    // Columna de Precio
    var cell3 = newRow.insertCell(2);
    var precioInput = document.createElement('input');
    precioInput.type = 'number';
    precioInput.name = 'costoEstimado[]';
    precioInput.className = 'form-control precio-input';
    precioInput.placeholder = 'costoEstimado';
    precioInput.step = "0.01";
    precioInput.min = "0";
    precioInput.required = true;
    precioInput.addEventListener('input', calculateTotal); // Actualizar total al modificar precio
    cell3.appendChild(precioInput);

    // Columna de Acción (botón de eliminar)
    var cell4 = newRow.insertCell(3);
    var removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.className = 'btn btn-danger remove-row-btn';
    removeBtn.textContent = 'X';
    removeBtn.addEventListener('click', function () {
        table.deleteRow(newRow.rowIndex - 1);
        calculateTotal(); // Actualizar total al eliminar fila
        toggleRemoveButtons(); // Verificar si se deben mostrar/ocultar los botones de eliminar
    });
    cell4.appendChild(removeBtn);

    toggleRemoveButtons(); // Verificar los botones de eliminar después de agregar una nueva fila
});

// Inicialmente esconder el botón de eliminar si solo hay una fila
toggleRemoveButtons();
