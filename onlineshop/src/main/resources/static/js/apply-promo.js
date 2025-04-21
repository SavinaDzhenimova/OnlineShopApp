document.getElementById("promoForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    const cartItems = [];

    document.querySelectorAll(".cart-item").forEach(item => {
        const hiddenInput = item.querySelector("input[type='hidden'][id^='unit-price-']");
        const id = hiddenInput.id.split('-')[2];
        const unitPrice = hiddenInput.value;
        const quantity = item.querySelector("select.quantity-selector").value;
        const size = item.querySelector("select[name='size']").value;

        cartItems.push({
            id: id,
            selectedQuantity: Number(quantity),
            selectedSize: Number(size),
            unitPrice: parseFloat(unitPrice)
        });
    });

    const promoCode = document.getElementById("promoCode").value;

    const formData = new FormData();
    formData.append("promoCode", promoCode);
    formData.append("cartData", JSON.stringify(cartItems));

    fetch("/promo-codes/apply-promo", {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken
        },
        body: formData
    })
        .then(res => {
        if (!res.ok) {
            throw new Error("HTTP error " + res.status);
        }
        return res.json();
    })
        .then(data => {
        if (data.error) {
            document.getElementById("discountValue").textContent = "";
            document.getElementById("finalPrice").textContent = "";

            const discountInfoDiv = document.getElementById("discountInfo");
            discountInfoDiv.textContent = "Невалиден промо код!";
            discountInfoDiv.style.color = "#C00";

            document.getElementById("discountBlock").style.display = "none";
            document.getElementById("finalBlock").style.display = "none";

            window.currentDiscountPercent = 0;

        } else {
            document.getElementById("originalPrice").textContent = data.originalPrice.toFixed(2) + " лв.";
            document.getElementById("discountValue").textContent = "- " + data.discount.toFixed(2) + " лв.";
            document.getElementById("finalPrice").textContent = data.finalPrice.toFixed(2) + " лв.";

            window.currentDiscountPercent = data.discountPercent;

            const discountInfoDiv = document.getElementById("discountInfo");
            discountInfoDiv.textContent = "Успешно приложи твоя промо код!";
            discountInfoDiv.style.color = "#4CAF50";

            document.getElementById("discountBlock").style.display = "flex";
            document.getElementById("finalBlock").style.display = "flex";
        }

        updateTotalPrice();
    })
        .catch(err => {
        console.error("Грешка при прилагане на промо кода", err);

        document.getElementById("discountValue").textContent = "";
        document.getElementById("finalPrice").textContent = "";

        const discountInfoDiv = document.getElementById("discountInfo");
        discountInfoDiv.textContent = "Невалиден промо код!";
        discountInfoDiv.style.color = "#C00";

        document.getElementById("discountBlock").style.display = "none";
        document.getElementById("finalBlock").style.display = "none";

        window.currentDiscountPercent = 0;
    });
});