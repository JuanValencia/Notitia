/*
 * Data.java
 *
 * Created on December 4, 2007, 2:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package notitia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Juan Carlos
 */
public class Data {
    
    /** Creates a new instance of Data */
    public Data() {
        m_data = new ArrayList<Entry>(1000);
        totalEntries = 0;
    }
    
    public void loadData(Notitia prog) {
        getDataFromSave(prog);
    }
    
    public void getDataFromSave(Notitia prog) {
        String str;
        String entry;
        try {
            BufferedReader in = new BufferedReader(new FileReader(prog.getCurrentSaveName()));
            while ((str = in.readLine()) != null) {
                entry = "";
                while (!str.equals("#END#")) {
                    entry += str;
                    str = in.readLine();
                    if (str == null) {
                        break;
                    }
                }
                if (entry.startsWith("version")) {
                    //System.err.println(entry);
                    addRawStringEntry(prog, entry);
                } 
            }
            in.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        
    }

    private void addRawStringEntry(Notitia gui, String str) {
        //System.err.println(str);
        String[] split = str.split("#SPLIT#");
        String[] version = split[0].split("-");
        //System.err.println("!" + version[0]);        
        if(Integer.parseInt(version[1]) == 0) {
            if (Integer.parseInt(version[2]) == 5) {
                //System.err.println(str);
                totalEntries++;
                m_data.add(new Entry(totalEntries, "", split[1], split[2]));
                gui.addEntryToGUI(split[1], split[2]);
            } else if (Integer.parseInt(version[2]) == 6) {
                //System.err.println(str);
                totalEntries++;
                m_data.add(new Entry(totalEntries, split[2],split[3], split[4]));
                gui.addEntryToGUI(split[3], split[4]);
                //System.err.println(m_data + "\n\n");
            }
        }
    }

    public void addEntry(String title, String tags, String entry) {
        totalEntries++;
        int ID = totalEntries;
        m_data.add(new Entry(ID, title, tags, entry));
    }

    public void deleteEntry(Entry e) {
        totalEntries--;
        m_data.remove(e);
    }

    public ArrayList<Entry> getEntry(String tag) {
        ArrayList<Entry> answer = new ArrayList<Entry>();
        for (int i = 0; i < m_data.size(); i++) {
            if (m_data.get(i).tagged(tag)) {
                if (!answer.contains(m_data.get(i))) {
                    answer.add(m_data.get(i));
                }
            }
        }
        return answer;
    }

    public ArrayList getEntries(Object[] string) {
        ArrayList<Entry> answer = new ArrayList<Entry>();
        for (int k = 0; k < string.length; k++) {
            String tag = (String)string[k];
            for (int i = 0; i < m_data.size(); i++) {
                if (m_data.get(i).tagged(tag)) {
                    if (!answer.contains(m_data.get(i))) {
                        answer.add(m_data.get(i));
                    }
                }
            }
        }
        return answer;
    }
        
    @Override
    public String toString() {
        String answer = "";
        for (int i = 0; i < m_data.size(); i++) {
            answer += m_data.get(i).toString();
        }
        return answer;        
    }   

    public int getTotalEntries() {
        return totalEntries;
    }
    
    private ArrayList<Entry> m_data;
    private int totalEntries;
    
}
