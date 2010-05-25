/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ros.game.database;

/**
 *
 * @author Ros
 */
public class Test {

    DatabaseManager db;
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        db = new DatabaseManager();
        db.printTop15();
    }

}
