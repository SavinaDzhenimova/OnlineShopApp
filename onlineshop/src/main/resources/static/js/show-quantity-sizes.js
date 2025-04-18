// Актуализира опциите за количеството според избрания размер
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

// Актуализира цената на продукта спрямо количеството
function updateProductPrice(cartItemId) {
    const quantitySelect = document.getElementById(`quantity-${cartItemId}`);
    const quantity = parseInt(quantitySelect.value);
    const priceElement = document.getElementById(`price-${cartItemId}`);

    const unitPriceText = priceElement.dataset.unitPrice; // Може да добавиш th:attr="data-unit-price=${cartItem.price}"
    const unitPrice = parseFloat(unitPriceText);

    const newPrice = quantity * unitPrice;
    priceElement.textContent = newPrice.toFixed(2) + ' лв.';
}

// Пресмята и показва новата обща сума на количката
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

// Инициализация след зареждане на страницата
document.addEventListener('DOMContentLoaded', function () {
    // За всеки size select — зареди количествата и закачи събитие
    document.querySelectorAll('.size-selector select').forEach(function (sizeSelect) {
        updateQuantityOptions(sizeSelect);

        const cartItemId = sizeSelect.id.replace('size-', '');

        // При смяна на размера — презарежда възможните количества
        sizeSelect.addEventListener('change', function () {
            updateQuantityOptions(sizeSelect);
            updateProductPrice(cartItemId);
            updateTotalPrice();
        });
    });

    // За всяко поле за количество — закачи събитие при смяна
    document.querySelectorAll('.quantity-selector').forEach(function (quantitySelect) {
        const cartItemId = quantitySelect.id.replace('quantity-', '');

        quantitySelect.addEventListener('change', function () {
            updateProductPrice(cartItemId);
            updateTotalPrice();
        });
    });

    // Първоначално изчисляване на общата сума
    updateTotalPrice();
});