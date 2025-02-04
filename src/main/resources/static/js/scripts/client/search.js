document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.querySelector('#searchInput');
    const searchForm = document.querySelector('#searchForm');

    searchInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Evita el comportamiento por defecto del Enter en formularios
            searchForm.submit(); // Envía el formulario cuando se presiona Enter
        }
    });
});