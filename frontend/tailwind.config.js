const config = {
    darkMode: "class",
    content: [
      "./index.html",
      "./src/**/*.{js,ts,jsx,tsx,css}",
    ],
    theme: {
      extend: {
        fontFamily: {
          sans: ['Inter', 'ui-sans-serif', 'system-ui']
        },
        colors: {
          background: 'oklch(var(--background) / <alpha-value>)',
          foreground: 'oklch(var(--foreground) / <alpha-value>)',
        },
      },
    },
    plugins: [],
  }
  
  export default config;