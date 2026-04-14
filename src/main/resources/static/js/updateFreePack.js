$(document).ready(function () {
    var packConfig = $('#pack-config');
    if (packConfig.length === 0) return;

    function updatePackPrice() {
        $.get('/pack/price', function (data) {
            var newPrice = data.price;
            var currentPrice = parseInt($('#packPrice').text().trim(), 10);
            if (newPrice !== currentPrice) {
                $('#packPrice').text(newPrice);
            }
        });
    }

    setInterval(updatePackPrice, 5000);
    updatePackPrice();
});