/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Ros
 */
public class ColoredJTable extends JTable{
    //for coloring row
    private Color[] rowColors;
    //for center contents
    private CenterTableCellRenderer renderer;

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
    }
    
    public ColoredJTable( TableModel tm, Color[] rowColors) {
        super(tm);
        this.rowColors = rowColors;
        this.renderer = new CenterTableCellRenderer();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component rendererComponent = super.prepareRenderer(renderer, row, column);

        if (!isCellSelected(row, column)) {
            // If the cell is NOT selected, then calculates the index
            // in the color array and sets the background.
            int index = row % rowColors.length;

            rendererComponent.setBackground(rowColors[index]);
        }

        return rendererComponent;
    }


}
