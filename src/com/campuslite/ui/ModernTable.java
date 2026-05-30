package com.campuslite.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Tabla moderna reutilizable.
 */
public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {

        super(model);

        setRowHeight(28);

        setFont(UIStyles.MAIN_FONT);

        setSelectionBackground(
                UIStyles.PRIMARY_COLOR
        );

        setGridColor(new Color(220, 220, 220));

        /**
         * Desactiva edición de celdas.
         */
        setDefaultEditor(Object.class, null);

        JTableHeader header = getTableHeader();

        header.setBackground(UIStyles.TABLE_HEADER_COLOR);
        header.setForeground(Color.WHITE);

        header.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        14)
        );
    }

}