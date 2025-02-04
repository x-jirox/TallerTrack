document.addEventListener('DOMContentLoaded', function () {
    const updateButtons = document.querySelectorAll('.update-btn');
    const deleteButtons = document.querySelectorAll('.delete-btn');

    updateButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita la navegación por defecto

            const url = this.getAttribute('href'); // Obtiene la URL del atributo href

            Swal.fire({
                title: '¿Estás seguro de Actualizar?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, actualizar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = url; // Navega a la URL si se confirma
                }
            });
        });
    });

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Evita la navegación por defecto

            const url = this.getAttribute('href'); // Obtiene la URL del atributo href

            Swal.fire({
                title: '¿Estás seguro de eliminar?',
                text: "¡No podrás deshacer esta acción!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = url; // Navega a la URL si se confirma
                }
            });
        });
    });
});