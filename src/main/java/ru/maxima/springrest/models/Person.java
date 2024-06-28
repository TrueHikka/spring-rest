package ru.maxima.springrest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
//    @Min(value=2, message="Name must be at least 2 symbols")
//    @Max(value=50, message="Name must be at most 50 symbols")
    @Size(min=2, max=50, message="Name must be between 2 and 50 symbols")
    @Column(name = "name")
    private String name;

    @Min(value = 0, message = "Age should be min 0 years")
    @Column(name = "age")
    private Integer age;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Column(name = "password")
//    @NotEmpty(message = "Password is required")
    private String password;

    @Column(name  = "role")
    private String role;

    //Кто создал этого пользователя
    @Column(name = "created_by")
    private String createdBy;

    //Когда был создан
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //Когда был удален
    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    //Удален пользователь или нет
    @Column(name = "removed")
    private Boolean removed;
}
