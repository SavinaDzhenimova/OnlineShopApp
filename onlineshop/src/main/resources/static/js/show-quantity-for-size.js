document.addEventListener('DOMContentLoaded', function () {
    const sizeSelect = document.getElementById('size');
    const quantitySelect = document.getElementById('quantity');

    sizeSelect.addEventListener('change', function () {
        const selectedOption = sizeSelect.options[sizeSelect.selectedIndex];
        const quantity = parseInt(selectedOption.getAttribute('data-quantity'));

        quantitySelect.innerHTML = '';

        if (!isNaN(quantity) && quantity > 0) {
            quantitySelect.disabled = false;

            for (let i = 1; i <= quantity && i <= 10; i++) {
                const option = document.createElement('option');
                option.value = i;
                option.textContent = i;
                quantitySelect.appendChild(option);
            }
        } else {
            quantitySelect.disabled = true;

            const option = document.createElement('option');
            option.value = '';
            option.textContent = '-- Няма наличност --';
            quantitySelect.appendChild(option);
        }
    });
});