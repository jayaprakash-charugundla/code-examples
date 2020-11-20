package com.jc.spring.jpa.criteria.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedMeta {
    private int offset = 0;
    private int count = 0;
    private long totalCount = 0;
}
