package com.campuslite.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Estilos globales de la aplicación.
 */
public class UIStyles {

    /**
     * Color principal.
     */
    public static final Color PRIMARY_COLOR =
            new Color(45, 125, 210);

    /**
     * Color secundario.
     */
    public static final Color BACKGROUND_COLOR =
            new Color(245, 247, 250);

    /**
     * Color de tablas.
     */
    public static final Color TABLE_HEADER_COLOR =
            new Color(30, 30, 30);

    /**
     * Fuente principal.
     */
    public static final Font MAIN_FONT =
            new Font("SansSerif", Font.BOLD, 15);

    /**
     * Fuente para títulos.
     */
    public static final Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 20);
    /**
     * Aplica estilos globales.
     */
    public static void applyGlobalStyle() {

        UIManager.put("Label.font", MAIN_FONT);

        UIManager.put("Button.font", MAIN_FONT);

        UIManager.put("TextField.font", MAIN_FONT);

        UIManager.put("ComboBox.font", MAIN_FONT);

        UIManager.put("Table.font", MAIN_FONT);

        UIManager.put(
                "TableHeader.font",
                new Font("SansSerif",
                        Font.BOLD,
                        14)
        );
    }

    /**
     * Padding reutilizable.
     */
    public static EmptyBorder createPadding() {

        return new EmptyBorder(
                10,
                10,
                10,
                10
        );
    }

    /**
     * Configura panel moderno.
     */
    public static void stylePanel(JPanel panel) {

        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    /**
     * Configura títulos.
     */
    public static void styleTitle(JLabel label) {

        label.setFont(TITLE_FONT);
    }

}