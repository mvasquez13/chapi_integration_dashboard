#!/bin/bash

echo "🔍 Escaneando con SonarCloud..."
echo ""

# Verificar que SONAR_TOKEN esté configurado
if [ -z "$SONAR_TOKEN" ]; then
    echo "❌ Error: SONAR_TOKEN no está configurado"
    echo ""
    echo "Por favor, configura tu token de SonarCloud:"
    echo "  export SONAR_TOKEN=tu-token-aqui"
    echo ""
    echo "O pásalo como argumento:"
    echo "  ./sonar-scan.sh tu-token-aqui"
    exit 1
fi

# Si se pasa el token como argumento, usarlo
if [ ! -z "$1" ]; then
    SONAR_TOKEN=$1
fi

echo "📦 Compilando proyecto y ejecutando tests con cobertura..."
mvn clean verify

if [ $? -ne 0 ]; then
    echo "❌ Error al compilar el proyecto"
    exit 1
fi

echo ""
echo "🚀 Ejecutando análisis de SonarCloud..."
mvn sonar:sonar \
  -Dsonar.projectKey=mvasquez13_chapi_integration_dashboard \
  -Dsonar.organization=marco-vasquez-l \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=$SONAR_TOKEN

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Análisis completado exitosamente!"
    echo ""
    echo "🌐 Revisa los resultados en:"
    echo "   https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard"
else
    echo ""
    echo "❌ El análisis falló. Revisa los logs arriba."
    exit 1
fi
