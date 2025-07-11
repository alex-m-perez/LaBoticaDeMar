//navbar.js
document.addEventListener('DOMContentLoaded', () => {
    // ——————————————————————————————
    // CONSTANTES Y SELECTORES (Sin cambios)
    // ——————————————————————————————
    const toggleCategoriesBtn = document.getElementById('toggleCategoriesBtn');
    const toggleBrandsBtn = document.getElementById('toggleBrandsBtn');
    const categoriesList = document.getElementById('categoriesList');
    const brandsList = document.getElementById('brandsList');
    const categoriesArrowDown = document.getElementById('categories_arrowDown');
    const categoriesArrowUp = document.getElementById('categories_arrowUp');
    const brandsArrowDown = document.getElementById('brands_arrowDown');
    const brandsArrowUp = document.getElementById('brands_arrowUp');
    const navbarItems = document.querySelectorAll('.navbar-item');
    const familyItems = document.querySelectorAll('.family-item');
    const familyCategoriesList = document.getElementById('familyCategoriesList');
    const alphabetContainer = document.getElementById('alphabetButtons');
    const navbarInput = document.getElementById('navbarSearchInput');
    const navbarBox = document.getElementById('navbarSuggestions');
    const profileContainer = document.getElementById('profileMenuContainer');
    const profileIcon = document.getElementById('profileIcon');
    const profileMenu = document.getElementById('profileMenu');
    const brandsByLetterContainer = document.getElementById('brandsByLetterContainer');
    const laboratoriosMap = laboratoriosAgrupados || {};
    const alphabetButtons = [];
    const activeClasses = ['bg-pistachio', 'text-white'];
    const inactiveClasses = ['bg-gray-200', 'text-gray-800', 'hover:bg-pistachio', 'hover:text-white'];
    const isAuthenticated = document.body.dataset.authenticated === 'true';

    let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    let profileMenuTimer = null; 
    let isCategoriesVisible = false;
    let isBrandsVisible = false;
    let navbarTimer;
    let menuCloseTimer = null;

    // ——————————————————————————————
    // TOOGLE CATEGORIAS Y MENÚS PRINCIPALES
    // ——————————————————————————————
    function clearSelection() {
        navbarItems.forEach(el => {
            el.classList.remove("text-red-600", "hover:text-gray-800", "hover:text-red-800");
            if (el.id === "toggleBrandsBtn" || el.id === "toggleCategoriesBtn") {
                el.classList.add("text-gray-500", "hover:text-gray-800");
            } else {
                el.classList.add("text-red-500", "hover:text-red-800");
            }
        });
    }

    function setActive(element) {
        element.classList.remove("text-gray-500", "hover:text-gray-800", "text-red-500", "hover:text-red-800");
        if (element.classList.contains("text-red-500")) element.classList.add("text-red-600");
    }

    function startMenuCloseTimer() {
        clearTimeout(menuCloseTimer);
        menuCloseTimer = setTimeout(() => {
            if (isCategoriesVisible) {
                toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, toggleCategoriesBtn, true);
            } else if (isBrandsVisible) {
                toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, toggleBrandsBtn, true);
            }
        }, 6000);
    }

    function resetMenuCloseTimer() {
        clearTimeout(menuCloseTimer);
    }


    // --- FUNCIÓN `toggleMenu` FINAL Y CORREGIDA ---
    function toggleMenu(menuToShow, menuToHide, arrowDownToShow, arrowUpToShow, arrowDownToHide, arrowUpToHide, btnToActivate, shouldClose) {
        resetMenuCloseTimer();

        // 1. Cierra siempre el OTRO menú.
        menuToHide.classList.add('max-h-0', 'opacity-0');
        // Le quitamos 'is-open' para asegurar que su línea también se oculte.
        menuToHide.classList.remove('pt-4', 'mt-4', 'is-open', 'max-h-[500px]');
        arrowDownToHide.classList.remove('hidden');
        arrowUpToHide.classList.add('hidden');

        // 2. Gestiona el menú sobre el que se hizo clic.
        if (shouldClose) {
            // LÓGICA DE CIERRE: Colapsa y oculta el menú.
            menuToShow.classList.add('max-h-0', 'opacity-0');
            // Quitamos las clases de layout y la clase 'is-open' para ocultar la línea.
            menuToShow.classList.remove('pt-4', 'mt-4', 'is-open', 'max-h-[500px]');

            arrowDownToShow.classList.remove('hidden');
            arrowUpToShow.classList.add('hidden');
            clearSelection();
        } else {
            // LÓGICA DE APERTURA: Expande y muestra el menú.
            menuToShow.classList.remove('max-h-0', 'opacity-0');
            // Añadimos las clases de layout y 'is-open' para que la línea aparezca suavemente.
            menuToShow.classList.add('pt-4', 'mt-4', 'is-open', 'max-h-[500px]');

            arrowDownToShow.classList.add('hidden');
            arrowUpToShow.classList.remove('hidden');

            clearSelection();
            setActive(btnToActivate);
            startMenuCloseTimer();
        }

        // 3. Actualiza los flags de estado al final de la operación.
        isCategoriesVisible = (menuToShow === categoriesList) && !shouldClose;
        isBrandsVisible = (menuToShow === brandsList) && !shouldClose;

        // 4. Limpia el contenido de las marcas (sin cambios)
        if (shouldClose && menuToShow === brandsList && brandsByLetterContainer) {
            brandsByLetterContainer.innerHTML = '';
            alphabetButtons.forEach(btn => {
                btn.classList.remove(...activeClasses);
                btn.classList.add(...inactiveClasses);
            });
        }
    }

    toggleCategoriesBtn.addEventListener('click', () => {
        toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, toggleCategoriesBtn, isCategoriesVisible);
    });

    toggleBrandsBtn.addEventListener('click', () => {
        toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, toggleBrandsBtn, isBrandsVisible);
    });

    if (categoriesList) {
        categoriesList.addEventListener('mouseenter', resetMenuCloseTimer);
        categoriesList.addEventListener('mouseleave', startMenuCloseTimer);
    }

    if (brandsList) {
        brandsList.addEventListener('mouseenter', resetMenuCloseTimer);
        brandsList.addEventListener('mouseleave', startMenuCloseTimer);
    }


    navbarItems.forEach(item => {
        if (item.id !== "toggleCategoriesBtn" && item.id !== "toggleBrandsBtn") {
            item.addEventListener('click', () => {
                // Al hacer clic en otro item, nos aseguramos de que los menús se cierren
                if(isCategoriesVisible) toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, toggleCategoriesBtn, true);
                if(isBrandsVisible) toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, toggleBrandsBtn, true);

                isCategoriesVisible = false;
                isBrandsVisible = false;
                clearSelection();
                setActive(item);
            });
        }
    });

    familyItems.forEach(item => {
        item.addEventListener('click', () => {
            const isActive = item.classList.contains('bg-gray-200');
            familyItems.forEach(fi => fi.classList.remove('bg-gray-200'));
            familyCategoriesList.innerHTML = '';
            if (!isActive) {
                item.classList.add('bg-gray-200');
                const hiddenSublist = item.querySelector('ul.hidden');
                if (hiddenSublist) {
                    Array.from(hiddenSublist.children).forEach(catLi => {
                        familyCategoriesList.appendChild(catLi.cloneNode(true));
                    });
                }
            }
        });
    });

    // ——————————————————————————————
    // FUNCIONALIDAD DE MARCAS (Laboratorios)
    // ——————————————————————————————

    if (alphabetContainer && Object.keys(laboratoriosMap).length > 0) {
        const availableLetters = Object.keys(laboratoriosMap);

        availableLetters.forEach(letter => {
            const button = document.createElement('button');
            button.textContent = letter;
            button.dataset.letter = letter;

            // Añadimos las clases de estilo base y las clases del estado inactivo.
            button.className = 'px-3 py-1 font-bold rounded transition-colors';
            button.classList.add(...inactiveClasses);

            // Evento para mostrar las marcas y gestionar el estado activo.
            button.addEventListener('mouseover', (event) => {
                // 1. Quitar la clase activa de TODOS los botones.
                alphabetButtons.forEach(btn => {
                    btn.classList.remove(...activeClasses);
                    btn.classList.add(...inactiveClasses);
                });

                // 2. Añadir la clase activa SOLO al botón actual.
                const currentButton = event.currentTarget;
                currentButton.classList.remove(...inactiveClasses);
                currentButton.classList.add(...activeClasses);

                // 3. Mostrar las marcas correspondientes (esta lógica se mantiene).
                if (!brandsByLetterContainer) return;
                brandsByLetterContainer.innerHTML = ''; 

                const brands = laboratoriosMap[letter];
                if (brands) {
                    brands.forEach(brand => {
                        const link = document.createElement('a');
                        link.href = `${window.contextPath}/product?laboratorio=${brand.id}`;
                        link.textContent = brand.nombre;
                        link.className = 'text-sm text-gray-700 hover:text-pistachio hover:font-semibold whitespace-nowrap';
                        brandsByLetterContainer.appendChild(link);
                    });
                }
            });

            alphabetContainer.appendChild(button);
            alphabetButtons.push(button); 
        });

    }

    // ——————————————————————————————
    // AUTCOMPLETAR NAVBAR
    // ——————————————————————————————
    if (navbarInput) {
        navbarInput.addEventListener('input', e => {
            const q = e.target.value.trim();
            clearTimeout(navbarTimer);
            if (q.length < 3) {
                if (navbarBox) {
                    navbarBox.innerHTML = '';
                    navbarBox.classList.add('hidden');
                }
                return;
            }
            navbarTimer = setTimeout(() => {
                fetch(`${window.contextPath}/api/product/search_names?q=${encodeURIComponent(q)}`)
                    .then(res => {
                        if (!res.ok) throw new Error(`Status ${res.status}`);
                        return res.json();
                    })
                    .then(list => {
                        if (list.length === 0) {
                            navbarBox.classList.add('hidden');
                            return;
                        }
                        navbarBox.innerHTML = list
                            .map(name => `<li class="px-4 py-2 hover:bg-gray-100 cursor-pointer">${name}</li>`)
                            .join('');
                        navbarBox.classList.remove('hidden');

                        navbarBox.querySelectorAll('li').forEach(li => {
                            li.addEventListener('mousedown', () => {
                                navbarInput.value = li.textContent;
                                navbarBox.classList.add('hidden');
                                li.closest('form').submit(); // Enviamos el formulario al seleccionar
                            });
                        });
                    })
                    .catch(err => {
                        console.error('Error autocomplete navbar:', err);
                        navbarBox.classList.add('hidden');
                    });
            }, 300);
        });

        navbarInput.addEventListener('blur', () => {
            setTimeout(() => {
                if(navbarBox) navbarBox.classList.add('hidden');
            }, 200);
        });
    }

    // ——————————————————————————————
    // MENU CONTEXTUAL PROFILE
    // ——————————————————————————————
    if (profileContainer && profileMenu) {
        const openMenu = () => {
            // Cancela cualquier temporizador de cierre pendiente
            clearTimeout(profileMenuTimer);
            // Muestra el menú
            profileMenu.classList.remove('hidden');
        };

        const startCloseTimer = () => {
            // Inicia un temporizador para cerrar el menú después de 1 segundo
            profileMenuTimer = setTimeout(() => {
                profileMenu.classList.add('hidden');
            }, 250); // 1000 ms = 1 segundo
        };

        // Eventos para el contenedor del icono
        profileContainer.addEventListener('mouseenter', openMenu);
        profileContainer.addEventListener('mouseleave', startCloseTimer);

        // Eventos para el menú desplegable
        profileMenu.addEventListener('mouseenter', openMenu);
        profileMenu.addEventListener('mouseleave', startCloseTimer);
    }

    if (profileIcon) {
        profileIcon.addEventListener('click', () => {
            window.location.href = `${window.contextPath}/profile`;
        });
    }
    
});

// ——————————————————————————————
// BURBUJA DE CANTIDAD DE ITEMS EN CARRITO
// ——————————————————————————————
// Reemplaza la función entera en navbar.js
function updateCartBubble(event) {
    const bubble = document.getElementById('cart-item-count');
    if (!bubble) return;

    let finalCartState = {};

    // Si la función fue llamada por nuestro evento personalizado, usamos el estado que nos envía.
    if (event && event.detail && event.detail.newState) {
        finalCartState = event.detail.newState;
    } else {
        // Si no (en la carga inicial de la página), intenta obtener el estado por sí misma.
        const isAuthenticated = document.body.dataset.authenticated === 'true';
        finalCartState = isAuthenticated 
            ? (window.userCartState || {}) 
            : JSON.parse(localStorage.getItem('cart') || '{}');
    }

    // CÁLCULO CORRECTO: Suma las cantidades de los productos, no solo cuenta las claves.
    const totalItems = Object.keys(finalCartState).length;

    // La lógica para mostrar/ocultar la burbuja no cambia
    if (totalItems > 0) {
        const displayText = totalItems > 99 ? '99+' : totalItems;
        if (bubble.textContent !== displayText.toString()) {
            bubble.textContent = displayText;
            bubble.classList.remove('bubble-pop');
            void bubble.offsetWidth;
            bubble.classList.add('bubble-pop');
        }
        bubble.classList.remove('hidden');
    } else {
        bubble.classList.add('hidden');
    }
}

// Los listeners se mantienen igual, ya que la lógica corregida está dentro de la función.
window.addEventListener('cartUpdated', updateCartBubble);
document.addEventListener('DOMContentLoaded', updateCartBubble);