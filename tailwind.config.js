/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/main/webapp/WEB-INF/views/**/*.jsp",
        "./src/**/*.{html,jsp,js,jsx,ts,tsx}",
        "./public/**/*.jsp"
    ],
    theme: {
        extend: {
            colors: {
                'dark-moss-green': '#516040ff',
                'pistachio': '#88b861ff',
                'pistachio-dark': '#689a3f',
                'thistle': '#ddcce1ff',
                'pomp-and-power': '#84599bff',
                'finn': '#6b436eff',
            },
        },
    },
    plugins: [],
};
