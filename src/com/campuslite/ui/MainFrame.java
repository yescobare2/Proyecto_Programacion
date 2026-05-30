package com.campuslite.ui;

import com.campuslite.logic.CourseManager;
import com.campuslite.logic.EnrollmentManager;
import com.campuslite.logic.StudentManager;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema.
 */
public class MainFrame extends JFrame {

    /**
     * Managers globales.
     */
    private final StudentManager studentManager;

    private final CourseManager courseManager;

    private final EnrollmentManager enrollmentManager;
    
    /**
     * Navegación entre pantallas.
     */
    private CardLayout cardLayout;

    private JPanel mainPanel;
    
    private StudentsPanel studentsPanel;

    private CoursesPanel coursesPanel;

    private EvaluationsPanel evaluationsPanel;

    private EnrollmentsPanel enrollmentsPanel;

    private ReportsPanel reportsPanel;


    /**
     * Constructor principal.
     */
    public MainFrame(StudentManager studentManager,
                     CourseManager courseManager,
                     EnrollmentManager enrollmentManager) {

        this.studentManager = studentManager;

        this.courseManager = courseManager;

        this.enrollmentManager = enrollmentManager;

        initialize();
    }

    /**
     * Inicializa ventana.
     */
    private void initialize() {

        setTitle("Campus Lite");

        setSize(1000, 650);
        
        setMinimumSize(
                new Dimension(950, 600)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Layout principal.
         */
        cardLayout = new CardLayout();

        mainPanel = new JPanel(cardLayout);

        /**
         * =========================
         * PANELES
         * =========================
         */
        JPanel homePanel =
                createHomePanel();

        JPanel studentsScreen =
                createStudentsPanel();

        JPanel academicScreen =
                createAcademicPanel();

        JPanel enrollmentsScreen =
                createEnrollmentsPanel();

        JPanel reportsScreen =
                createReportsPanel();


        /**
         * Cambios estudiantes.
         */
        studentsPanel.setOnStudentsChanged(() -> {

            enrollmentsPanel.reloadData();

            evaluationsPanel.reloadData();

            reportsPanel.reloadStudents();
        });

        /**
         * Cambios cursos.
         */
        coursesPanel.setOnCoursesChanged(() -> {

            enrollmentsPanel.reloadData();

            evaluationsPanel.reloadData();
        });
        
        /**
         * Cambios inscripciones.
         */
        enrollmentsPanel.setOnEnrollmentsChanged(() -> {

            evaluationsPanel.reloadData();

            reportsPanel.reloadStudents();
        });
        
        mainPanel.add(homePanel, "HOME");

        mainPanel.add(studentsScreen, "STUDENTS");

        mainPanel.add(academicScreen, "ACADEMIC");

        mainPanel.add(enrollmentsScreen, "ENROLLMENTS");

        mainPanel.add(reportsScreen, "REPORTS");

        add(mainPanel);

        /**
         * Mostrar menú principal.
         */
        cardLayout.show(mainPanel, "HOME");
    }

    /**
     * =========================
     * MENÚ PRINCIPAL
     * =========================
     */
    private JPanel createHomePanel() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        /**
         * =========================
         * TÍTULO
         * =========================
         */
        JPanel titlePanel =
                new JPanel(new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        20
                ));

        titlePanel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        /**
         * Logo.
         */
        ImageIcon originalIcon =
                new ImageIcon(
                        MainFrame.class.getResource(
                                "logo.png"
                        )
                );

        Image scaledImage =
                originalIcon.getImage()
                        .getScaledInstance(
                                64,
                                64,
                                Image.SCALE_SMOOTH
                        );

        ImageIcon logoIcon =
                new ImageIcon(scaledImage);
        
        JLabel logoLabel =
                new JLabel(logoIcon);

        /**
         * Texto título.
         */
        JLabel title =
                new JLabel("Campus Lite");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        titlePanel.add(logoLabel);

        titlePanel.add(title);

        panel.add(titlePanel, BorderLayout.NORTH);


        /**
         * =========================
         * PANEL CENTRAL
         * =========================
         */
        JPanel buttonsPanel =
                new JPanel();

        buttonsPanel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        buttonsPanel.setLayout(
                new BoxLayout(
                        buttonsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        buttonsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        320,
                        30,
                        320
                )
        );

        /**
         * =========================
         * BOTONES
         * =========================
         */
        ModernButton btnStudents =
                new ModernButton(
                        "👨‍🎓   Estudiantes"
                );

        ModernButton btnAcademic =
                new ModernButton(
                        "📚   Cursos/Evaluaciones"
                );

        ModernButton btnEnrollments =
                new ModernButton(
                        "📝   Inscripciones"
                );

        ModernButton btnReports =
                new ModernButton(
                        "📊   Reportes"
                );

        /**
         * Tamaño uniforme.
         */
        Dimension buttonSize =
                new Dimension(350, 45);

        btnStudents.setMaximumSize(buttonSize);

        btnAcademic.setMaximumSize(buttonSize);

        btnEnrollments.setMaximumSize(buttonSize);

        btnReports.setMaximumSize(buttonSize);
        
        btnStudents.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAcademic.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEnrollments.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnReports.setAlignmentX(Component.CENTER_ALIGNMENT);

        /**
         * =========================
         * EVENTOS
         * =========================
         */
        btnStudents.addActionListener(
                e -> cardLayout.show(
                        mainPanel,
                        "STUDENTS"
                )
        );

        btnAcademic.addActionListener(
                e -> cardLayout.show(
                        mainPanel,
                        "ACADEMIC"
                )
        );

        btnEnrollments.addActionListener(
                e -> cardLayout.show(
                        mainPanel,
                        "ENROLLMENTS"
                )
        );

        btnReports.addActionListener(
                e -> cardLayout.show(
                        mainPanel,
                        "REPORTS"
                )
        );

        /**
         * =========================
         * AGREGAR COMPONENTES
         * =========================
         */
        buttonsPanel.add(btnStudents);

        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(btnAcademic);

        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(btnEnrollments);

        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(btnReports);

        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * =========================
     * ESTUDIANTES
     * =========================
     */
    private JPanel createStudentsPanel() {

    	studentsPanel =
        		new StudentsPanel(
        		        studentManager,
        		        enrollmentManager
        		);

        JPanel container =
                createContainerPanel(
                        "Gestión de Estudiantes",
                        studentsPanel
                );

        return container;
    }

    /**
     * =========================
     * CURSOS + EVALUACIONES
     * =========================
     */
    private JPanel createAcademicPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        /**
         * Título.
         */
        JLabel title =
                new JLabel(
                        "Cursos / Evaluaciones"
                );

        title.setFont(UIStyles.TITLE_FONT);

        title.setBorder(
                UIStyles.createPadding()
        );

        panel.add(title, BorderLayout.NORTH);

        /**
         * Tabs.
         */
        JTabbedPane tabbedPane =
                new JTabbedPane();

        coursesPanel  =
        		new CoursesPanel(
        		        courseManager,
        		        enrollmentManager
        		);

        evaluationsPanel =
                new EvaluationsPanel(enrollmentManager);

        /**
         * Actualización tiempo real.
         */
        coursesPanel.setOnCoursesChanged(() -> {

            enrollmentsPanel.reloadData();

            evaluationsPanel.reloadData();
        });

        tabbedPane.addTab(
                "Cursos",
                coursesPanel
        );

        tabbedPane.addTab(
                "Evaluaciones",
                evaluationsPanel
        );

        panel.add(tabbedPane, BorderLayout.CENTER);

        /**
         * Botón volver.
         */
        panel.add(
                createBackPanel(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    /**
     * =========================
     * INSCRIPCIONES
     * =========================
     */
    private JPanel createEnrollmentsPanel() {

    	enrollmentsPanel  =
                new EnrollmentsPanel(
                        studentManager,
                        courseManager,
                        enrollmentManager
                );

        return createContainerPanel(
                "Inscripciones",
                enrollmentsPanel
        );
    }

    /**
     * =========================
     * REPORTES
     * =========================
     */
    private JPanel createReportsPanel() {

    	reportsPanel =
                new ReportsPanel(
                        studentManager,
                        enrollmentManager
                );

        return createContainerPanel(
                "Reportes",
                reportsPanel
        );
    }

    /**
     * =========================
     * PANEL CONTENEDOR
     * =========================
     */
    private JPanel createContainerPanel(
            String titleText,
            JPanel content
    ) {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        /**
         * Título.
         */
        JLabel title =
                new JLabel(titleText);

        title.setFont(UIStyles.TITLE_FONT);

        title.setBorder(
                UIStyles.createPadding()
        );

        panel.add(title, BorderLayout.NORTH);

        /**
         * Contenido.
         */
        panel.add(content, BorderLayout.CENTER);

        /**
         * Botón volver.
         */
        panel.add(
                createBackPanel(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    /**
     * =========================
     * PANEL BOTÓN VOLVER
     * =========================
     */
    private JPanel createBackPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setBackground(
                UIStyles.BACKGROUND_COLOR
        );

        ModernButton btnBack =
                new ModernButton("◀ Volver");

        btnBack.addActionListener(
                e -> cardLayout.show(
                        mainPanel,
                        "HOME"
                )
        );

        panel.add(btnBack);

        return panel;
    }
}