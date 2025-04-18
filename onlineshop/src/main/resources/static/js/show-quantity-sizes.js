function updateQuantityOptions(sizeSelect) {
    const cartItemInfo = sizeSelect.closest('.cart-item-info');
    const cartItemId = sizeSelect.id.split('-')[1];

    const selectedOption = sizeSelect.options[sizeSelect.selectedIndex];
    const availableQuantity = parseInt(selectedOption.dataset.quantity);

    const quantitySelect = document.getElementById(`quantity-${cartItemId}`);
    const preselectedQuantity = parseInt(cartItemInfo.dataset.selectedQuantity);

    quantitySelect.innerHTML = '';

    for (let i = 1; i <= availableQuantity; i++) {
        const option = document.createElement("option");
        option.value = i;
        option.text = i;
        if (i === preselectedQuantity) {
            option.selected = true;
        }
        quantitySelect.appendChild(option);
    }
}

function updatePrice(sizeSelect) {
    const cartItemInfo = sizeSelect.closest('.cart-item-info');
    const cartItemId = sizeSelect.id.split('-')[1];
    const pricePerUnit = parseFloat(cartItemInfo.dataset.price);

    const quantitySelect = document.getElementById(`quantity-${cartItemId}`);
    const selectedQuantity = parseInt(quantitySelect.value);

    const newPrice = (pricePerUnit * selectedQuantity).toFixed(2);
    document.getElementById(`price-${cartItemId}`).textContent = newPrice + ' лв.';
}

function updateTotalPrice() {
    let total = 0;
    document.querySelectorAll('.price .current').forEach(span => {
        const price = parseFloat(span.textContent.replace(' лв.', ''));
        total += price;
    });
    const totalPriceEl = document.querySelector('.total-price span');
    if (totalPriceEl) {
        totalPriceEl.textContent = total.toFixed(2) + ' лв.';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.size-select').forEach(sizeSelect => {
        updateQuantityOptions(sizeSelect);

        sizeSelect.addEventListener('change', function () {
            updateQuantityOptions(this);
            updatePrice(this);
            updateTotalPrice();
        });
    });

    document.querySelectorAll('.quantity-select').forEach(quantitySelect => {
        quantitySelect.addEventListener('change', function () {
            const sizeSelect = this.closest('.cart-item-info').querySelector('.size-select');
            updatePrice(sizeSelect);
            updateTotalPrice();
        });
    });

    updateTotalPrice(); // при първоначално зареждане
});