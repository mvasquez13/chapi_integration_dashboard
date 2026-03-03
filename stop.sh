#!/bin/bash

echo "🛑 Deteniendo Chapi Integration Dashboard..."
echo ""

docker-compose down

echo ""
echo "✅ Aplicación detenida correctamente."
echo ""
echo "💡 Para limpiar completamente (incluyendo volúmenes):"
echo "   docker-compose down -v"
echo ""
echo "💡 Para eliminar también las imágenes:"
echo "   docker-compose down --rmi all"
