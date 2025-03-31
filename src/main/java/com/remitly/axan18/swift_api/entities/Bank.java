package com.remitly.axan18.swift_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 11,name = "swiftcode", columnDefinition = "varchar(11)", nullable = false)
    private String swiftCode;

    @NotNull
    @NotBlank
    @Column(length = 2,name = "countryiso2", columnDefinition = "varchar(2)")
    private String countryISO2;

    @NotNull
    @NotBlank
    @Column(length = 255,name = "bankname", columnDefinition = "varchar(255)")
    private String bankName;

    @Column(length = 255,name = "address", columnDefinition = "varchar(255)")
    private String address;

    @NotBlank
    @Column(length = 255,name = "countryname", columnDefinition = "varchar(255)")
    private String countryName;

    @NotNull
    @Column(name = "isheadquarter")
    private Boolean isHeadquarter;

}
