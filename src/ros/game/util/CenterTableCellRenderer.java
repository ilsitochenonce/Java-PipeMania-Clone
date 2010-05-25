/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.util;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ros
 */
public class CenterTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
