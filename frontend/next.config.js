/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  // output: 'export', // Disabled for dev server - enable only for static build
  // distDir: 'out',   // Disabled for dev server - enable only for static build
  images: {
    unoptimized: true,
  },
}

module.exports = nextConfig
