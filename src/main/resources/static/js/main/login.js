async function login(event) {
    event.preventDefault();

    // --- ESTA ES LA PARTE QUE TE FALTA ---
    // Lee el formulario y convierte sus datos en un objeto.
    const miForm = document.querySelector('form');
    const formData = new FormData(miForm);
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });
    // ------------------------------------

    try {
        // Ahora 'formObject' sí existe y se puede usar aquí.
        const response = await axios.post('/auth/authenticate', formObject, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        console.log('Autenticación exitosa:', response.data);

        await mergeGuestCartOnLogin();

        window.location.href = "/";
        
    } catch (error) {
        console.error('Error:', error.response ? error.response.data : error.message);
        alert('Dirección de correo o contraseña incorrectos.');
    }
}


// Nueva función de fusión usando axios
async function mergeGuestCartOnLogin() {
    debugger
    const GUEST_CART_KEY = 'cart';
    const guestCart = JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');
    const contextPath = window.contextPath || '';

    if (Object.keys(guestCart).length === 0) {
        return; 
    }

    try {
        await axios.post(`${contextPath}/api/cart/merge`, guestCart, {
            headers: { 'Content-Type': 'application/json' }
        });
        
        console.log("Carrito de invitado fusionado con éxito.");
        localStorage.removeItem(GUEST_CART_KEY);
        window.dispatchEvent(new CustomEvent('cartUpdated'));

    } catch (err) {
        console.error("Error al fusionar el carrito:", err.response ? err.response.data : err.message);
    }
}