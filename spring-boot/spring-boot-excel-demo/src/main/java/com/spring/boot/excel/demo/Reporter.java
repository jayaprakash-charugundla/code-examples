package com.spring.boot.excel.demo;

import java.io.ByteArrayOutputStream;

public interface Reporter {
    ByteArrayOutputStream generateReport();
}
