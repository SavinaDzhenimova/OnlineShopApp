const ratingInputs = document.querySelectorAll('.rating input[type="radio"]');
const ratingValue = document.getElementById('ratingValue');

ratingInputs.forEach(input => {
    input.addEventListener('change', () => {
        ratingValue.textContent = `${input.value}/5`;
    });
});