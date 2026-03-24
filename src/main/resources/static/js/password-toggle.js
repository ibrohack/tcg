
document.querySelectorAll('.group input[type="password"]').forEach(input => {
    const group = input.closest('.group');
    const toggleBtn = group.querySelector('button');

    toggleBtn.addEventListener('click', () => {
        const isHidden = input.type === 'password';
        input.type = isHidden ? 'text' : 'password';
        toggleBtn.textContent = isHidden ? 'visibility_off' : 'visibility';
    });
});


