package com.campuslite.logic;

import com.campuslite.domain.GradeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Maneja reportes y cálculos.
 */
public class ReportManager {

    private final List<GradeRecord> records;

    public ReportManager() {

        records = new ArrayList<>();
    }

    /**
     * Agrega registro.
     */
    public void addRecord(GradeRecord record) {

        records.add(record);
    }

    /**
     * Obtiene todos los registros.
     */
    public List<GradeRecord> getRecords() {
        return records;
    }

    /**
     * Filtra registros por curso.
     */
    public List<GradeRecord> getRecordsByCourse(String courseCode) {

        List<GradeRecord> filtered = new ArrayList<>();

        for (GradeRecord record : records) {

            if (record.getCourse()
                    .getCourseCode()
                    .equalsIgnoreCase(courseCode)) {

                filtered.add(record);
            }
        }

        return filtered;
    }

    /**
     * Calcula promedio general.
     */
    public double calculateAverage() {

        if (records.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (GradeRecord record : records) {

            total += record.calculateFinalGrade();
        }

        return total / records.size();
    }

}