package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camps {

    @Id
    private UUID campId;
    private String name;
    private Integer level;
    private String address;
    private String mapLink;
    private Integer price;
    private Integer capacity;
    private String description;
    private String picture;

}