$(document).ready(function () {
    var shopConfig = $('#shop-config');
    if (shopConfig.length === 0) return;

    var serverTimeValue = shopConfig.data('server-time');
    var serverTimeMs = parseInt(serverTimeValue, 10);

    if (isNaN(serverTimeMs)) {
        serverTimeMs = Date.now();
    }

    var timeOffset = serverTimeMs - Date.now();
    var isReloading = false;
    var packTimerDiv = $('#pack-timer');

	function updateAvailability(ss) {
	    $.get('/pack/availability', function (data) {
	        var packAvailability = data.packAvailability;

	        if (!packAvailability) {
	            packTimerDiv.html('<span class="material-symbols-outlined text-sm">schedule</span> Next in: 00:00:' + ss);
	            packTimerDiv.removeClass('text-primary border-primary/40').addClass('text-primary/60 border-primary/10');
	        } else {
	            packTimerDiv.html('<span class="material-symbols-outlined text-sm">auto_awesome</span> Pack Available!');
	            packTimerDiv.removeClass('text-primary/60 border-primary/10').addClass('text-primary border-primary/40');
	        }
	    });
	}


    function updateTimer() {
        if (isReloading) return;

        var now = new Date(Date.now() + timeOffset);
        var secondsLeft = 60 - now.getSeconds();
        if (secondsLeft === 60) secondsLeft = 0;

        var ss = secondsLeft;
        if (ss < 10) ss = "0" + ss;

        $('#reset-timer').text("00:00:" + ss);
        updateAvailability(ss);

		  if (secondsLeft === 0) {
		            isReloading = true;

		            // Show loading animation in the grid
		            var grid = $('#flash-acquisitions-section .grid');
		            grid.html('<div class="flex flex-col justify-center items-center py-20 w-full md:col-span-3 gap-4"><span class="material-symbols-outlined animate-spin text-6xl text-primary">autorenew</span><span class="text-primary font-bold tracking-widest text-sm animate-pulse uppercase">Restocking Inventory...</span></div>');

		            // Swap timer text with syncing animation natively
		            $('#reset-timer').html('<span class="material-symbols-outlined animate-spin text-lg inline-block align-middle">sync</span>').removeClass('font-mono');

		            // Wait 3 seconds before executing the fetch
		            setTimeout(function () {
		                $('#flash-acquisitions-section').load(window.location.pathname + ' #flash-acquisitions-section > *', function () {
		                    isReloading = false;
		                });
		            }, 3000);
		        }
		    }

		    setInterval(updateTimer, 1000);
		    updateTimer();
		});