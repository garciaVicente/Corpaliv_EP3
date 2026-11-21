package cl.corpaliv.auth.dto;

public record LoginResponse(String token, String tipo, String rol, String email) {}
