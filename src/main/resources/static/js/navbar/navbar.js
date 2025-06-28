document.addEventListener('DOMContentLoaded', () => {
    // ——————————————————————————————
    // CONSTANTES Y SELECTORES
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

    let isCategoriesVisible = false;
    let isBrandsVisible = false;
    let navbarTimer;

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
        // Nota: Este condicional puede ser redundante si clearSelection ya lo gestiona.
        if (element.classList.contains("text-red-500")) element.classList.add("text-red-600");
    }

    function toggleMenu(menuToShow, menuToHide, arrowDownToShow, arrowUpToShow, arrowDownToHide, arrowUpToHide, btnToActivate, visibilityFlagToShow) {
        const shouldClose = visibilityFlagToShow;
        if (!shouldClose) {
            menuToShow.classList.remove('hidden');
            arrowDownToShow.classList.add('hidden');
            arrowUpToShow.classList.remove('hidden');
            menuToHide.classList.add('hidden');
            arrowDownToHide.classList.remove('hidden');
            arrowUpToHide.classList.add('hidden');
            clearSelection();
            setActive(btnToActivate);
        } else {
            menuToShow.classList.add('hidden');
            arrowDownToShow.classList.remove('hidden');
            arrowUpToShow.classList.add('hidden');
            clearSelection();
        }
        isCategoriesVisible = (menuToShow === categoriesList) ? !shouldClose : false;
        isBrandsVisible = (menuToShow === brandsList) ? !shouldClose : false;
    }

    toggleCategoriesBtn.addEventListener('click', () => {
        toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, toggleCategoriesBtn, isCategoriesVisible);
    });

    toggleBrandsBtn.addEventListener('click', () => {
        toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, toggleBrandsBtn, isBrandsVisible);
    });

    navbarItems.forEach(item => {
        if (item.id !== "toggleCategoriesBtn" && item.id !== "toggleBrandsBtn") {
            item.addEventListener('click', () => {
                categoriesList.classList.add('hidden');
                brandsList.classList.add('hidden');
                categoriesArrowDown.classList.remove('hidden');
                categoriesArrowUp.classList.add('hidden');
                brandsArrowDown.classList.remove('hidden');
                brandsArrowUp.classList.add('hidden');
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

        const activeClasses = ['bg-pistachio', 'text-white'];
        const inactiveClasses = ['bg-gray-200', 'text-gray-800', 'hover:bg-pistachio', 'hover:text-white'];

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
            // Novedad: Guardamos el botón en nuestro array.
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
                fetch(`${window.contextPath}/admin/api/products/search_names?q=${encodeURIComponent(q)}`)
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
        profileContainer.addEventListener('mouseenter', () => profileMenu.classList.remove('hidden'));
        profileContainer.addEventListener('mouseleave', () => profileMenu.classList.add('hidden'));
    }

    if (profileIcon) {
        profileIcon.addEventListener('click', () => {
            window.location.href = `${window.contextPath}/profile`;
        });
    }
});