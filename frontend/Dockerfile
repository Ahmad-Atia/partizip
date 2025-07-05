# Use Node.js for both build and serve
FROM node:18-alpine

# Install dependencies
RUN apk add --no-cache git

# Install Expo CLI and ngrok globally
RUN npm install -g @expo/cli@latest @expo/ngrok@^4.1.0

# Set working directory
WORKDIR /app

# Copy package files (assuming you're building from root directory)
COPY frontend/package*.json ./

# Install dependencies with legacy peer deps to handle conflicts
RUN npm install --legacy-peer-deps

# Copy source code from frontend directory
COPY frontend/ .

# Create App.js (only if it doesn't exist)
RUN if [ ! -f App.js ] && [ ! -f App.tsx ]; then \
    echo 'import { StatusBar } from "expo-status-bar";\nimport { StyleSheet, Text, View } from "react-native";\n\nexport default function App() {\n  return (\n    <View style={styles.container}>\n      <Text>Welcome to PartiZip!</Text>\n      <StatusBar style="auto" />\n    </View>\n  );\n}\n\nconst styles = StyleSheet.create({\n  container: {\n    flex: 1,\n    backgroundColor: "#fff",\n    alignItems: "center",\n    justifyContent: "center",\n  },\n});' > App.js; \
    fi

# Create babel.config.js if it doesn't exist
RUN if [ ! -f babel.config.js ]; then \
    echo 'module.exports = function(api) {\n  api.cache(true);\n  return {\n    presets: ["babel-preset-expo"],\n  };\n};' > babel.config.js; \
    fi

# Expose Metro bundler port
EXPOSE 8081

# Set environment variables for Metro
ENV EXPO_DEVTOOLS_LISTEN_ADDRESS=0.0.0.0
ENV REACT_NATIVE_PACKAGER_HOSTNAME=0.0.0.0
ENV EXPO_NO_DOTENV=1

# Start with tunnel and wait for Metro to be ready
CMD ["sh", "-c", "npx expo start --tunnel --non-interactive --clear"]