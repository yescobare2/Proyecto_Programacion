package com.campuslite.main;

import com.campuslite.logic.CourseManager;
import com.campuslite.logic.StudentManager;
import com.campuslite.logic.EnrollmentManager;
import com.campuslite.persistence.CourseCSVRepository;
import com.campuslite.persistence.EvaluationCSVRepository;
import com.campuslite.persistence.StudentCSVRepository;
import com.campuslite.persistence.EnrollmentCSVRepository;
import com.campuslite.ui.MainFrame;
import com.campuslite.ui.UIStyles;

import javax.swing.*;

/**
 * Punto de entrada principal.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            /**
             * Estilos globales.
             */
            UIStyles.applyGlobalStyle();

            /**
             * Managers.
             */
            StudentManager studentManager =
                    new StudentManager();

            CourseManager courseManager =
                    new CourseManager();
            
            EnrollmentManager enrollmentManager =
                    new EnrollmentManager();

            /**
             * Repositorios CSV.
             */
            StudentCSVRepository studentRepository =
                    new StudentCSVRepository();

            CourseCSVRepository courseRepository =
                    new CourseCSVRepository();

            EvaluationCSVRepository evaluationRepository =
                    new EvaluationCSVRepository();
            
            EnrollmentCSVRepository enrollmentRepository =
                    new EnrollmentCSVRepository();

            /**
             * Carga estudiantes.
             */
            studentManager.getStudents().addAll(
                    studentRepository.loadStudents()
            );

            /**
             * Carga cursos.
             */
            courseManager.getCourses().addAll(
                    courseRepository.loadCourses()
            );

            /**
             * Carga inscripciones.
             */
            enrollmentRepository.loadEnrollments(
                    enrollmentManager,
                    studentManager.getStudents(),
                    courseManager.getCourses()
            );

            /**
             * Carga evaluaciones.
             */
            evaluationRepository.loadEvaluations(
                    enrollmentManager.getEnrollments()
            );

            /**
             * Ventana principal.
             */
            MainFrame frame =
                    new MainFrame(
                            studentManager,
                            courseManager,
                            enrollmentManager
                    );

            frame.setVisible(true);
        });
    }

}