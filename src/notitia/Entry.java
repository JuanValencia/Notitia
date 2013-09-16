/*
 * Entry.java
 *
 * Created on December 4, 2007, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 * All internal variables to this class have a "m_" prefix for clarity
 */

package notitia;

/**
 *
 * @author Juan Carlos
 */
public class Entry {
    
    private class Version {
        public int m_major;
        public int m_minor;
        public Version(int major, int minor) {
            m_major = major;
            m_minor = minor;
        }
    }
    
    /** Creates a new instance of Entry */
    public Entry(int ID, String title, String tags, String entry) {
        m_version = new Version(0, 6);
        m_ID = ID;
        m_title = title;
        m_tags = tags;
        m_entry = entry;
    }
    
    public boolean tagged(String tag) {
        String[] splitTags = m_tags.split(",");
        for (int i = 0; i<splitTags.length; i++) {
            if (splitTags[i].trim().equals(tag)) {
                return true;
            }
        }
        return false;
        //return m_tags.contains(tag);
    }
    
    public String getEntryString() {
        return m_entry;
    }
    public String getTags() {
        return m_tags;
    }
    public String getTitle() {
        return m_title;
    }
    public int getID() {
        return m_ID;
    }

    @Override
    public String toString() {
        //return "version-"+m_version.m_major+"-"+m_version.m_minor+"#SPLIT#"+ m_tags + "#SPLIT#" + m_entry +" \n#END#\n";
        return "version-"+m_version.m_major+"-"+m_version.m_minor+ "#SPLIT#"+ m_ID + "#SPLIT#"+ m_title +"#SPLIT#"+ m_tags + "#SPLIT#" + m_entry +" \n#END#\n";
    }

    public String format(int formatWidth) {
        String entry;
        entry = "<html><body><table width=" + (formatWidth - 30) + ">";
        //entry += "[<b>Tags:</b> <i>" + entries.get(i).getTags() + "</i>]<br />";
        entry += "<br />" + getEntryString();
        entry += "<br /><font size=3>[<b>Tags:</b> <i>" + getTags() + "</i>] (";
        entry += getID() + ")</font>";
        entry += "<br /></table></body></html>";
        return entry;
    }
    
    private Version m_version;
    private int m_ID;
    private String m_title;
    private String m_tags;
    private String m_entry;
    
}
