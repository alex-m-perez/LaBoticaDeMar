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
        el.classList.remove("text-pistachio", "text-red-600", "hover:text-gray-800", "hover:text-red-800");
        if (el.id === "toggleBrandsBtn" || el.id === "toggleCategoriesBtn" || el.classList.contains("text-gray-500")) {
            el.classList.add("text-gray-500", "hover:text-gray-800");
        } else {
            el.classList.add("text-red-500", "hover:text-red-800");
        }
    });
}

function setActive(element) {
    element.classList.remove("text-gray-500", "hover:text-gray-800", "text-red-500", "hover:text-red-800");
    if (element.classList.contains("text-red-500")) {
        element.classList.add("text-red-600");
    } else {
        element.classList.add("text-pistachio");
    }
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

// Generador alfabético (sin cambios)
const alphabetContainer = document.getElementById('alphabetButtons');
const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

alphabet.forEach(letter => {
    const button = document.createElement('button');
    button.textContent = letter;
    button.classList.add('px-3', 'py-1', 'bg-gray-200', 'hover:bg-gray-300', 'text-gray-800', 'font-bold', 'rounded');
    alphabetContainer.appendChild(button);
});

// Autocomplete en el buscador del navbar (solo productos activos)
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
		fetch(`${window.contextPath}/admin/api/products/search_names?q=${encodeURIComponent(q)}&active=true`)
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

