package com.chapi.integration.controller;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

/**
 * ADVERTENCIA: Esta clase contiene VULNERABILIDADES INTENCIONALES
 * para demostrar las capacidades de SonarCloud.
 * 
 * NO USAR EN PRODUCCIÓN
 */
@RestController
@RequestMapping("/api/vulnerable")
public class VulnerableController {

    // VULNERABILIDAD 1: Hardcoded password (Critical)
    private static final String DB_PASSWORD = "admin123";
    private static final String API_KEY = "sk_live_1234567890abcdef";
    
    // VULNERABILIDAD 2: Uso de Random en lugar de SecureRandom para seguridad
    private Random random = new Random();

    /**
     * VULNERABILIDAD 3: SQL Injection
     * Este método es vulnerable a SQL Injection porque concatena directamente
     * la entrada del usuario en la query SQL
     */
    @GetMapping("/users")
    public String getUserByName(@RequestParam String username) {
        try {
            // Hardcoded credentials (VULNERABILIDAD)
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", 
                "root", 
                DB_PASSWORD  // Credential hardcodeada
            );
            
            Statement stmt = conn.createStatement();
            
            // SQL INJECTION VULNERABILITY - NO sanitizar input
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append(rs.getString("username")).append("\n");
            }
            
            conn.close();
            return result.toString();
            
        } catch (Exception e) {
            // VULNERABILIDAD 4: Exposición de información sensible en logs
            System.out.println("Error with password: " + DB_PASSWORD);
            e.printStackTrace();
            return "Error: " + e.getMessage(); // Stack trace expuesto al usuario
        }
    }

    /**
     * VULNERABILIDAD 5: Path Traversal
     * Permite acceso a archivos del sistema sin validación
     */
    @GetMapping("/file")
    public String readFile(@RequestParam String filename) {
        try {
            // Path traversal vulnerability - no validation
            java.io.File file = new java.io.File("/app/data/" + filename);
            java.util.Scanner scanner = new java.util.Scanner(file);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();
            return content.toString();
        } catch (Exception e) {
            return "Error reading file";
        }
    }

    /**
     * VULNERABILIDAD 6: Command Injection
     * Ejecuta comandos del sistema sin sanitizar input
     */
    @GetMapping("/ping")
    public String pingHost(@RequestParam String host) {
        try {
            // Command injection vulnerability
            Process process = Runtime.getRuntime().exec("ping -c 1 " + host);
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return output.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * VULNERABILIDAD 7: Uso inseguro de Random para tokens
     * Debe usar SecureRandom para criptografía
     */
    @GetMapping("/token")
    public String generateToken() {
        // Uso de Random en lugar de SecureRandom (inseguro)
        long token = random.nextLong();
        return "Token: " + token + ", API_KEY: " + API_KEY;
    }

    /**
     * VULNERABILIDAD 8: Null pointer dereference sin validación
     */
    @PostMapping("/process")
    public String processData(@RequestBody String data) {
        // No se valida si data es null
        String result = data.toUpperCase(); // Potential NullPointerException
        return result;
    }

    /**
     * VULNERABILIDAD 9: Weak cryptography
     * Uso de algoritmo MD5 que es débil
     */
    @GetMapping("/hash")
    public String hashPassword(@RequestParam String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return "Error";
        }
    }

    /**
     * VULNERABILIDAD 10: XXE (XML External Entity) vulnerability
     */
    @PostMapping("/xml")
    public String parseXml(@RequestBody String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            // XXE vulnerability - no se desactivan las entidades externas
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                new java.io.ByteArrayInputStream(xml.getBytes())
            );
            return "XML parsed successfully";
        } catch (Exception e) {
            return "Error parsing XML";
        }
    }
}
