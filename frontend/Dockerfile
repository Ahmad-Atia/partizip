# Use Node.js for both build and serve
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package files
COPY frontend/package*.json ./

# Install dependencies (including dev dependencies for build)
RUN npm install

# Copy source code
COPY frontend/ .

# Build the application
RUN npm run build

# Install serve to host the static files
RUN npm install -g serve

# Expose port 5000
EXPOSE 5000

# Start the application using serve
CMD ["serve", "-s", "build", "-l", "5000"]
