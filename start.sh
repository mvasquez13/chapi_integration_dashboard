#!/bin/bash

echo "🚀 Iniciando Chapi Integration Dashboard..."
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker primero."
    exit 1
fi

# Verificar si Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado. Por favor instala Docker Compose primero."
    exit 1
fi

# Construir y ejecutar
echo "📦 Construyendo imagen Docker..."
docker-compose build

echo ""
echo "🎯 Iniciando contenedor..."
docker-compose up -d

echo ""
echo "⏳ Esperando que la aplicación esté lista..."
sleep 10

# Verificar el estado
if docker-compose ps | grep -q "Up"; then
    echo ""
    echo "✅ ¡Chapi Integration Dashboard está corriendo!"
    echo ""
    echo "🌐 Accede al dashboard en: http://localhost:8080"
    echo "📊 API REST disponible en: http://localhost:8080/api/dashboard"
    echo "💚 Health check: http://localhost:8080/actuator/health"
    echo ""
    echo "📝 Para ver logs en tiempo real:"
    echo "   docker-compose logs -f"
    echo ""
    echo "🛑 Para detener:"
    echo "   docker-compose down"
else
    echo ""
    echo "❌ Hubo un problema al iniciar el contenedor."
    echo "Verifica los logs con: docker-compose logs"
fi
