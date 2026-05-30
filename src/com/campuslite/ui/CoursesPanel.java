package com.campuslite.ui;

import com.campuslite.domain.Course;
import com.campuslite.logic.CourseManager;
import com.campuslite.logic.EnrollmentManager;
import com.campuslite.logic.ValidationUtils;
import com.campuslite.persistence.CourseCSVRepository;
import com.campuslite.persistence.EnrollmentCSVRepository;
import com.campuslite.persistence.EvaluationCSVRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de cursos.
 */
public class CoursesPanel extends JPanel {

    private final CourseManager courseManager;
    private final EnrollmentManager enrollmentManager;
    private final CourseCSVRepository repository;
    private final EnrollmentCSVRepository enrollmentRepository;
    private final EvaluationCSVRepository evaluationRepository;
    private Runnable onCoursesChanged;

    
    // Componentes UI.
     
    private JTextField txtCode;
    private JTextField txtName;
    private JTextField txtCredits;
    private JTextField txtCapacity;

    private ModernTable table;

    private DefaultTableModel model;

    public CoursesPanel(CourseManager courseManager,EnrollmentManager enrollmentManager) {

        this.courseManager = courseManager;
        this.enrollmentManager = enrollmentManager;
        repository = new CourseCSVRepository();
        enrollmentRepository =
                new EnrollmentCSVRepository();

        evaluationRepository =
                new EvaluationCSVRepository();

        initialize();

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

        topPanel.setLayout(new GridBagLayout());

        topPanel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        /**
         * Campos más grandes.
         */
        txtCode = new JTextField(15);

        txtName = new JTextField(15);

        txtCredits = new JTextField(15);

        txtCapacity = new JTextField(15);

        /**
         * =========================
         * FILA 1 LABELS
         * =========================
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Código"), gbc);

        gbc.gridx = 1;
        topPanel.add(new JLabel("Nombre"), gbc);

        gbc.gridx = 2;
        topPanel.add(new JLabel("Créditos"), gbc);

        gbc.gridx = 3;
        topPanel.add(new JLabel("Cupo"), gbc);

        /**
         * =========================
         * FILA 2 CAMPOS
         * =========================
         */
        gbc.gridy = 1;

        gbc.gridx = 0;
        topPanel.add(txtCode, gbc);

        gbc.gridx = 1;
        topPanel.add(txtName, gbc);

        gbc.gridx = 2;
        topPanel.add(txtCredits, gbc);

        gbc.gridx = 3;
        topPanel.add(txtCapacity, gbc);

        /**
         * =========================
         * PANEL BOTONES
         * =========================
         */
        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        ModernButton btnAdd =
                new ModernButton("➕ Agregar");

        ModernButton btnUpdate =
                new ModernButton("🔄 Actualizar");

        ModernButton btnDelete =
                new ModernButton("❌ Eliminar");

        buttonPanel.add(btnAdd);

        buttonPanel.add(btnUpdate);

        buttonPanel.add(btnDelete);

        /**
         * =========================
         * FILA 3 BOTONES
         * =========================
         */
        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = 4;

        gbc.anchor = GridBagConstraints.CENTER;

        topPanel.add(buttonPanel, gbc);

        /**
         * =========================
         * TABLA
         * =========================
         */
        model = new DefaultTableModel();

        model.setColumnIdentifiers(
                new Object[]{
                        "Código",
                        "Nombre",
                        "Créditos",
                        "Cupo"
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
         * Evento agregar.
         */
        btnAdd.addActionListener(
                e -> addCourse()
        );

        /**
         * Evento actualizar.
         */
        btnUpdate.addActionListener(
                e -> updateCourse()
        );

        /**
         * Evento eliminar.
         */
        btnDelete.addActionListener(
                e -> deleteCourse()
        );

        /**
         * Evento selección tabla.
         */
        table.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedCourse()
                );

        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Agrega curso.
     */
    private void addCourse() {

        try {

            String code = txtCode.getText();

            String name = txtName.getText();

            String creditsText =
                    txtCredits.getText();

            String capacityText =
                    txtCapacity.getText();

            validateFields(
                    code,
                    name,
                    creditsText,
                    capacityText
            );

            Course course =
                    new Course(
                            code,
                            name,
                            Integer.parseInt(creditsText),
                            Integer.parseInt(capacityText)
                    );

            courseManager.addCourse(course);

            repository.saveCourses(
                    courseManager.getCourses()
            );

            refreshTable();
            
            if (onCoursesChanged != null) {

                onCoursesChanged.run();
            }

            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Curso agregado correctamente."
            );

        } catch (Exception ex) {
        	
        	if (onCoursesChanged != null) {
        	    onCoursesChanged.run();
        	}

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza curso.
     */
    private void updateCourse() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un curso."
            );

            return;
        }

        try {

            String originalCode =
                    model.getValueAt(row, 0)
                            .toString();

            /**
             * Nuevos datos desde campos.
             */
            String newCode = originalCode;

            String name =
                    txtName.getText();

            int credits =
                    Integer.parseInt(
                            txtCredits.getText()
                    );

            int capacity =
                    Integer.parseInt(
                            txtCapacity.getText()
                    );

            /**
             * Nuevo objeto actualizado.
             */
            Course updatedCourse =
                    new Course(
                            newCode,
                            name,
                            credits,
                            capacity
                    );

            courseManager.updateCourse(
                    originalCode,
                    updatedCourse
            );

            repository.saveCourses(
                    courseManager.getCourses()
            );

            refreshTable();

            if (onCoursesChanged != null) {

                onCoursesChanged.run();
            }

            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Curso actualizado."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina curso.
     */
    private void deleteCourse() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un curso."
            );

            return;
        }

        String code =
                model.getValueAt(row, 0)
                        .toString();
        enrollmentManager.removeEnrollmentsByCourse(
                code
        );

        courseManager.removeCourse(code);
        /**
         * Reescribe inscripciones.
         */
        enrollmentRepository.saveEnrollments(
                enrollmentManager.getEnrollments()
        );

        /**
         * Reescribe evaluaciones.
         */
        evaluationRepository.saveEvaluations(
                enrollmentManager.getEnrollments()
        );

        repository.saveCourses(
                courseManager.getCourses()
        );

        refreshTable();
        
        if (onCoursesChanged != null) {

            onCoursesChanged.run();
        }

        clearFields();

        JOptionPane.showMessageDialog(
                this,
                "Curso eliminado."
        );
    }

    /**
     * Carga curso seleccionado.
     */
    private void loadSelectedCourse() {

        int row = table.getSelectedRow();

        if (row != -1) {
        	
        	/**
             * Bloquear edición de carnet
             * al editar un estudiante.
             */
            txtCode.setEditable(false);

            txtCode.setText(
                    model.getValueAt(row, 0)
                            .toString()
            );

            txtName.setText(
                    model.getValueAt(row, 1)
                            .toString()
            );

            txtCredits.setText(
                    model.getValueAt(row, 2)
                            .toString()
            );

            txtCapacity.setText(
                    model.getValueAt(row, 3)
                            .toString()
            );
        }
    }

    /**
     * Refresca tabla.
     */
    private void refreshTable() {

        model.setRowCount(0);

        for (Course course :
                courseManager.getCourses()) {

            model.addRow(
                    new Object[]{
                            course.getCourseCode(),
                            course.getName(),
                            course.getCredits(),
                            course.getCapacity()
                    }
            );
        }
    }

    /**
     * Limpia campos.
     */
    private void clearFields() {

        txtCode.setText("");

        txtName.setText("");

        txtCredits.setText("");

        txtCapacity.setText("");
        
        /**
         * Volver a habilitar carnet
         * para nuevos registros.
         */
        txtCode.setEditable(true);
    }

    /**
     * Valida campos.
     */
    private void validateFields(String code,
                                String name,
                                String credits,
                                String capacity) {

        if (ValidationUtils.isEmpty(code)
                || ValidationUtils.isEmpty(name)
                || ValidationUtils.isEmpty(credits)
                || ValidationUtils.isEmpty(capacity)) {

            throw new IllegalArgumentException(
                    "Todos los campos son obligatorios."
            );
        }

        if (!ValidationUtils.isInteger(credits)) {

            throw new IllegalArgumentException(
                    "Los créditos deben ser numéricos."
            );
        }

        if (!ValidationUtils.isInteger(capacity)) {

            throw new IllegalArgumentException(
                    "El cupo debe ser numérico."
            );
        }
    }

    
    /**
     * Evento cuando cambian cursos.
     */
    public void setOnCoursesChanged(
            Runnable onCoursesChanged) {

        this.onCoursesChanged =
                onCoursesChanged;
    }

}