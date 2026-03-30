package com.banco.api.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credenciais) {
        String usuario = credenciais.get("usuario");
        String senha = credenciais.get("senha");

        if ("admin".equals(usuario) && "admin123".equals(senha)) {
            // Um mock simples para o projeto acadêmico do aluno
            return ResponseEntity.ok("Token-Secreto-12345");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }
    }
}
