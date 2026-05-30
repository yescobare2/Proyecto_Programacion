package com.campuslite.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Relación entre estudiante y curso.
 * Guarda las evaluaciones del estudiante.
 */
public class GradeRecord {

    private Student student;
    private Course course;

    /**
     * Lista polimórfica.
     */
    private List<Evaluation> evaluations;

    public GradeRecord(Student student, Course course) {

        setStudent(student);
        setCourse(course);

        evaluations = new ArrayList<>();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo.");
        }

        this.course = course;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    /**
     * Agrega una evaluación.
     */
    public void addEvaluation(Evaluation evaluation) {

        if (evaluation == null) {
            throw new IllegalArgumentException("La evaluación no puede ser nula.");
        }

        evaluations.add(evaluation);
    }

    /**
     * Calcula el promedio ponderado final.
     */
    public double calculateFinalGrade() {

        double total = 0;

        for (Evaluation evaluation : evaluations) {

            total += evaluation.calculateContribution();
        }

        return total;
    }

    /**
     * Estado del estudiante.
     */
    public String getStatus() {

        return calculateFinalGrade() >= 61
                ? "Aprobado"
                : "Reprobado";
    }

}