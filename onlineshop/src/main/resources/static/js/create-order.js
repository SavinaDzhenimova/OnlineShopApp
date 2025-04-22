document.addEventListener("DOMContentLoaded", function () {
    function gatherOrderItems() {
        const orderItems = [];

        document.querySelectorAll('.cart-item').forEach(function (item) {
            const orderItem = {
                name: item.querySelector('.product-title h2').textContent,
                imageUrl: item.querySelector('img').src,
                category: getCategory(item),
                selectedQuantity: item.querySelector('.quantity-selector').value,
                selectedSize: item.querySelector('.size-selector select').value,
                unitPrice: parseFloat(item.querySelector('input[id^="unit-price-"]').value)
            };

            orderItems.push(orderItem);
        });

        return orderItems;
    }

    function getCategory(item) {
        const categoryText = item.querySelector('.product-type span')?.textContent || '';
        if (categoryText.includes('Мъжки')) return 'Мъжки обувки';
        if (categoryText.includes('Дамски')) return 'Дамски обувки';
        if (categoryText.includes('Детски')) return 'Детски обувки';
        return '';
    }

    document.querySelector('#orderForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const promoCode = document.querySelector('#promoCode')?.value || null;
        const orderItems = gatherOrderItems();

        const payload = {
            promoCode: promoCode && promoCode.trim() !== '' ? promoCode : null,
            orderItems: orderItems
        };

        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch('/orders/create-order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(payload)
        }).then(response => {
            if (response.ok) {
                window.location.href = '/orders/confirmation';
            } else {
                alert('Грешка при създаване на поръчка');
            }
        });
    });
});