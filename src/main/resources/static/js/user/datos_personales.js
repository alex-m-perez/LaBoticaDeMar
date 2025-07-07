/**
 * Expone la función de inicialización para la sección "Mis Datos".
 * Es llamada por perfil.js después de cargar el fragmento HTML.
 */
function initializePage() {
    
    const API_URL_GET = `${window.contextPath}/api/user/data`;
    const API_URL_UPDATE = `${window.contextPath}/api/user/update_data`;
    const form = document.getElementById('profileForm');

    if (!form) {
        console.error("El formulario 'profileForm' no se encontró en el DOM.");
        return;
    }

    // 1. Llama a la API para obtener los datos.
    fetch(API_URL_GET)
        .then(response => {
            if (!response.ok) throw new Error('No se pudieron obtener los datos del perfil.');
            return response.json();
        })
        .then(dto => {
            populateForm(dto);
            setupEventListeners(dto);
        })
        .catch(error => {
            console.error('Error al cargar datos del perfil:', error);
            form.innerHTML = `<p class="text-red-500 text-center">Error al cargar tus datos.</p>`;
        });

    /**
     * Rellena el formulario usando los datos del DTO.
     */
    function populateForm(dto) {
        document.getElementById('user-points').textContent = dto.puntos || 0;
        
        form.querySelector('#nombre').value = dto.nombre || '';
        form.querySelector('#apellido1').value = dto.apellido1 || '';
        form.querySelector('#apellido2').value = dto.apellido2 || '';
        form.querySelector('#telefono').value = dto.telefono || '';
        
        if (dto.fechaNac) {
            form.querySelector('#fechaNac').value = dto.fechaNac;
        }
        form.querySelector('#genero').value = dto.genero;

        form.querySelector('#calle').value = dto.calle || '';
        form.querySelector('#numero').value = dto.numero || '';
        form.querySelector('#piso').value = dto.piso || '';
        form.querySelector('#puerta').value = dto.puerta || '';
        form.querySelector('#localidad').value = dto.localidad || '';
        form.querySelector('#codigoPostal').value = dto.codigoPostal || '';
        form.querySelector('#provincia').value = dto.provincia || '';
        form.querySelector('#pais').value = dto.pais || '';
    }

    /**
     * Configura los event listeners para el formulario.
     */
    function setupEventListeners(dto) {
        const interesElements = form.querySelectorAll('.select-interes');
        const preferenciasInput = form.querySelector('#preferenciasInput');
        const selectedIntereses = new Set(dto.preferencias || []);

        function updateInterestStyle(element, isSelected) {
            if (isSelected) {
                element.classList.remove('border-gray-300');
                element.classList.add('border-2', 'border-pistachio');
            } else {
                element.classList.remove('border-2', 'border-pistachio');
                element.classList.add('border-gray-300');
            }
        }

        interesElements.forEach(el => {
            const id = parseInt(el.dataset.id, 10);
            updateInterestStyle(el, selectedIntereses.has(id));
            
            el.addEventListener('click', () => {
                if (selectedIntereses.has(id)) {
                    selectedIntereses.delete(id);
                } else {
                    selectedIntereses.add(id);
                }
                updateInterestStyle(el, selectedIntereses.has(id));
            });
        });

        form.addEventListener('submit', (event) => {
            event.preventDefault();
            
            const formData = new FormData(form);
            const dataToSend = Object.fromEntries(formData.entries());
            dataToSend.preferencias = Array.from(selectedIntereses);
            
            fetch(API_URL_UPDATE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dataToSend)
            })
            .then(response => {
                if (!response.ok) throw new Error('Error al guardar los datos.');
                alert("¡Datos guardados correctamente!");
            })
            .catch(error => {
                console.error('Error en el envío del formulario:', error);
                alert("Hubo un problema al guardar tus datos.");
            });
        });
    }
}