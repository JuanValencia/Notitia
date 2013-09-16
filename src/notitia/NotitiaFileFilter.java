/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package notitia;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Juan
 */
public class NotitiaFileFilter extends FileFilter {

    public NotitiaFileFilter() {
    }

    @Override
    public boolean accept(File f) {
        if (f.getPath().endsWith(".not") || f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Notitia Save Files";
    }

}
