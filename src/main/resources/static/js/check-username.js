document.addEventListener('DOMContentLoaded', () => {
    const usernameInput = document.getElementById('usernameInput');
    const statusIcon = document.getElementById('usernameStatusIcon');
    const statusMsg = document.getElementById('usernameStatusMsg');
    let debounceTimer;

    if (usernameInput) {
        usernameInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            const username = usernameInput.value.trim();
            
            // Reset styling
            usernameInput.className = "w-full bg-background-dark border border-primary/10 focus:border-primary/50 focus:ring-1 focus:ring-primary/20 text-slate-100 pl-12 pr-10 py-4 rounded-lg transition-all font-mono text-sm placeholder:text-slate-600";
            statusIcon.className = "hidden";
            statusMsg.className = "hidden";
            
            if (username === '') {
                return;
            }

            debounceTimer = setTimeout(async () => {
                try {
                    const response = await fetch(`/api/check-username?username=${encodeURIComponent(username)}`);
                    const data = await response.json();
                    
                    statusIcon.classList.remove('hidden');
                    statusMsg.classList.remove('hidden');

                    if (data.available) {
                        statusIcon.textContent = 'check_circle';
                        statusIcon.className = 'material-symbols-outlined text-green-500';
                        statusMsg.textContent = 'Username is available';
                        statusMsg.className = 'text-[10px] text-green-500 mt-1 ml-1';
                        usernameInput.className = "w-full bg-background-dark border border-green-500/50 focus:border-green-500/50 focus:ring-1 focus:ring-green-500/20 text-slate-100 pl-12 pr-10 py-4 rounded-lg transition-all font-mono text-sm placeholder:text-slate-600";
                    } else {
                        statusIcon.textContent = 'cancel';
                        statusIcon.className = 'material-symbols-outlined text-red-500';
                        statusMsg.textContent = 'Username is already taken';
                        statusMsg.className = 'text-[10px] text-red-500 mt-1 ml-1';
                        usernameInput.className = "w-full bg-background-dark border border-red-500/50 focus:border-red-500/50 focus:ring-1 focus:ring-red-500/20 text-slate-100 pl-12 pr-10 py-4 rounded-lg transition-all font-mono text-sm placeholder:text-slate-600";
                    }
                } catch (error) {
                    console.error("Error checking username availability", error);
                }
            }, 500); // 500ms debounce
        });
    }
});
