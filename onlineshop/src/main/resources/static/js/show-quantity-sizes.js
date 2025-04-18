function updateQuantityOptions(sizeSelect) {
    const cartItemId = sizeSelect.id.replace('size-', '');
    const selectedOption = sizeSelect.options[sizeSelect.selectedIndex];
    const availableQuantity = parseInt(selectedOption.dataset.quantity);

    const quantitySelect = document.getElementById(`quantity-${cartItemId}`);
    const preselectedQuantity = parseInt(sizeSelect.dataset.selectedQuantity);

    quantitySelect.innerHTML = ''; // Изчистваме старите стойности

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

function updateProductPrice(cartItemId) {
    const quantitySelect = document.getElementById(`quantity-${cartItemId}`);
    const quantity = parseInt(quantitySelect.value);
    const priceElement = document.getElementById(`price-${cartItemId}`);

    const unitPriceInput = document.getElementById(`unit-price-${cartItemId}`);
    const unitPrice = parseFloat(unitPriceInput.value);

    const newPrice = quantity * unitPrice;
    priceElement.textContent = newPrice.toFixed(2) + ' лв.';
}

function updateTotalPrice() {
    let total = 0;

    document.querySelectorAll('.cart-item .current').forEach(function (priceElement) {
        const priceText = priceElement.textContent;
        const priceValue = parseFloat(priceText.replace('лв.', '').trim());
        total += priceValue;
    });

    const totalSpan = document.querySelector('.total-price span');
    if (totalSpan) {
        totalSpan.textContent = total.toFixed(2) + ' лв.';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.size-selector select').forEach(function (sizeSelect) {
        updateQuantityOptions(sizeSelect);

        const cartItemId = sizeSelect.id.replace('size-', '');

        sizeSelect.addEventListener('change', function () {
            updateQuantityOptions(sizeSelect);
            updateProductPrice(cartItemId);
            updateTotalPrice();
        });
    });

    document.querySelectorAll('.quantity-selector').forEach(function (quantitySelect) {
        const cartItemId = quantitySelect.id.replace('quantity-', '');

        quantitySelect.addEventListener('change', function () {
            updateProductPrice(cartItemId);
            updateTotalPrice();
        });
    });

    updateTotalPrice();
});