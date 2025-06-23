// ——————————————————————————————
// TOOGLE CATEGORIAS
// ——————————————————————————————
const toggleCategoriesBtn = document.getElementById('toggleCategoriesBtn');
const toggleBrandsBtn = document.getElementById('toggleBrandsBtn');

const categoriesList = document.getElementById('categoriesList');
const brandsList = document.getElementById('brandsList');

const categoriesArrowDown = document.getElementById('categories_arrowDown');
const categoriesArrowUp = document.getElementById('categories_arrowUp');
const brandsArrowDown = document.getElementById('brands_arrowDown');
const brandsArrowUp = document.getElementById('brands_arrowUp');

let isCategoriesVisible = false;
let isBrandsVisible = false;

// Utilidades
function clearSelection() {
    const navbarItems = document.querySelectorAll('.navbar-item');
    navbarItems.forEach(el => {
        el.classList.remove("text-red-600", "hover:text-gray-800", "hover:text-red-800");
        if (el.id === "toggleBrandsBtn" || el.id === "toggleCategoriesBtn" || el.classList.contains("text-gray-500")) {
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

// Menú desplegable
function toggleMenu(menuToShow, menuToHide, arrowDownToShow, arrowUpToShow, arrowDownToHide, arrowUpToHide, btnToActivate, btnToDeactivate, visibilityFlagToShow, visibilityFlagToHide) {
    const shouldClose = visibilityFlagToShow;

    if (!shouldClose) {
        menuToShow.classList.remove('hidden');
        menuToShow.classList.add('block');
        arrowDownToShow.classList.add('hidden');
        arrowUpToShow.classList.remove('hidden');

        menuToHide.classList.add('hidden');
        menuToHide.classList.remove('block');
        arrowDownToHide.classList.remove('hidden');
        arrowUpToHide.classList.add('hidden');

        clearSelection();
        setActive(btnToActivate);
    } else {
        menuToShow.classList.add('hidden');
        menuToShow.classList.remove('block');
        arrowDownToShow.classList.remove('hidden');
        arrowUpToShow.classList.add('hidden');

        clearSelection();
    }

    isCategoriesVisible = (menuToShow === categoriesList) ? !shouldClose : false;
    isBrandsVisible = (menuToShow === brandsList) ? !shouldClose : false;
}

// Clicks
toggleCategoriesBtn.addEventListener('click', function () {
    toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, toggleCategoriesBtn, toggleBrandsBtn, isCategoriesVisible, isBrandsVisible);
});

toggleBrandsBtn.addEventListener('click', function () {
    toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, toggleBrandsBtn, toggleCategoriesBtn, isBrandsVisible, isCategoriesVisible);
});

// Click en enlaces no desplegables
const navbarItems = document.querySelectorAll('.navbar-item');

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


const familyItems = document.querySelectorAll('.family-item');
const familyCategoriesList = document.getElementById('familyCategoriesList');

familyItems.forEach(item => {
    item.addEventListener('click', () => {
        const isActive = item.classList.contains('bg-gray-200');

        // 1) Limpiar estado de todas las Familias
        familyItems.forEach(fi => {
            fi.classList.remove('bg-gray-200');
        });
        // 2) Limpiar Contenedor de Categorías
        familyCategoriesList.innerHTML = '';

        // 3) Si antes no estaba activo, activarlo y mostrar sus Categorías
        if (!isActive) {
            item.classList.add('bg-gray-200');  // marca como activo
            const hiddenSublist = item.querySelector('ul.hidden');
            if (hiddenSublist) {
                // Clonar cada <li> de categoría y añadirlo al contenedor
                Array.from(hiddenSublist.children).forEach(catLi => {
                    familyCategoriesList.appendChild(catLi.cloneNode(true));
                });
            }
        }
    });
});




// ——————————————————————————————
// TOOGLE MARCAS
// ——————————————————————————————
const alphabetContainer = document.getElementById('alphabetButtons');
const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

alphabet.forEach(letter => {
    const button = document.createElement('button');
    button.textContent = letter;
    button.classList.add('px-3', 'py-1', 'bg-gray-200', 'hover:bg-gray-300', 'text-gray-800', 'font-bold', 'rounded');
    alphabetContainer.appendChild(button);
});



// ——————————————————————————————
// AUTCOMPLETAR NAVBAR
// ——————————————————————————————
const navbarInput   = document.getElementById('navbarSearchInput');
const navbarBox     = document.getElementById('navbarSuggestions');
let   navbarTimer;

navbarInput.addEventListener('input', e => {
	const q = e.target.value.trim();
	clearTimeout(navbarTimer);
	if (q.length < 3) {
		navbarBox.innerHTML = '';
		navbarBox.classList.add('hidden');
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
						// opcional: submit inmediato
						// li.parentElement.closest('form').submit();
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
	setTimeout(() => navbarBox.classList.add('hidden'), 200);
});



// ——————————————————————————————
// MENU CONTEXTUAL PROFILE
// ——————————————————————————————
const profileContainer = document.getElementById('profileMenuContainer');
const profileIcon      = document.getElementById('profileIcon');
const profileMenu      = document.getElementById('profileMenu');

// Sólo si existe menú (es decir, estás autenticado)
if (profileContainer && profileMenu) {
	profileContainer.addEventListener('mouseenter', () => {
		profileMenu.classList.remove('hidden');
	});
	profileContainer.addEventListener('mouseleave', () => {
		profileMenu.classList.add('hidden');
	});
}

if (profileIcon) {
	profileIcon.addEventListener('click', () => {
		window.location.href = `${window.contextPath}/profile`;
	});
}
