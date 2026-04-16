$(document).ready(function () {
    var packConfig = $('#pack-config');
    if (packConfig.length === 0) return;

    function updatePackPrice() {
        $.get('/pack/availability', function (data) {
            var packAvailability = data.packAvailability;
            if (!packAvailability) {
                $('#packPrice').text("500");
            }else{
				$('#packPrice').text("0");
			}
        });
    }
	
    setInterval(updatePackPrice, 1000);
    updatePackPrice();
});