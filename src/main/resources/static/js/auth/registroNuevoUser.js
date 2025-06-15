document.addEventListener('DOMContentLoaded', () => {
    const form        = document.getElementById('register-form');
    const prevBtn     = document.getElementById('prev-btn');
    const nextBtn     = document.getElementById('next-btn');
    const progress    = document.getElementById('progress-bar');
    const stepLabel   = document.getElementById('step-label');
    const stepTitle   = document.getElementById('step-title');
    const step1       = document.getElementById('step-1');
    const step2       = document.getElementById('step-2');
    const step3       = document.getElementById('step-3');
    const pwdField    = document.getElementById('passwd');
    const confirmPwd  = document.getElementById('confirm_passwd');
    const togglePwd   = document.getElementById('toggle-password');
    const correoField = document.getElementById('correo');

    // Lista de caracteres especiales permitidos
    const allowedSpecials = '!@#$%^&*()+-=[]{};,<>/?|~';
    // Lista de dominios de correo válidos en España
    const allowedEmailDomains = [
        // Internacionales
        'gmail.com', 'hotmail.com', 'outlook.com', 'live.com', 'icloud.com',
        'yahoo.com', 'yahoo.es', 'protonmail.com', 'gmx.es', 'msn.com',
        'hotmail.es', 'outlook.es', 'live.es',

        // Nacionales
        'telefonica.net', 'terra.es', 'ono.com', 'jazztel.com', 'wanadoo.es',
        'ya.com', 'orange.es', 'movistar.es', 'vodafone.es'
    ];


    let currentStep = 1;
    const selectedIntereses = new Set();

    // Quitar borde rojo al rellenar dinámicamente
    document.querySelectorAll('#register-form input[required], #register-form select[required]').forEach(field => {
        field.addEventListener('input', () => {
            if (field.type === 'checkbox') return;
            if (field.value.trim()) {
                field.classList.remove('border-2','border-red-500');
            }
        });
        field.addEventListener('change', () => {
            if (field.type === 'checkbox') {
                if (field.checked) {
                    field.classList.remove('border-2','border-red-500');
                }
            } else if (field.value.trim()) {
                field.classList.remove('border-2','border-red-500');
            }
        });
    });

    // Deshabilitar placeholder en select de género tras selección
    const generoSelect = document.getElementById('genero');
    if (generoSelect) {
        generoSelect.addEventListener('change', () => {
            const placeholder = generoSelect.querySelector('option[value=""]');
            if (generoSelect.value && placeholder) {
                placeholder.disabled = true;
            }
        });
    }

    // Íconos ojo contraseña
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

    // Insertar lista de requisitos de contraseña bajo confirmación
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

    // Selección de intereses: borde verde y envío de IDs seleccionados
    const interesEls = document.querySelectorAll('.select-interes');
    interesEls.forEach(el => {
        el.addEventListener('click', () => {
            const id = parseInt(el.dataset.id, 10);
            if (selectedIntereses.has(id)) {
                selectedIntereses.delete(id);
                el.classList.remove('border-2','border-pistachio');
                el.classList.add('border','border-gray-300');
            } else {
                selectedIntereses.add(id);
                el.classList.remove('border','border-gray-300');
                el.classList.add('border-2','border-pistachio');
            }
        });
    });


    // Mensaje de advertencia (requeridos, email o contraseñas)
    const footerActions = form.querySelector('.shadow-inner');
    const warningMsg = document.createElement('div');
    warningMsg.id = 'warning-msg';
    warningMsg.className = 'hidden text-red-500 flex items-center space-x-2 mt-2';
    warningMsg.innerHTML = `<svg xmlns=\"http://www.w3.org/2000/svg\" class=\"h-5 w-5\" fill=\"none\" viewBox=\"0 0 24 24\" stroke=\"currentColor\"><path stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"2\" d=\"M10.29 3.86L1.82 18a1 1 0 00.86 1.5h18.64a1 1 0 00.86-1.5L13.71 3.86a1 1 0 00-1.72 0zM12 9v4m0 4h.01\"/></svg><span id=\"warning-text\"></span>`;
    footerActions.insertBefore(warningMsg, nextBtn);

    // Validar campos required en paso actual
    function validateRequired() {
        const invalid = [];
        document.getElementById(`step-${currentStep}`).querySelectorAll('input[required], select[required]').forEach(f => {
            const empty = f.type === 'checkbox' ? !f.checked : !f.value.trim();
            if (empty) {
                invalid.push(f);
                if (f.type === 'checkbox') {
                    f.classList.add('ring-2','ring-red-500','rounded');
                } else {
                    f.classList.add('border-2','border-red-500');
                }
            } else {
                if (f.type === 'checkbox') {
                    f.classList.remove('ring-2','ring-red-500','rounded');
                } else {
                    f.classList.remove('border-2','border-red-500');
                }
            }
        });
        return invalid;
    }

    // Validar fuerza y coincidencia de contraseñas
    function validatePasswords() {
        const pw = pwdField.value;
        const cp = confirmPwd.value;
        const specialRegex = /[!@#$%^&*()+\-=[\]{};,<>\/\?\|~]/;
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

    // Validar formato y dominio de correo electrónico
    function validateEmail() {
        const email = correoField.value.trim();
        // Formato básico
        const basicRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!basicRegex.test(email)) return false;
        // Comprobar dominio
        const domain = email.split('@')[1].toLowerCase();
        return allowedEmailDomains.includes(domain);
    }

    // Al cambiar cualquiera de las dos contraseñas, actualizar lista
    [pwdField, confirmPwd].forEach(f => f.addEventListener('input', validatePasswords));

    function updateWizard() {
        nextBtn.textContent = (currentStep < 3) ? 'Siguiente' : 'Terminar';
        prevBtn.classList.toggle('invisible', currentStep === 1);
        prevBtn.disabled = (currentStep === 1);
        if (currentStep === 1) {
            stepTitle.textContent = 'Consigue tu cuenta ahora'; stepLabel.textContent = 'Paso 1 de 3'; progress.style.width = '33%';
        } else if (currentStep === 2) {
            stepTitle.textContent = 'Ya casi está...'; stepLabel.textContent = 'Paso 2 de 3'; progress.style.width = '66%';
        } else {
            stepTitle.textContent = 'Una última pregunta'; stepLabel.textContent = 'Paso 3 de 3'; progress.style.width = '100%';
        }
        step1.classList.toggle('hidden', currentStep!==1);
        step2.classList.toggle('hidden', currentStep!==2);
        step3.classList.toggle('hidden', currentStep!==3);
        warningMsg.classList.add('hidden');
    }

    prevBtn.addEventListener('click', () => {
        currentStep = Math.max(1, currentStep-1);
        updateWizard();
    });

    nextBtn.addEventListener('click', e => {
        e.preventDefault();
        // 1) Validar required
        const inv = validateRequired();
        if (inv.length) {
            warningMsg.querySelector('#warning-text').textContent = 'Se deben rellenar los campos obligatorios';
            warningMsg.classList.remove('hidden');
            inv[0].scrollIntoView({behavior:'smooth', block:'center'});
            return;
        }
        // 2) Si estamos en paso 1, validar correo y contraseñas
        if (currentStep===1) {
            // Validar email
            if (!validateEmail()) {
                correoField.classList.add('border-2','border-red-500');
                warningMsg.querySelector('#warning-text').textContent = 'Dirección de correo no válida';
                warningMsg.classList.remove('hidden');
                correoField.scrollIntoView({behavior:'smooth', block:'center'});
                return;
            }
            // Validar contraseñas
            const ok = validatePasswords();
            if (!ok) {
                warningMsg.querySelector('#warning-text').textContent = 'Las contraseñas deben cumplir los requisitos';
                warningMsg.classList.remove('hidden');
                document.getElementById('pwd-requirements').scrollIntoView({behavior:'smooth', block:'center'});
                return;
            }
        }
        // 3) Avanzar paso
        if (currentStep<3) {
            currentStep++;
            updateWizard();
            return;
        }
        // 4) Envío final
        const data = Object.fromEntries(new FormData(form));
        data.aceptaPromociones = form.querySelector('#aceptaPromociones').checked;
        data.aceptaTerminos    = form.querySelector('#aceptaTerminos').checked;
        data.aceptaPrivacidad  = form.querySelector('#aceptaPrivacidad').checked;
        data.preferencias      = Array.from(selectedIntereses);
        fetch(form.action, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(async r => {
            if (!r.ok) throw new Error();
            const res = await r.json();
            localStorage.setItem('token', res.token); // guarda el token
            window.location = '/'; // redirige a home
        })
        .catch(() => {
            alert('Error al registrarse, por favor inténtalo de nuevo.');
        });
    });

    // Inicializar wizard
    updateWizard();
});
