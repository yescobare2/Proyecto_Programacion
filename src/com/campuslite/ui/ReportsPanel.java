package com.campuslite.ui;

import com.campuslite.domain.Enrollment;
import com.campuslite.domain.Student;
import com.campuslite.logic.EnrollmentManager;
import com.campuslite.logic.StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de reportes.
 */
public class ReportsPanel extends JPanel {

    private final StudentManager studentManager;

    private final EnrollmentManager enrollmentManager;

    /**
     * Componentes UI.
     */
    private JComboBox<Student> cmbStudents;

    private JLabel lblAverage;

    private JLabel lblStatus;

    private ModernTable table;

    private DefaultTableModel model;

    public ReportsPanel(StudentManager studentManager,
                        EnrollmentManager enrollmentManager) {


        this.studentManager = studentManager;

        this.enrollmentManager =
                enrollmentManager;

        initialize();

        loadStudents();

        refreshTable();
    }

    /**
     * Inicializa interfaz.
     */
    private void initialize() {

        setLayout(new BorderLayout());

        setBackground(UIStyles.BACKGROUND_COLOR);

        setBorder(UIStyles.createPadding());

        /**
         * =========================
         * PANEL SUPERIOR
         * =========================
         */
        JPanel topPanel = new JPanel();

        topPanel.setLayout(
                new GridLayout(2, 2, 10, 10)
        );

        topPanel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        /**
         * Combo estudiantes.
         */
        cmbStudents = new JComboBox<>();

        /**
         * Labels de reporte.
         */
        lblAverage =
                new JLabel("Promedio general: 0");

        lblStatus =
                new JLabel("Estado: Sin datos");

        topPanel.add(
                new JLabel("Estudiante")
        );

        topPanel.add(cmbStudents);

        topPanel.add(lblAverage);

        topPanel.add(lblStatus);

        /**
         * =========================
         * TABLA
         * =========================
         */
        model = new DefaultTableModel();

        model.setColumnIdentifiers(
                new Object[]{
                		"Inscripción",
                        "Curso",
                        "Promedio",
                        "Estado"
                }
        );

        table = new ModernTable(model);

        JScrollPane scrollPane =
                new JScrollPane(table);

        /**
         * =========================
         * EVENTOS
         * =========================
         */

        /**
         * Actualiza reporte al cambiar estudiante.
         */
        cmbStudents.addActionListener(
                e -> refreshTable()
        );

        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refresca tabla y cálculos.
     */
    private void refreshTable() {

        model.setRowCount(0);

        Student student =
                (Student) cmbStudents
                        .getSelectedItem();

        if (student == null) {

            lblAverage.setText(
                    "Promedio general: 0"
            );

            lblStatus.setText(
                    "Estado: Sin datos"
            );

            return;
        }

        double totalAverage = 0;

        int totalCourses = 0;

        /**
         * Cursos inscritos del estudiante.
         */
        for (Enrollment enrollment :
            enrollmentManager
                    .getEnrollments()) {

        if (!enrollment.getStudent()
                .getStudentCode()
                .equals(
                        student.getStudentCode()
                )) {

            continue;
        }

        double average =
                enrollment.calculateAverage();

        String status =
                average >= 61
                        ? "Aprobado"
                        : "Reprobado";

        totalAverage += average;

        totalCourses++;

        model.addRow(
                new Object[]{

                        enrollment
                                .getEnrollmentCode(),

                        enrollment
                                .getCourse()
                                .getName(),

                        String.format(
                                "%.2f",
                                average
                        ),

                        status
                }
        );
    }

        /**
         * Promedio general.
         */
        double finalAverage =
                totalCourses > 0
                        ? totalAverage / totalCourses
                        : 0;

        lblAverage.setText(
                "Promedio general: "
                        + String.format(
                        "%.2f",
                        finalAverage
                )
        );

        lblStatus.setText(
                finalAverage >= 61
                        ? "Estado: Aprobado"
                        : "Estado: Reprobado"
        );
    }

    /**
     * Carga estudiantes.
     */
    private void loadStudents() {

        cmbStudents.removeAllItems();

        for (Student student :
                studentManager.getStudents()) {

            cmbStudents.addItem(student);
        }
    }

    /**
     * Recarga estudiantes.
     */
    public void reloadStudents() {

        loadStudents();

        refreshTable();
    }

}