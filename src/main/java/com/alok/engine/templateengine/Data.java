package com.alok.engine.templateengine;

import lombok.*;

import java.io.Serializable;

@lombok.Data
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Data implements Serializable {
    private String firstname;
    private String lastname;
    private String street;
    private String zipCode;
    private String city;
    private Boolean covered;
    private Boolean isHTML;
}
