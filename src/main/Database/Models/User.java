package main.Database.Models;

public record User(
        String firstName,
        String lastName,
        String email,
        String login,
        String password,
        String phoneNumber
) {}
