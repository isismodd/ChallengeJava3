package br.com.fiap.ClyvoPet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenha {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String hash =
                encoder.encode("Admin123");

        System.out.println(hash);

        System.out.println(
                encoder.matches("Admin123", hash)
        );
    }
}