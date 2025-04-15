let sizeIndex = 1;

function addSizeRow() {
    const container = document.getElementById("sizes-container");

    const newRow = document.createElement("div");
    newRow.classList.add("size-entry");

    newRow.innerHTML = `
            <div class="size">
                <label>Размер:</label>
                <select name="sizes[${sizeIndex}].size"></select>
            </div>

            <div class="quantity">
                <label>Количество:</label>
                <input type="number" name="sizes[${sizeIndex}].quantity" min="0" />
            </div>

            <button type="button" class="remove-btn" onclick="removeSizeRow(this)">✖</button>
        `;

    const firstSelect = document.querySelector("select[name^='sizes[0].size']");
    const newSelect = newRow.querySelector("select");
    newSelect.innerHTML = firstSelect.innerHTML;

    container.appendChild(newRow);
    sizeIndex++;
}

function removeSizeRow(button) {
    const container = document.getElementById("sizes-container");
    button.parentElement.remove();

    const entries = container.querySelectorAll(".size-entry");
    entries.forEach((entry, index) => {
        entry.querySelector("select").name = `sizes[${index}].size`;
        entry.querySelector("input").name = `sizes[${index}].quantity`;
    });

    sizeIndex = entries.length;
}