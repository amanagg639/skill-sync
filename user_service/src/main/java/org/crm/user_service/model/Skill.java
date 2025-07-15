package org.crm.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String level; // BEGINNER, INTERMEDIATE, ADVANCED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
