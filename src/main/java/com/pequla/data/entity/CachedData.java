package com.pequla.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CachedData {

    @Id
    @Column(name = "data_id")
    private Integer id;

    private String name;

    private String uuid;

    private String discordId;

    private String tag;

    private String avatar;

    private String guildId;

    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime cachedAt = LocalDateTime.now();
}
