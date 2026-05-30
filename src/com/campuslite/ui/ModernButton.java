package com.campuslite.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Botón moderno reutilizable.
 */
public class ModernButton extends JButton {

    public ModernButton(String text) {

        super(text);

        setFocusPainted(false);

        setFont(UIStyles.MAIN_FONT);

        setForeground(Color.WHITE);

        setBackground(UIStyles.PRIMARY_COLOR);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(180, 42));
    }

}