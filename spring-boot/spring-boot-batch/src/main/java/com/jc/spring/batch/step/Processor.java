package com.jc.spring.batch.step;

import com.jc.spring.batch.domain.Patient;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<Patient, Patient> {
    @Override
    public Patient process(Patient patient) throws Exception {
        return patient;
    }
}
