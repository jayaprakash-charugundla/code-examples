package com.spring.boot.excel.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String from;
    private String to;
    private String subject;
}
