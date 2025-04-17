function updateQuantityOptions(sizeSelect) {
    const selectedOption = sizeSelect.options[sizeSelect.selectedIndex];
    const availableQuantity = parseInt(selectedOption.dataset.quantity);
    const quantitySelect = document.getElementById("quantity");

    const preselectedQuantity = parseInt(sizeSelect.dataset.selectedQuantity);

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

window.addEventListener('DOMContentLoaded', function () {
    const sizeSelect = document.getElementById("size");
    updateQuantityOptions(sizeSelect);
});