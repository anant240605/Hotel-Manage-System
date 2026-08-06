package com.HotelBookingSystem.HBS.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String city;

    @NonNull
    @Column(nullable = false)
    private String address;

    private Double rating;

    @OneToMany(mappedBy = "hotel")

    private List<Room> rooms;
    @OneToMany(
            mappedBy = "hotel",
            cascade = CascadeType.ALL
    )

    private List<RoomCategory> roomCategories;


}
