# 🐳 Análisis de SonarCloud con Docker

Esta guía te muestra cómo ejecutar el análisis de SonarCloud **sin necesidad de tener Maven o Java instalados** en tu máquina local, usando solo Docker.

## 📋 Prerequisitos

Solo necesitas tener instalado:
- ✅ Docker
- ✅ Docker Compose
- ✅ Token de SonarCloud

## 🚀 Ejecución Rápida

### Paso 1: Obtener tu Token de SonarCloud

1. Ve a [SonarCloud](https://sonarcloud.io) e inicia sesión
2. Click en tu avatar → **My Account** → **Security**
3. Genera un nuevo token
4. Copia el token

### Paso 2: Ejecutar el análisis

#### Opción A: Con el script (Recomendado) ⭐

```bash
# Exportar el token
export SONAR_TOKEN=tu-token-de-sonarcloud

# Ejecutar el script
./sonar-scan-docker.sh
```

O pasar el token directamente:
```bash
./sonar-scan-docker.sh tu-token-de-sonarcloud
```

#### Opción B: Con Docker Compose directamente

```bash
# Exportar el token
export SONAR_TOKEN=tu-token-de-sonarcloud

# Ejecutar el análisis
docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner

# Limpiar después
docker-compose -f docker-compose.sonar.yml down
```

#### Opción C: Con archivo .env (para desarrollo)

```bash
# Crear archivo .env.sonar desde el ejemplo
cp .env.sonar.example .env.sonar

# Editar y agregar tu token
nano .env.sonar  # o usa tu editor favorito

# Cargar las variables y ejecutar
export $(cat .env.sonar | xargs) && ./sonar-scan-docker.sh
```

⚠️ **IMPORTANTE**: Nunca commitees el archivo `.env.sonar` con tu token real. Ya está incluido en `.gitignore`.

## 📊 Ver Resultados

Una vez completado el análisis, accede a:

```
https://sonarcloud.io/dashboard?id=chapi-integration
```

O al overview del proyecto:

```
https://sonarcloud.io/project/overview?id=chapi-integration
```

## 🔧 Cómo Funciona

El `docker-compose.sonar.yml` hace lo siguiente:

1. **Descarga** la imagen de Maven con Java 21
2. **Monta** tu código en el contenedor
3. **Compila** el proyecto con `mvn clean verify`
4. **Ejecuta** el análisis con `mvn sonar:sonar`
5. **Envía** los resultados a SonarCloud
6. **Limpia** automáticamente después

### Ventajas de usar Docker:

✅ No necesitas instalar Java 21  
✅ No necesitas instalar Maven  
✅ Entorno consistente y reproducible  
✅ Fácil de usar en CI/CD  
✅ No contamina tu máquina local  
✅ Cache de dependencias Maven persistente  

## 📁 Archivos Creados

```
chapi-integration/
├── docker-compose.sonar.yml    # Configuración de Docker Compose
├── sonar-scan-docker.sh        # Script automatizado
├── .env.sonar.example          # Plantilla para variables de entorno
└── .gitignore                  # Incluye .env.sonar
```

## 🔍 Configuración del docker-compose.sonar.yml

```yaml
services:
  sonar-scanner:
    image: maven:3.9-eclipse-temurin-21  # Java 21 + Maven
    volumes:
      - .:/app                            # Código fuente
      - maven-cache:/root/.m2             # Cache de Maven (persistente)
    environment:
      - SONAR_TOKEN                       # Tu token
      - SONAR_HOST_URL=https://sonarcloud.io
      - SONAR_ORGANIZATION=marco-vasquez-l
      - SONAR_PROJECT_KEY=chapi-integration
```

## 🛠️ Troubleshooting

### Error: "SONAR_TOKEN no está configurado"

```bash
# Asegúrate de exportar el token antes
export SONAR_TOKEN=tu-token-aqui
```

### Error: "Docker is not running"

```bash
# Inicia Docker Desktop o el daemon de Docker
sudo systemctl start docker  # En Linux
```

### El análisis es lento la primera vez

Es normal. Docker necesita:
- Descargar la imagen de Maven (~500MB)
- Descargar todas las dependencias del proyecto

Las siguientes ejecuciones serán mucho más rápidas gracias al cache.

### Error: "Insufficient privileges"

Verifica que:
1. Tu token sea válido
2. Tu usuario tenga permisos en SonarCloud
3. El proyecto existe en SonarCloud con el key correcto

### Limpiar cache de Maven

Si quieres limpiar completamente el cache:

```bash
# Ver volúmenes
docker volume ls | grep maven

# Eliminar el volumen de cache
docker volume rm chapi-integration_maven-cache

# O eliminar todo
docker-compose -f docker-compose.sonar.yml down -v
```

## 💡 Comandos Útiles

```bash
# Ver logs en tiempo real
docker-compose -f docker-compose.sonar.yml logs -f

# Entrar al contenedor para debugging
docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner bash

# Ver el tamaño del cache de Maven
docker volume inspect chapi-integration_maven-cache

# Limpiar todo (contenedores, redes, volúmenes)
docker-compose -f docker-compose.sonar.yml down -v
docker system prune -f
```

## 🔄 Integración con CI/CD

Puedes usar el mismo `docker-compose.sonar.yml` en tu pipeline:

### GitHub Actions:
```yaml
- name: SonarCloud Scan with Docker
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
  run: |
    docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner
```

### GitLab CI:
```yaml
sonarcloud:
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner
  variables:
    SONAR_TOKEN: $SONAR_TOKEN
```

## 📈 Optimizaciones

### Reducir tiempo de build

El docker-compose ya incluye:
- ✅ Cache de dependencias Maven (volumen persistente)
- ✅ Reutilización de capas de Docker
- ✅ Compilación incremental de Maven

### Ejecutar solo el análisis (sin compilar)

Si ya compilaste antes y solo quieres analizar:

```bash
docker-compose -f docker-compose.sonar.yml run --rm sonar-scanner \
  sh -c "mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN"
```

## 🔐 Seguridad

### Mejores prácticas:

1. ✅ **Nunca** commitees tu token en el repositorio
2. ✅ Usa `.env.sonar` solo para desarrollo local
3. ✅ Usa secrets en CI/CD (GitHub Secrets, GitLab Variables, etc.)
4. ✅ Revoca tokens que ya no uses
5. ✅ Genera tokens específicos por proyecto/entorno

### Variables de entorno soportadas:

```bash
SONAR_TOKEN          # Token de autenticación (requerido)
SONAR_HOST_URL       # URL de SonarCloud (default: https://sonarcloud.io)
SONAR_ORGANIZATION   # Tu organización (default: marco-vasquez-l)
SONAR_PROJECT_KEY    # Key del proyecto (default: chapi-integration)
```

## ✨ Ejemplo Completo

```bash
# 1. Clonar/navegar al proyecto
cd chapi-integration

# 2. Verificar que Docker esté corriendo
docker --version
docker-compose --version

# 3. Configurar el token
export SONAR_TOKEN=squ_1234567890abcdef

# 4. Ejecutar el análisis
./sonar-scan-docker.sh

# 5. Esperar a que termine (primera vez ~5-10min)

# 6. Ver resultados
# https://sonarcloud.io/dashboard?id=chapi-integration

# 7. Limpiar (opcional)
docker-compose -f docker-compose.sonar.yml down
```

## 🆘 Soporte

Si tienes problemas:

1. Revisa los logs completos
2. Verifica tu token en SonarCloud
3. Asegúrate de tener acceso al proyecto
4. Consulta [docs.sonarcloud.io](https://docs.sonarcloud.io/)

---

¿Preguntas? Abre un issue o consulta la [documentación oficial de SonarCloud](https://docs.sonarcloud.io/).
