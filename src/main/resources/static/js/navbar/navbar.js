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

function toggleMenu(menuToShow, menuToHide, arrowDownToShow, arrowUpToShow, arrowDownToHide, arrowUpToHide, visibilityFlagToShow, visibilityFlagToHide) {
    // Si el menú que se intenta mostrar está abierto, lo cerramos
    if (visibilityFlagToShow) {
        menuToShow.classList.add('hidden');
        menuToShow.classList.remove('block');
        arrowDownToShow.classList.remove('hidden');
        arrowUpToShow.classList.add('hidden');
        visibilityFlagToShow = false;
    } else {
        // Mostramos el menú deseado
        menuToShow.classList.remove('hidden');
        menuToShow.classList.add('block');
        arrowDownToShow.classList.add('hidden');
        arrowUpToShow.classList.remove('hidden');
        visibilityFlagToShow = true;

        // Ocultamos el otro menú si está visible
        if (visibilityFlagToHide) {
            menuToHide.classList.add('hidden');
            menuToHide.classList.remove('block');
            arrowDownToHide.classList.remove('hidden');
            arrowUpToHide.classList.add('hidden');
            visibilityFlagToHide = false;
        }
    }

    // Actualizamos las variables globales directamente
    isCategoriesVisible = (menuToShow === categoriesList) ? visibilityFlagToShow : visibilityFlagToHide;
    isBrandsVisible = (menuToShow === brandsList) ? visibilityFlagToShow : visibilityFlagToHide;
}

// Evento de clic en Categorías
toggleCategoriesBtn.addEventListener('click', function() {
    toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, isCategoriesVisible, isBrandsVisible);
});

// Evento de clic en Marcas
toggleBrandsBtn.addEventListener('click', function() {
    toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, isBrandsVisible, isCategoriesVisible);
});


const alphabetContainer = document.getElementById('alphabetButtons');
const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

alphabet.forEach(letter => {
    const button = document.createElement('button');
    button.textContent = letter;
    button.classList.add('px-3', 'py-1', 'bg-gray-200', 'hover:bg-gray-300', 'text-gray-800', 'font-bold', 'rounded');
    alphabetContainer.appendChild(button);
});