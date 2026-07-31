package com.HotelBookingSystem.HBS.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
        import lombok.*;
        import java.util.List;

@Entity(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    private Role role;




}
