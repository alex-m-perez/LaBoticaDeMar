document.addEventListener('DOMContentLoaded', () => {

    let cart = JSON.parse(localStorage.getItem('cart') || '{}');
    function saveCart() {
        localStorage.setItem('cart', JSON.stringify(cart));
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    }

    let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
    function saveWishlist() {
        localStorage.setItem('wishlist', JSON.stringify(wishlist));
        window.dispatchEvent(new CustomEvent('wishlistUpdated'));
    }

    function renderCartControls(container, product) {
        container.innerHTML = '';
        const qty = cart[product.id] || 0;

        if (qty === 0) {
            const btn = document.createElement('button');

            if (product.stock === 0) {
                btn.disabled = true;
                btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
                btn.textContent = 'Añadir'; // El botón deshabilitado no tendrá icono
            } else {
                btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-pistachio-dark transition flex items-center gap-1';
                
                const imgIcon = document.createElement('img');
                imgIcon.src = "/images/shopping-cart-white.svg";
                imgIcon.className = 'h-5 w-5';
                imgIcon.alt = 'Añadir al carrito';

                btn.appendChild(imgIcon);

                btn.addEventListener('click', () => {
                    cart[product.id] = 1;
                    saveCart();
                    renderCartControls(container, product);
                });
            }
            container.appendChild(btn);
        } else {
            const wrap = document.createElement('div');
            wrap.className = 'flex items-center border border-gray-200 rounded';

            const btnMinus = document.createElement('button');
            btnMinus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
            btnMinus.textContent = '−';
            btnMinus.addEventListener('click', () => {
                const newQty = (cart[product.id] || 0) - 1;
                if (newQty > 0) {
                    cart[product.id] = newQty;
                } else {
                    delete cart[product.id];
                }
                saveCart();
                renderCartControls(container, product);
            });

            const input = document.createElement('input');
            input.type = 'text';
            input.value = qty;
            input.className = 'w-7 text-center text-sm py-1 border-0 focus:ring-0 focus:outline-none';
            input.readOnly = true;

            const btnPlus = document.createElement('button');
            btnPlus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
            btnPlus.textContent = '+';
            if (qty >= product.stock) {
               btnPlus.disabled = true;
               btnPlus.classList.add("cursor-not-allowed", "text-gray-300");
            }
            btnPlus.addEventListener('click', () => {
                cart[product.id] = (cart[product.id] || 0) + 1;
                saveCart();
                renderCartControls(container, product);
            });

            wrap.appendChild(btnMinus);
            wrap.appendChild(input);
            wrap.appendChild(btnPlus);
            container.appendChild(wrap);
        }
    }
    
    function renderWishlistStatus(button, productId) {
        const svg = button.querySelector('svg');
        if (wishlist.includes(productId.toString())) {
            svg.setAttribute('fill', 'currentColor');
            svg.classList.remove('text-gray-400');
            svg.classList.add('text-red-500');
        } else {
            svg.setAttribute('fill', 'none');
            svg.classList.remove('text-red-500');
            svg.classList.add('text-gray-400');
        }
    }
    
    const productCards = document.querySelectorAll('.product-card');

    productCards.forEach(card => {
        const productData = JSON.parse(card.dataset.product);
        const cartContainer = card.querySelector('.cart-controls-container');
        const wishlistBtn = card.querySelector('.wishlist-btn');

        if (cartContainer) {
            renderCartControls(cartContainer, productData);
        }

        if (wishlistBtn) {
            renderWishlistStatus(wishlistBtn, productData.id);
            
            wishlistBtn.addEventListener('click', () => {
                const productIdStr = productData.id.toString();
                const index = wishlist.indexOf(productIdStr);

                if (index > -1) {
                    wishlist.splice(index, 1);
                } else {
                    wishlist.push(productIdStr);
                }
                saveWishlist();
                renderWishlistStatus(wishlistBtn, productData.id);
            });
        }
    });
});