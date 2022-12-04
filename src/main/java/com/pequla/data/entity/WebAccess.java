package com.pequla.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "access")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WebAccess {

    @Id
    @Column(name = "access_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;

    private String xff;

    private String path;

    private LocalDateTime createdAt = LocalDateTime.now();
}
