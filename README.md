# Chapi Integration Dashboard

Dashboard minimalista de indicadores construido con Java 21 y Spring Boot.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mvasquez13_chapi_integration_dashboard&metric=alert_status)](https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mvasquez13_chapi_integration_dashboard&metric=coverage)](https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mvasquez13_chapi_integration_dashboard&metric=bugs)](https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mvasquez13_chapi_integration_dashboard&metric=code_smells)](https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard)

## 🚀 Características

- **Dashboard en tiempo real** con indicadores dinámicos
- **API REST** para consumir datos programáticamente
- **Diseño responsive** y minimalista
- **Auto-refresh** cada 30 segundos
- **Categorización de indicadores**: Performance, Business, Reliability
- **Indicadores de tendencia**: Up, Down, Stable
- **Estado general del sistema**: Healthy, Warning, Critical

## 📋 Requisitos

### Opción 1: Con Docker (Recomendado)
- Docker
- Docker Compose

### Opción 2: Sin Docker
- Java 21
- Maven 3.6+

## 🛠️ Instalación

### Con Docker 🐳

#### Método rápido (usando script):
```bash
cd chapi-integration
chmod +x start.sh
./start.sh
```

#### Método manual:

1. Navegar al directorio del proyecto:
```bash
cd chapi-integration
```

2. Construir y ejecutar con Docker Compose:
```bash
docker-compose up --build
```

O en modo detached (segundo plano):
```bash
docker-compose up -d --build
```

3. Ver logs:
```bash
docker-compose logs -f
```

4. Detener:
```bash
docker-compose down
```

O usando el script:
```bash
./stop.sh
```

### Sin Docker

1. Clonar o navegar al directorio del proyecto:
```bash
cd chapi-integration
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

O ejecutar el JAR directamente:
```bash
java -jar target/chapi-integration-1.0.0.jar
```

## 🌐 Uso

### Dashboard Web
Acceder al dashboard en tu navegador:
```
http://localhost:8080
```

### API REST
Obtener datos del dashboard en formato JSON:
```
http://localhost:8080/api/dashboard
```

### Actuator Endpoints
Verificar el estado de la aplicación:
```
http://localhost:8080/actuator/health
```

## 🔍 Análisis de Código con SonarCloud

Este proyecto está configurado para análisis automático de calidad de código con SonarCloud.

### Ejecutar análisis con Docker (sin Maven/Java local) 🐳:

```bash
# Configurar token
export SONAR_TOKEN=tu-token-aqui

# Ejecutar análisis
./sonar-scan-docker.sh
```

**📖 Guía completa con Docker:** Ver [SONARCLOUD-DOCKER.md](SONARCLOUD-DOCKER.md)

### Ejecutar análisis localmente (con Maven):

```bash
# Configurar token
export SONAR_TOKEN=tu-token-aqui

# Ejecutar análisis
./sonar-scan.sh
```

O directamente con Maven:
```bash
mvn clean verify sonar:sonar \
  -Dsonar.token=$SONAR_TOKEN
```

### Ver resultados:
```
https://sonarcloud.io/dashboard?id=mvasquez13_chapi_integration_dashboard
```

**📖 Guía completa de configuración:** Ver [SONARCLOUD.md](SONARCLOUD.md)

## 📊 Indicadores Incluidos

El dashboard muestra los siguientes indicadores de ejemplo:

**Performance:**
- CPU Usage
- Memory Usage
- Disk Usage
- Response Time

**Business:**
- Active Users
- Requests/min

**Reliability:**
- Uptime
- Error Rate

## 🎨 Personalización

### Modificar Indicadores
Edita el archivo `IndicatorService.java` para agregar o modificar indicadores:
```java
src/main/java/com/chapi/integration/service/IndicatorService.java
```

### Cambiar Estilos
Modifica el archivo `dashboard.html` para personalizar el diseño:
```html
src/main/resources/templates/dashboard.html
```

### Configuración de la Aplicación
Ajusta parámetros en:
```properties
src/main/resources/application.properties
```

## 🏗️ Estructura del Proyecto

```
chapi-integration/
├── src/
│   ├── main/
│   │   ├── java/com/chapi/integration/
│   │   │   ├── ChapiIntegrationApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── ApiController.java
│   │   │   │   └── DashboardController.java
│   │   │   ├── model/
│   │   │   │   ├── Dashboard.java
│   │   │   │   └── Indicator.java
│   │   │   └── service/
│   │   │       └── IndicatorService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── dashboard.html
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .gitignore
├── pom.xml
└── README.md
```

## 🔧 Tecnologías

- **Java 21**
- **Spring Boot 3.2.3**
- **Spring Web**
- **Thymeleaf**
- **Spring Boot Actuator**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

## 🐳 Docker

El proyecto incluye configuración completa de Docker:

- **Dockerfile multi-stage**: Optimizado para construir y ejecutar la aplicación
- **docker-compose.yml**: Orquestación simplificada
- **Health checks**: Monitoreo automático del estado del contenedor
- **Scripts de utilidad**: `start.sh` y `stop.sh` para inicio/parada rápidos
- **.dockerignore**: Optimización del contexto de build

### Características del contenedor:
- Imagen base: Eclipse Temurin (OpenJDK 21)
- Puerto expuesto: 8080
- Health check integrado
- Reinicio automático (unless-stopped)
- Memoria configurada: 256MB-512MB

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o pull request para sugerencias o mejoras.
