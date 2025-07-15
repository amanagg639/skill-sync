package org.crm.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue
    private Long id;

    private String degree;
    private String institution;
    private String fieldOfStudy;
    private String startYear;
    private String endYear;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
