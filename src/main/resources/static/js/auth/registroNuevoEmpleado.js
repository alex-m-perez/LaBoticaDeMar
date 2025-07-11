document.addEventListener('DOMContentLoaded', () => {
    // --- Referencias a elementos del DOM ---
    const form = document.getElementById('register-form');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const progress = document.getElementById('progress-bar');
    const stepLabel = document.getElementById('step-label');
    const stepTitle = document.getElementById('step-title');
    const step1 = document.getElementById('step-1');
    const step2 = document.getElementById('step-2');
    // Se eliminó la referencia a step-3

    const pwdField = document.getElementById('passwd');
    const confirmPwd = document.getElementById('confirm_passwd');
    const togglePwd = document.getElementById('toggle-password');
    const correoField = document.getElementById('correo');

    // Listas para validación (se mantienen)
    const allowedSpecials = '!@#$%^&*()+-=[]{};,<>/?|~';
    const allowedEmailDomains = [
        'gmail.com', 'hotmail.com', 'outlook.com', 'live.com', 'icloud.com',
        'yahoo.com', 'yahoo.es', 'protonmail.com', 'gmx.es', 'msn.com',
        'hotmail.es', 'outlook.es', 'live.es', 'telefonica.net', 'terra.es',
        'ono.com', 'jazztel.com', 'wanadoo.es', 'ya.com', 'orange.es',
        'movistar.es', 'vodafone.es'
    ];

    let currentStep = 1;
    const TOTAL_STEPS = 2; // El total de pasos ahora es 2

    // --- Funciones de utilidad y validación (la mayoría se mantienen) ---

    // Quitar borde rojo al rellenar (sin cambios)
    document.querySelectorAll('#register-form input[required], #register-form select[required]').forEach(field => {
        field.addEventListener('input', () => {
            if (field.value.trim()) {
                field.classList.remove('border-2', 'border-red-500');
            }
        });
    });

    // Toggle para mostrar/ocultar contraseña (sin cambios)
    const eyeOpenIcon = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.477 0 8.268 2.943 9.542 7-1.274 4.057-5.065 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>`;
    const eyeClosedIcon = `<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.477 0-8.268-2.943-9.542-7a9.96 9.96 0 012.22-3.479m1.504-1.504A9.969 9.969 0 0112 5c4.477 0 8.268 2.943 9.542 7"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3l18 18"/></svg>`;
    togglePwd.innerHTML = eyeOpenIcon;
    togglePwd.addEventListener('click', () => {
        if (pwdField.type === 'password') {
            pwdField.type = 'text';
            togglePwd.innerHTML = eyeClosedIcon;
        } else {
            pwdField.type = 'password';
            togglePwd.innerHTML = eyeOpenIcon;
        }
    });

    // Requisitos de contraseña (sin cambios)
    const confirmDiv = confirmPwd.closest('div');
    const reqList = document.createElement('ul');
    reqList.id = 'pwd-requirements';
    reqList.className = 'mt-2 space-y-1 text-sm';
    reqList.innerHTML = [
        { text: 'Mínimo 8 caracteres', id: 'req-length' },
        { text: 'Al menos una mayúscula', id: 'req-uppercase' },
        { text: 'Al menos un número', id: 'req-number' },
        { text: `Al menos un carácter especial (${allowedSpecials})`, id: 'req-special' },
        { text: 'Las contraseñas coinciden', id: 'req-match' }
    ].map(req => `<li id="${req.id}" class="flex items-center text-red-500"><span class="mr-2">❌</span>${req.text}</li>`).join('');
    confirmDiv.appendChild(reqList);

    // Mensaje de advertencia (sin cambios)
    const footerActions = form.querySelector('.shadow-inner');
    const warningMsg = document.createElement('div');
    warningMsg.id = 'warning-msg';
    warningMsg.className = 'hidden text-red-500 flex items-center space-x-2 mt-2';
    warningMsg.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.29 3.86L1.82 18a1 1 0 00.86 1.5h18.64a1 1 0 00.86-1.5L13.71 3.86a1 1 0 00-1.72 0zM12 9v4m0 4h.01"/></svg><span id="warning-text"></span>`;
    footerActions.insertBefore(warningMsg, nextBtn);

    // --- Funciones de validación (la lógica interna se mantiene) ---
    function validateRequired() {
        const invalid = [];
        document.getElementById(`step-${currentStep}`).querySelectorAll('input[required], select[required]').forEach(f => {
            if (!f.value.trim()) {
                invalid.push(f);
                f.classList.add('border-2', 'border-red-500');
            } else {
                f.classList.remove('border-2', 'border-red-500');
            }
        });
        return invalid;
    }

    function validatePasswords() { /* ...lógica sin cambios... */
        const pw = pwdField.value;
        const cp = confirmPwd.value;
        const specialRegex = new RegExp(`[${allowedSpecials.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}]`);
        const checks = {
            length: pw.length >= 8,
            uppercase: /[A-Z]/.test(pw),
            number: /[0-9]/.test(pw),
            special: specialRegex.test(pw),
            match: pw === cp && pw !== ''
        };
        for (const [key, ok] of Object.entries(checks)) {
            const li = document.getElementById(`req-${key}`);
            li.querySelector('span').textContent = ok ? '✔️' : '❌';
            li.classList.toggle('text-green-600', ok);
            li.classList.toggle('text-red-500', !ok);
        }
        return Object.values(checks).every(Boolean);
    }
    [pwdField, confirmPwd].forEach(f => f.addEventListener('input', validatePasswords));

    function validateEmail() { /* ...lógica sin cambios... */
        const email = correoField.value.trim();
        const basicRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!basicRegex.test(email)) return false;
        const domain = email.split('@')[1].toLowerCase();
        return allowedEmailDomains.includes(domain);
    }

    // --- Lógica del Asistente (Wizard) ---
    function updateWizard() {
        // Actualiza el botón 'Siguiente' para que en el último paso diga 'Registrar Empleado'
        nextBtn.textContent = (currentStep < TOTAL_STEPS) ? 'Siguiente' : 'Registrar Empleado';
        
        // El botón 'Atrás' solo es visible a partir del paso 2
        prevBtn.classList.toggle('invisible', currentStep === 1);
        prevBtn.disabled = (currentStep === 1);

        // Actualiza la barra de progreso y los textos para 2 pasos
        if (currentStep === 1) {
            stepLabel.textContent = `Paso 1 de ${TOTAL_STEPS}`;
            stepTitle.textContent = 'Datos de la cuenta del empleado';
            progress.style.width = '50%';
        } else if (currentStep === 2) {
            stepLabel.textContent = `Paso 2 de ${TOTAL_STEPS}`;
            stepTitle.textContent = 'Información personal y de contacto';
            progress.style.width = '100%';
        }

        step1.classList.toggle('hidden', currentStep !== 1);
        step2.classList.toggle('hidden', currentStep !== 2);
        // Oculta siempre cualquier mensaje de advertencia al cambiar de paso
        warningMsg.classList.add('hidden');
    }

    prevBtn.addEventListener('click', () => {
        currentStep = Math.max(1, currentStep - 1);
        updateWizard();
    });

    nextBtn.addEventListener('click', e => {
        e.preventDefault();

        // 1. Validar campos requeridos del paso actual
        const invalidFields = validateRequired();
        if (invalidFields.length > 0) {
            warningMsg.querySelector('#warning-text').textContent = 'Debes rellenar los campos obligatorios.';
            warningMsg.classList.remove('hidden');
            invalidFields[0].focus();
            return;
        }

        // 2. Validaciones específicas del Paso 1 (email y contraseñas)
        if (currentStep === 1) {
            if (!validateEmail()) {
                correoField.classList.add('border-2', 'border-red-500');
                warningMsg.querySelector('#warning-text').textContent = 'La dirección de correo no es válida.';
                warningMsg.classList.remove('hidden');
                correoField.focus();
                return;
            }
            if (!validatePasswords()) {
                warningMsg.querySelector('#warning-text').textContent = 'La contraseña no cumple los requisitos.';
                warningMsg.classList.remove('hidden');
                pwdField.focus();
                return;
            }
        }

        // 3. Si no es el último paso, avanza
        if (currentStep < TOTAL_STEPS) {
            currentStep++;
            updateWizard();
            return;
        }

        // 4. Si es el último paso, enviar el formulario
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());
        // Ya no se envían preferencias ni términos

        console.log("Enviando datos del empleado:", data);

        fetch(form.action, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(async response => {
            if (!response.ok) {
                // Intenta leer el mensaje de error del servidor
                const errorData = await response.json().catch(() => ({ message: 'Error desconocido en el servidor.' }));
                throw new Error(errorData.message);
            }
            return response.json();
        })
        .then(result => {
            console.log("Respuesta exitosa:", result);
            alert('¡Empleado registrado con éxito!');
            // Redirige a la página de login o a donde indique el formulario
            window.location.href = form.dataset.successRedirect || `${window.contextPath}/auth/login`;
        })
        .catch(error => {
            console.error('Error en el registro:', error);
            alert(`Error al registrar el empleado: ${error.message}`);
        });
    });

    // Inicializar el asistente en el primer paso
    updateWizard();
});