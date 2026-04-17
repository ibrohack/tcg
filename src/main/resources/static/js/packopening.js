document.addEventListener('DOMContentLoaded', () => {
    let packForm = document.querySelector('#packForm');
    let packContainer = document.querySelector('#packContainer');
    const openSound = new Audio('/audio/monkeypackopen.mp3');
    if (packForm && packContainer) {
        packForm.addEventListener('submit', function (e) {
            // 1. Prevent immediate submission
            e.preventDefault();

            // 2. Add the animation class
            openSound.play().catch(error => {
                console.log("Audio play failed:", error);
            });
            packContainer.classList.add('animate-pack-open');

            // 3. Wait for the animation to reach the "peak" (4000ms) in this case aduio based
            // Then submit the form to the Spring Boot controller
            setTimeout(() => {
                packForm.submit();
            }, 4000);
        });
    }
});