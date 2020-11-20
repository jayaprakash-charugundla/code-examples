package com.jc.spring.jpa.criteria.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "JPA_CRITERIA_EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    String name;

    @Column
    String email;

    @Column
    Date date;
}


