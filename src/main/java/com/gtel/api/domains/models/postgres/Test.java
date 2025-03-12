package com.gtel.api.domains.models.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq_gen")
    @SequenceGenerator(name = "test_seq_gen", sequenceName = "test_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "is_request_delete")
    private String isRequestDelete;
}
