#!/bin/bash

echo "🔍 Análisis de SonarCloud con Docker"
echo "===================================="
echo ""

# Verificar que SONAR_TOKEN esté configurado
if [ -z "$SONAR_TOKEN" ]; then
    echo "❌ Error: SONAR_TOKEN no está configurado"
    echo ""
    echo "Por favor, exporta tu token de SonarCloud:"
    echo ""
    echo "  export SONAR_TOKEN=tu-token-aqui"
    echo ""
    echo "O pásalo como argumento:"
    echo ""
    echo "  ./sonar-scan-docker.sh tu-token-aqui"
    echo ""
    exit 1
fi

# Si se pasa el token como argumento, usarlo
if [ ! -z "$1" ]; then
    export SONAR_TOKEN=$1
    echo "✓ Usando token proporcionado como argumento"
else
    echo "✓ Usando token de variable de entorno SONAR_TOKEN"
fi

echo ""
echo "📋 Configuración:"
echo "   Organization: marco-vasquez-l"
echo "   Project Key:  chapi-integration"
echo "   Host URL:     https://sonarcloud.io"
echo ""

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker primero."
    exit 1
fi

# Verificar que Docker Compose esté instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado. Por favor instala Docker Compose primero."
    exit 1
fi

echo "🐳 Ejecutando análisis con Docker Compose..."
echo ""

# Ejecutar docker-compose con el token
docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "✅ Análisis completado exitosamente!"
    echo "════════════════════════════════════════════════════════"
    echo ""
    echo "🌐 Ver resultados en:"
    echo "   https://sonarcloud.io/dashboard?id=chapi-integration"
    echo ""
    echo "📊 Dashboard del proyecto:"
    echo "   https://sonarcloud.io/project/overview?id=chapi-integration"
    echo ""
else
    echo ""
    echo "════════════════════════════════════════════════════════"
    echo "❌ El análisis falló con código de salida: $EXIT_CODE"
    echo "════════════════════════════════════════════════════════"
    echo ""
    echo "💡 Posibles soluciones:"
    echo "   1. Verifica que tu SONAR_TOKEN sea válido"
    echo "   2. Asegúrate de tener acceso al proyecto en SonarCloud"
    echo "   3. Revisa los logs arriba para más detalles"
    echo ""
    exit $EXIT_CODE
fi

# Limpiar contenedores y redes
echo "🧹 Limpiando recursos..."
docker-compose -f docker-compose.sonar.yml down 2>/dev/null

echo ""
echo "✨ Proceso completado"
