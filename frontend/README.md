# Partizip Frontend

A React-based frontend for the Partizip community engagement platform.

## Features

- Modern React 18 application
- React Router for navigation
- Responsive design
- API integration with backend services
- Docker support

## Development

### Prerequisites

- Node.js 18 or higher
- npm or yarn

### Getting Started

1. Install dependencies:
```bash
npm install
```

2. Start development server:
```bash
npm start
```

The application will be available at http://localhost:3000

### Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

## Docker

### Build Docker Image

```bash
docker build -t partizip-frontend .
```

### Run Docker Container

```bash
docker run -p 80:80 partizip-frontend
```

## Production Deployment

The application is built using a multi-stage Docker build:

1. **Build stage**: Uses Node.js to install dependencies and build the React app
2. **Production stage**: Uses Nginx to serve the built static files

### Environment Variables

- `REACT_APP_API_URL` - Backend API URL (default: http://localhost:3000)

## Architecture

- **Frontend Framework**: React 18
- **Routing**: React Router 6
- **HTTP Client**: Axios
- **Build Tool**: Create React App
- **Web Server**: Nginx (production)

## API Integration

The frontend communicates with the backend through the API Gateway service. All API calls are proxied through Nginx to avoid CORS issues.

## Folder Structure

```
frontend/
├── public/           # Static assets
├── src/             # Source code
│   ├── components/  # React components
│   ├── App.js       # Main application component
│   ├── App.css      # Application styles
│   └── index.js     # Application entry point
├── Dockerfile       # Docker configuration
├── nginx.conf       # Nginx configuration
└── package.json     # Dependencies and scripts
```
