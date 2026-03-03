# 🔍 Configuración de SonarCloud para Chapi Integration

Este documento te guiará paso a paso para configurar y ejecutar el análisis de código con SonarCloud.

## 📋 Prerequisitos

1. **Cuenta en SonarCloud**: Crea una cuenta gratuita en [sonarcloud.io](https://sonarcloud.io)
2. **Java 21** (o Docker instalado)
3. **Maven 3.6+** (o Docker instalado)

## 🚀 Configuración Inicial

### Paso 1: Crear proyecto en SonarCloud

1. Ve a [SonarCloud](https://sonarcloud.io) e inicia sesión
2. Haz clic en el botón **"+"** y selecciona **"Analyze new project"**
3. Selecciona tu organización (o crea una nueva)
4. Importa tu repositorio de GitHub/GitLab/Bitbucket o crea un proyecto manual
5. Asigna el **Project Key**: `chapi-integration`
6. Anota tu **Organization Key**

### Paso 2: Obtener el Token de Autenticación

1. En SonarCloud, ve a **My Account** → **Security**
2. En la sección **Tokens**, genera un nuevo token
3. Dale un nombre descriptivo (ej: "chapi-integration-local")
4. **Copia el token** (no podrás verlo de nuevo)

### Paso 3: Actualizar archivos de configuración

Actualiza los siguientes archivos con tu información:

#### `sonar-project.properties`
```properties
sonar.projectKey=chapi-integration
sonar.organization=TU-ORGANIZATION-KEY-AQUI  # ⬅️ Actualizar
```

#### `pom.xml`
```xml
<sonar.organization>TU-ORGANIZATION-KEY-AQUI</sonar.organization>  <!-- ⬅️ Actualizar -->
```

#### `sonar-scan.sh` (línea 23)
```bash
-Dsonar.organization=TU-ORGANIZATION-KEY-AQUI \  # ⬅️ Actualizar
```

## 🔧 Ejecución del Análisis

### Opción 1: Con script (Recomendado)

```bash
# Dar permisos de ejecución al script
chmod +x sonar-scan.sh

# Ejecutar el análisis (el token se pasa como argumento)
./sonar-scan.sh TU-SONAR-TOKEN-AQUI
```

O configurar el token como variable de entorno:
```bash
export SONAR_TOKEN=TU-SONAR-TOKEN-AQUI
./sonar-scan.sh
```

### Opción 2: Comando Maven directo

```bash
# Compilar y ejecutar tests con cobertura
mvn clean verify

# Ejecutar análisis de SonarCloud
mvn sonar:sonar \
  -Dsonar.projectKey=chapi-integration \
  -Dsonar.organization=TU-ORGANIZATION-KEY-AQUI \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=TU-SONAR-TOKEN-AQUI
```

### Opción 3: Con Docker

Si no tienes Java/Maven instalado localmente:

```bash
docker run --rm \
  -v $(pwd):/usr/src \
  -w /usr/src \
  maven:3.9-eclipse-temurin-21 \
  mvn clean verify sonar:sonar \
    -Dsonar.projectKey=chapi-integration \
    -Dsonar.organization=TU-ORGANIZATION-KEY-AQUI \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.token=TU-SONAR-TOKEN-AQUI
```

## 🤖 Integración con GitHub Actions (CI/CD)

### Paso 1: Configurar el Workflow

El workflow ya está creado en `.github/workflows/sonarcloud.yml`. Solo necesitas actualizarlo con tu organization key.

### Paso 2: Agregar Secrets en GitHub

1. Ve a tu repositorio en GitHub
2. Settings → Secrets and variables → Actions
3. Crea un nuevo secret llamado `SONAR_TOKEN`
4. Pega tu token de SonarCloud

### Paso 3: Actualizar el workflow

Edita `.github/workflows/sonarcloud.yml`:
```yaml
-Dsonar.organization=TU-ORGANIZATION-KEY-AQUI  # ⬅️ Actualizar
```

Ahora cada push a `main` o `develop` ejecutará automáticamente el análisis de SonarCloud.

## 📊 Ver Resultados

Una vez completado el análisis:

1. Ve a: `https://sonarcloud.io/dashboard?id=chapi-integration`
2. O busca tu proyecto en el dashboard de SonarCloud

## 🎯 Métricas Analizadas

SonarCloud analizará:

- ✅ **Bugs**: Errores en el código
- 🔒 **Vulnerabilities**: Problemas de seguridad
- 💡 **Code Smells**: Problemas de mantenibilidad
- 📈 **Coverage**: Cobertura de código (con JaCoCo)
- 🔄 **Duplications**: Código duplicado
- 📐 **Complexity**: Complejidad ciclomática
- 📏 **Size**: Líneas de código

## 🔍 Quality Gate

Por defecto, SonarCloud usa un Quality Gate que verifica:

- Coverage > 80%
- Maintainability Rating ≥ A
- Reliability Rating ≥ A
- Security Rating ≥ A
- Security Review Rating ≥ A

Puedes personalizar estas reglas desde el dashboard de SonarCloud.

## 🛠️ Troubleshooting

### Error: "Project key already exists"
- Verifica que el `projectKey` en tu configuración coincida con el de SonarCloud

### Error: "Insufficient privileges"
- Verifica que tu token tenga permisos de "Execute Analysis"
- Regenera el token si es necesario

### Error: "Could not find or load main class"
- Ejecuta `mvn clean install` antes del análisis

### El análisis no muestra cobertura
- Verifica que JaCoCo esté generando el reporte: `target/site/jacoco/jacoco.xml`
- Ejecuta `mvn clean verify` antes de `mvn sonar:sonar`

## 📝 Archivos del Proyecto

```
chapi-integration/
├── sonar-project.properties          # Configuración de SonarCloud
├── sonar-scan.sh                     # Script de análisis
├── pom.xml                           # Incluye plugins de SonarQube y JaCoCo
└── .github/
    └── workflows/
        └── sonarcloud.yml            # CI/CD con GitHub Actions
```

## 🔗 Links Útiles

- [SonarCloud Dashboard](https://sonarcloud.io)
- [Documentación SonarCloud](https://docs.sonarcloud.io/)
- [Reglas de análisis Java](https://rules.sonarsource.com/java/)
- [Quality Gates](https://docs.sonarcloud.io/improving/quality-gates/)

## 💡 Próximos Pasos

1. **Agregar tests unitarios** para mejorar la cobertura
2. **Configurar ramas** para análisis (develop, feature branches)
3. **Personalizar Quality Gates** según tus necesidades
4. **Agregar badges** de SonarCloud a tu README
5. **Integrar con PRs** para bloquear merges que no pasen el Quality Gate

---

¿Preguntas? Revisa la [documentación oficial de SonarCloud](https://docs.sonarcloud.io/).
