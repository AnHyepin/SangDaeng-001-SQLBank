package com.example.sangdaeng001sqlbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_class_settings")
@Getter
@Setter
@NoArgsConstructor
public class ClassSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_num", nullable = false)
    private int classNum;
}
