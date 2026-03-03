# ⚠️ Vulnerabilidades Intencionales para Testing de SonarCloud

## ✅ ESTADO: VULNERABILIDADES REMOVIDAS

**Fecha de remoción:** 2 de Marzo, 2026

Este documento explica las vulnerabilidades que fueron agregadas **temporalmente** al proyecto para demostrar las capacidades de detección de SonarCloud.

**Las vulnerabilidades han sido ELIMINADAS del código.**

---

## 🚨 ADVERTENCIA (Histórico)

Las siguientes vulnerabilidades fueron agregadas y posteriormente removidas con fines educativos.

---

## 📦 1. Vulnerabilidad de Dependencia

### commons-collections 3.2.1 (CVE-2015-6420)

**Ubicación:** `pom.xml`

**Tipo:** Deserialization of Untrusted Data

**Severidad:** CRITICAL

**Descripción:**
- Versión vulnerable de Apache Commons Collections
- Permite ejecución remota de código (RCE) a través de deserialización maliciosa
- CVE conocido desde 2015

**Detección esperada:** SonarCloud/Dependabot debe reportar esta vulnerabilidad conocida.

---

## 🔓 2. Vulnerabilidades Estáticas de Código

Archivo: `VulnerableController.java`

### 2.1. Hardcoded Credentials (CRITICAL)

```java
private static final String DB_PASSWORD = "admin123";
private static final String API_KEY = "sk_live_1234567890abcdef";
```

**Problema:** Contraseñas y API keys hardcodeadas en el código fuente.

**Riesgo:** 
- Exposición de credenciales en el repositorio
- Acceso no autorizado a sistemas

**Solución adecuada:** Usar variables de entorno o gestores de secretos.

---

### 2.2. SQL Injection (CRITICAL)

```java
String query = "SELECT * FROM users WHERE username = '" + username + "'";
```

**Problema:** Concatenación directa de input del usuario en SQL query.

**Riesgo:**
- Lectura no autorizada de datos
- Modificación de la base de datos
- Bypass de autenticación

**Solución adecuada:** Usar PreparedStatement o JPA con parámetros.

---

### 2.3. Uso Inseguro de Random (MEDIUM)

```java
private Random random = new Random();
```

**Problema:** Uso de `java.util.Random` para generar tokens de seguridad.

**Riesgo:**
- Tokens predecibles
- Posible bypass de autenticación

**Solución adecuada:** Usar `java.security.SecureRandom`.

---

### 2.4. Path Traversal (CRITICAL)

```java
java.io.File file = new java.io.File("/app/data/" + filename);
```

**Problema:** No se valida el path del archivo.

**Riesgo:**
- Lectura de archivos arbitrarios del sistema
- Exposición de archivos sensibles (/etc/passwd, etc.)

**Ejemplo de ataque:** `filename=../../../etc/passwd`

**Solución adecuada:** Validar y sanitizar el path, usar whitelist.

---

### 2.5. Command Injection (CRITICAL)

```java
Process process = Runtime.getRuntime().exec("ping -c 1 " + host);
```

**Problema:** Ejecución de comandos del sistema sin sanitizar input.

**Riesgo:**
- Ejecución de comandos arbitrarios
- Compromiso total del servidor

**Ejemplo de ataque:** `host=google.com; rm -rf /`

**Solución adecuada:** Usar listas blancas o librerías específicas para la tarea.

---

### 2.6. Weak Cryptography - MD5 (HIGH)

```java
java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
```

**Problema:** Uso de MD5 para hashear passwords.

**Riesgo:**
- MD5 es vulnerable a colisiones
- Facilita ataques de fuerza bruta
- No es seguro para passwords

**Solución adecuada:** Usar bcrypt, Argon2, o PBKDF2.

---

### 2.7. XML External Entity (XXE) (CRITICAL)

```java
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// No se desactivan las entidades externas
```

**Problema:** Parser XML sin protección contra entidades externas.

**Riesgo:**
- Lectura de archivos locales
- SSRF (Server-Side Request Forgery)
- Denial of Service

**Solución adecuada:**
```java
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
```

---

### 2.8. Information Disclosure (MEDIUM)

```java
System.out.println("Error with password: " + DB_PASSWORD);
return "Error: " + e.getMessage();
```

**Problema:** Exposición de información sensible en logs y respuestas.

**Riesgo:**
- Revelación de contraseñas
- Stack traces expuestos a usuarios
- Información útil para atacantes

**Solución adecuada:** Logger apropiado, mensajes genéricos al usuario.

---

### 2.9. Null Pointer Dereference (MEDIUM)

```java
String result = data.toUpperCase(); // No se valida si data es null
```

**Problema:** No se valida si el parámetro es null antes de usarlo.

**Riesgo:**
- NullPointerException
- Denial of Service
- Comportamiento inesperado

**Solución adecuada:** Validar inputs, usar Optional, o anotaciones @NonNull.

---

## 🔍 Cómo Verificar las Detecciones

### 1. Ejecutar el análisis de SonarCloud:

```bash
export SONAR_TOKEN=tu-token
./sonar-scan-docker.sh
```

### 2. Revisar el dashboard en SonarCloud:

```
https://sonarcloud.io/dashboard?id=chapi-integration
```

### 3. Métricas esperadas:

- **Bugs:** 2-5 (null checks, resource leaks)
- **Vulnerabilities:** 10+ (las listadas arriba)
- **Security Hotspots:** 5+ (áreas que requieren revisión)
- **Code Smells:** 20+ (malas prácticas)
- **Security Rating:** E (lo peor)
- **Reliability Rating:** D-E

---

## 📚 Categorías OWASP Top 10 Cubiertas

- ✅ **A01:2021 - Broken Access Control** (Path Traversal)
- ✅ **A02:2021 - Cryptographic Failures** (MD5, hardcoded secrets)
- ✅ **A03:2021 - Injection** (SQL Injection, Command Injection)
- ✅ **A04:2021 - Insecure Design** (Weak random, no input validation)
- ✅ **A05:2021 - Security Misconfiguration** (XXE enabled)
- ✅ **A06:2021 - Vulnerable Components** (commons-collections 3.2.1)
- ✅ **A09:2021 - Security Logging Failures** (Password in logs)

---

## 🛠️ Herramientas de Análisis

Además de SonarCloud, puedes usar:

1. **OWASP Dependency-Check:**
   ```bash
   docker run --rm -v $(pwd):/src owasp/dependency-check \
     --scan /src --format JSON
   ```

2. **SpotBugs:**
   ```bash
   mvn spotbugs:check
   ```

3. **GitHub Dependabot:** (automático si está en GitHub)

---

## 🔧 Cómo Corregir (No hacer ahora)

Para corregir estas vulnerabilidades:

1. **Actualizar dependencia:**
   ```xml
   <dependency>
       <groupId>org.apache.commons</groupId>
       <artifactId>commons-collections4</artifactId>
       <version>4.4</version>
   </dependency>
   ```

2. **Eliminar o corregir VulnerableController.java**

3. **Implementar:**
   - PreparedStatements para SQL
   - SecureRandom para tokens
   - Input validation
   - Proper error handling
   - Secure XML parsing

---

## 📊 Comparación: Antes vs Después

### Antes de Corregir (Estado Actual):
- Security Rating: E
- Vulnerabilities: 10+
- Bugs: 5+
- Technical Debt: 2-3 hours

### Después de Corregir:
- Security Rating: A
- Vulnerabilities: 0
- Bugs: 0
- Technical Debt: <30 min

---

## ⚖️ Notas Legales y Éticas

Este código es **solo para fines educativos** en un entorno controlado.

**NO:**
- ❌ Desplegar en producción
- ❌ Usar en aplicaciones reales
- ❌ Compartir sin el contexto educativo
- ❌ Usar para propósitos maliciosos

**SÍ:**
- ✅ Usar como material de aprendizaje
- ✅ Demostrar capacidades de SonarCloud
- ✅ Enseñar buenas prácticas por contraste
- ✅ Entrenar equipos de seguridad

---

## 📖 Recursos Adicionales

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [CWE - Common Weakness Enumeration](https://cwe.mitre.org/)
- [SonarCloud Security Rules](https://rules.sonarsource.com/java/type/Vulnerability)
- [NIST Secure Coding](https://www.nist.gov/itl/ssd/software-quality-group/secure-coding)

---

## ✅ Checklist para el Demo

- [x] Dependencia vulnerable agregada
- [x] 10+ vulnerabilidades de código estáticas
- [x] Documentación de cada vulnerabilidad
- [x] Ejemplos de OWASP Top 10
- [ ] Ejecutar análisis de SonarCloud
- [ ] Revisar resultados en dashboard
- [ ] Comparar métricas de seguridad

---

**Fecha de creación:** 2 de Marzo, 2026  
**Propósito:** Demostración educativa de SonarCloud  
**Mantenedor:** Marco Vásquez
