tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                "primary": "#f4d125",
                "background-light": "#f8f8f5",
                "background-dark": "#221f10",
                "card-common": "#94a3b8",
                "card-rare": "#3b82f6",
                "card-epic": "#a855f7",
                "card-legendary": "#f97316",
                "card-mythic": "#ef4444",
                "card-arok": "#2dd4bf",
            },
            fontFamily: {
                "display": ["Space Grotesk", "sans-serif"]
            },
            borderRadius: {
                "DEFAULT": "0.25rem",
                "lg": "0.5rem",
                "xl": "0.75rem",
                "full": "9999px"
            },
        },
    },
}