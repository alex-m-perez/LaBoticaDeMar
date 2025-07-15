//filter-ui.js
// ==========================================================
// MÓDULO: LÓGICA DE LA INTERFAZ DE FILTROS (VERSIÓN FINAL)
// ==========================================================

// Se asume que 'jerarquiaFiltros' y 'urlParams' están definidos en el JSP 
// antes de que se cargue este script.
// const jerarquiaFiltros = JSON.parse('${jerarquiaFiltrosJson}');
// const urlParams = new URLSearchParams(window.location.search);

const FilterUI = {
    // --- PROPIEDADES ---
    containers: {},
    data: {
        jerarquia: [],
        todasCategorias: [],
        todasSubcategorias: []
    },

    // --- MÉTODO PRINCIPAL DE INICIALIZACIÓN ---
    init: function(jerarquiaData) {
        if (!jerarquiaData || jerarquiaData.length === 0) {
            console.error("Los datos de jerarquía de filtros están vacíos o no se han cargado.");
            return;
        }
        this.data.jerarquia = jerarquiaData;
        
        this.prepareDataLookups();
        this.setupDomContainers();
        this.populateInitialOptions();
        this.setupEventListeners();
        this.applyInitialFiltersFromUrl();
        this.setupPriceSlider();
    },

    // --- MÉTODOS DE CONFIGURACIÓN Y PREPARACIÓN ---

    prepareDataLookups: function() {
        this.data.todasCategorias = this.data.jerarquia.flatMap(fam => fam.categorias);
        this.data.todasSubcategorias = this.data.todasCategorias.flatMap(cat => cat.subcategorias);
    },

    setupDomContainers: function() {
        this.containers = {
            familia: { 
                selector: document.getElementById('familiaSelector'), 
                pills: document.getElementById('selectedFamiliesContainer'), 
                inputs: document.getElementById('familiaInputsContainer') },
            categoria: { 
                selector: document.getElementById('categoriaSelector'), 
                pills: document.getElementById('selectedCategoriesContainer'), 
                inputs: document.getElementById('categoriaInputsContainer') },
            subcategoria: { 
                selector: document.getElementById('subcategoriaSelector'), 
                pills: document.getElementById('selectedSubcategoriesContainer'), 
                inputs: document.getElementById('subcategoriaInputsContainer') },
            tipo: { 
                selector: document.getElementById('tipoSelector'),
                pills: document.getElementById('selectedTiposContainer'), 
                inputs: document.getElementById('tipoInputsContainer') },
            laboratorio: { 
                selector: document.getElementById('laboratorioSelector'), 
                pills: document.getElementById('selectedLaboratoriosContainer'), 
                inputs: document.getElementById('laboratorioInputsContainer') }
        };
    },
    
    populateInitialOptions: function() {
        this.populateSelect('familia', this.data.jerarquia, 'Añadir una familia...');
        this.populateSelect('categoria', this.data.todasCategorias, 'Añadir una categoría...');
        this.populateSelect('subcategoria', this.data.todasSubcategorias, 'Añadir una subcategoría...');
        // NOTA: Se asume que los selects de 'tipo' y 'laboratorio' se rellenan desde el JSP con JSTL,
        // ya que no forman parte de la jerarquía. El script les añadirá la funcionalidad de píldoras.
    },

    setupEventListeners: function() {
        Object.keys(this.containers).forEach(type => {
            const container = this.containers[type];
            if (container && container.selector) {
                container.selector.addEventListener('change', (event) => this.handleSelection(event, type));
            }
        });
    },

    // --- LÓGICA DE ACTUALIZACIÓN DE OPCIONES (FILTRADO DESCENDENTE) ---

    getActiveFilterIds: function(type) {
        const container = this.containers[type]?.inputs;
        return container ? Array.from(container.querySelectorAll('input')).map(input => parseInt(input.value, 10)) : [];
    },
    
    populateSelect: function(type, items, defaultText) {
        const selector = this.containers[type]?.selector;
        if (!selector) return;

        const currentValue = selector.value;
        selector.innerHTML = `<option value="">${defaultText}</option>`;
        
        items.forEach(item => {
            const option = new Option(item.nombre, item.id);
            option.dataset.nombre = item.nombre;
            selector.appendChild(option);
        });
        selector.value = currentValue;
    },

    updateCategoryOptions: function() {
        const activeFamilyIds = this.getActiveFilterIds('familia');
        let categoriasDisponibles = (activeFamilyIds.length === 0)
            ? this.data.todasCategorias
            : this.data.todasCategorias.filter(cat => activeFamilyIds.includes(cat.familiaId));
        
        this.populateSelect('categoria', categoriasDisponibles, 'Añadir una categoría...');
    },

    updateSubcategoryOptions: function() {
        const activeCategoryIds = this.getActiveFilterIds('categoria');
        const activeFamilyIds = this.getActiveFilterIds('familia');
        let subcategoriasDisponibles = [];

        if (activeCategoryIds.length > 0) {
            subcategoriasDisponibles = this.data.todasSubcategorias.filter(sub => activeCategoryIds.includes(sub.categoriaId));
        } else if (activeFamilyIds.length > 0) {
            const categoryIdsInFamily = this.data.todasCategorias
                .filter(cat => activeFamilyIds.includes(cat.familiaId))
                .map(cat => cat.id);
            subcategoriasDisponibles = this.data.todasSubcategorias.filter(sub => categoryIdsInFamily.includes(sub.categoriaId));
        } else {
            subcategoriasDisponibles = this.data.todasSubcategorias;
        }
        this.populateSelect('subcategoria', subcategoriasDisponibles, 'Añadir una subcategoría...');
    },

    updateAllDependentOptions: function() {
        this.updateCategoryOptions();
        this.updateSubcategoryOptions();
    },

    // --- MÉTODO PARA LIMPIAR TODOS LOS FILTROS ---
    clearAll: function() {
        // 1) Reset del formulario (selects, checkboxes, números, ranges)
        const form = document.getElementById('filterForm');
        form.reset();

        // 2) Eliminar todas las píldoras y hidden inputs
        Object.values(this.containers).forEach(({ pills, inputs }) => {
            if (pills)  pills.innerHTML = '';
            if (inputs) inputs.innerHTML = '';
        });

        // 3) Volver a poblar los selects jerárquicos
        this.populateInitialOptions();
        this.updateAllDependentOptions();

        // 4) Limpiar la querystring de la URL y recargar por AJAX
        history.replaceState(null, '', window.location.pathname);
        // loadPage(0) viene de products-grid.js
        if (typeof loadPage === 'function') {
            loadPage(0);
        }
        if (typeof loadPage === 'function') {
            window.scrollTo({ top: 0, behavior: 'smooth' });
            loadPage(0);
        }
    },


    // --- LÓGICA DE MANEJO DE PÍLDORAS ---

    addFilterPill: function(type, id, name) {
        const { pills, inputs } = this.containers[type];
        if (!pills || !inputs || this.getActiveFilterIds(type).includes(parseInt(id, 10))) return;

        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = type;
        hiddenInput.value = id;
        inputs.appendChild(hiddenInput);

        const pill = document.createElement('div');
        pill.className = 'flex items-center bg-super-light-pistachio text-green-800 text-sm font-medium px-3 py-1 rounded-full';
        pill.innerHTML = `<span>${name}</span><button type="button" class="ml-2 text-green-800 hover:text-green-900 focus:outline-none">&times;</button>`;
        pills.appendChild(pill);

        pill.querySelector('button').addEventListener('click', () => {
            pill.remove();
            hiddenInput.remove();
            if (['familia', 'categoria'].includes(type)) {
                this.updateAllDependentOptions();
            }
        });

        if (['familia', 'categoria'].includes(type)) {
            this.updateAllDependentOptions();
        }
    },

    handleSelection: function(event, type) {
        const selector = event.target;
        const selectedOption = selector.options[selector.selectedIndex];
        const id = selectedOption.value;
        const name = selectedOption.dataset.nombre;

        if (!id || !name) return;
        
        this.addFilterPill(type, id, name);
        selector.value = ''; // Resetea el select para permitir nuevas selecciones
    },
    
    applyInitialFiltersFromUrl: function() {
        const conDescuentoParam = urlParams.get('conDescuento');
        if (conDescuentoParam === 'true') {
            const descuentoCheckbox = document.querySelector('input[name="conDescuento"]');
            if (descuentoCheckbox) {
                descuentoCheckbox.checked = true;
            }
        }

        Object.keys(this.containers).forEach(type => {
            const idsFromUrl = urlParams.getAll(type);
            if (idsFromUrl.length === 0) return;

            idsFromUrl.forEach(id => {
                let item = null;
                if (type === 'familia') {
                    item = this.data.jerarquia.find(i => i.id == id);
                } else if (type === 'categoria') {
                    item = this.data.todasCategorias.find(i => i.id == id);
                } else if (type === 'subcategoria') {
                    item = this.data.todasSubcategorias.find(i => i.id == id);
                } else if (this.containers[type]?.selector) {
                    const option = this.containers[type].selector.querySelector(`option[value="${id}"]`);
                    if (option) {
                        item = { id: id, nombre: option.textContent };
                    }
                }
                if (item) {
                    this.addFilterPill(type, item.id, item.nombre);
                }
            });
        });
    },

    // --- LÓGICA DEL SLIDER DE PRECIOS (SIN CAMBIOS FUNCIONALES) ---
    setupPriceSlider: function() {
        const sliderMin = document.getElementById('slider-min');
        const sliderMax = document.getElementById('slider-max');
        const inputMin = document.querySelector('input[name="precioMin"]');
        const inputMax = document.querySelector('input[name="precioMax"]');
        const progress = document.querySelector('.slider-progress');
        const priceGap = 3;

        if (!sliderMin || !sliderMax || !inputMin || !inputMax || !progress) return;

        const updateProgress = () => {
            const minVal = parseInt(inputMin.value);
            const maxVal = parseInt(inputMax.value);
            progress.style.left = `${(minVal / sliderMin.max) * 100}%`;
            progress.style.width = `${((maxVal - minVal) / sliderMax.max) * 100}%`;
        };

        const handleSliderInput = (e) => {
            let minVal = parseInt(sliderMin.value);
            let maxVal = parseInt(sliderMax.value);
            if (maxVal - minVal < priceGap) {
                if (e.target === sliderMin) {
                    sliderMin.value = maxVal - priceGap;
                } else {
                    sliderMax.value = minVal + priceGap;
                }
            }
            inputMin.value = sliderMin.value;
            inputMax.value = sliderMax.value;
            updateProgress();
        };

        const handleTextInput = (e) => {
            let minVal = parseInt(inputMin.value);
            let maxVal = parseInt(inputMax.value);
            if ((maxVal - minVal >= priceGap) && maxVal <= sliderMax.max) {
                 if (e.target === inputMin) {
                    sliderMin.value = minVal;
                 } else {
                    sliderMax.value = maxVal;
                 }
            }
            updateProgress();
        };

        sliderMin.addEventListener('input', handleSliderInput);
        sliderMax.addEventListener('input', handleSliderInput);
        inputMin.addEventListener('input', handleTextInput);
        inputMax.addEventListener('input', handleTextInput);
        
        updateProgress();
    }
};

document.addEventListener('DOMContentLoaded', () => {
  if (typeof jerarquiaFiltros !== 'undefined') {
    FilterUI.init(jerarquiaFiltros);

    if (typeof loadPage === 'function') {
      loadPage(0);
    } else {
      console.error("La función loadPage() de products-grid.js no está disponible.");
    }

  } else {
      console.error("Error cargando el mapa de jerarquias para el filtrado de productos.");
  }

  const clearBtn = document.getElementById('clearFilters');
  if (clearBtn) {
    clearBtn.addEventListener('click', () => FilterUI.clearAll());
  }
});
