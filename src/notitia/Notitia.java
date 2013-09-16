/*
 * Notitia.java
 *
 * Created on November 30, 2007, 3:47 PM
 */

package notitia;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  Juan Carlos
 */
public class Notitia extends javax.swing.JFrame implements ActionListener, KeyListener {

    /** Creates new form Notitia */
    public Notitia() {
        
        initComponents();
        fileChooser = new JFileChooser();
        initFiles();
        renewData();
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }            
            @Override
            public void windowClosing(WindowEvent e) {
                if (prompt("Do you want to save the database?", "Save")) {
                    save();
                }
                System.exit(0);
            }            
            @Override
            public void windowClosed(WindowEvent e) {
            }
            @Override
            public void windowIconified(WindowEvent e) {
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            @Override
            public void windowActivated(WindowEvent e) {
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
            }});
        
    }

    private void renewData() {
        m_tags = new Vector();
        m_tagsMaster = new Vector();
        m_data = new Data();
        m_data.loadData(this);
        currentEntries = m_data.getEntries(tags.getSelectedValues());
        tags.setListData(m_tags);
    }

    private void initComponents() {

        this.setFont(new Font("Serif", Font.PLAIN, 20));

        splitPane = new javax.swing.JSplitPane();
        addTagField = new javax.swing.JTextField("", 200);
        addTagField.addKeyListener(this);
        addTitleField = new javax.swing.JTextField("", 200);
        addTitleField.addKeyListener(this);
        addTextPane = new javax.swing.JTextPane();
        addTextPane.addKeyListener(this);

        navigation = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_entries = new Vector();
        m_tags = new Vector();
        m_tagsMaster = new Vector();
        tags = new javax.swing.JList(m_tags);
        entryList = new javax.swing.JList(m_entries);
        notebooks = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        //mainInfo = new javax.swing.JTextPane();
        //mainDocument = mainInfo.getStyledDocument();
        menuBar = new javax.swing.JMenuBar();
        popup = new javax.swing.JPopupMenu();
        menu = new javax.swing.JMenu();
        edit = new javax.swing.JMenu();
        data = new javax.swing.JMenu();
        about = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Notitia");
        setBackground(new java.awt.Color(240, 240, 240));
        splitPane.setDividerLocation(180);
        splitPane.setAutoscrolls(true);
        splitPane.setOneTouchExpandable(true);
        
        navigation.setName("tags");
        tags.setName("Tags");
        tags.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                tagsValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(tags);
        tags.getAccessibleContext().setAccessibleName("Tags");
        tags.setFont(new Font("Serif", Font.PLAIN, 14));

        navigation.addTab("tab1", jScrollPane1);

        splitPane.setLeftComponent(navigation);
        navigation.getAccessibleContext().setAccessibleName("Navigation");
        navigation.setTitleAt(0, "Tags");

        entryRenderer = new CustomEntryRenderer();
        entryList.setCellRenderer(entryRenderer);
        entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(entryList);
        jScrollPane2.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                drawEntries(currentEntries);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        entryList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!entryList.isSelectionEmpty() && !e.getValueIsAdjusting()) {
                    addTitleField.setText(((Entry)entryList.getSelectedValue()).getTitle());
                    addTagField.setText(((Entry)entryList.getSelectedValue()).getTags());
                    addTextPane.setText(((Entry)entryList.getSelectedValue()).getEntryString());
                    addButton.setEnabled(true);
                    entrySelected = (Entry)entryList.getSelectedValue();
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else if (entryList.isSelectionEmpty() && !e.getValueIsAdjusting()) {
                    clear();
                }
            }
        });

        notebooks.addTab("Tab 1", jScrollPane2);
        
        splitPane.setRightComponent(notebooks);        
        
        jScrollPane3.setViewportView(addTextPane);

        JMenuItem toAdd = new JMenuItem("New Database");
        toAdd.addActionListener(this);
        
        menu.setText("Menu");
        menu.setMnemonic('M');
        menu.add(toAdd);
        toAdd = new JMenuItem("Save");
        toAdd.addActionListener(this);
        menu.add(toAdd);
        toAdd = new JMenuItem("Save As");
        toAdd.addActionListener(this);
        menu.add(toAdd);
        toAdd = new JMenuItem("Load");
        toAdd.addActionListener(this);
        menu.add(toAdd);
        toAdd = new JMenuItem("Exit");
        toAdd.addActionListener(this);
        menu.add(toAdd);
        menuBar.add(menu);
                
        toAdd = new JMenuItem("Modify");
        toAdd.addActionListener(this);
        popup.add(toAdd);
        toAdd = new JMenuItem("Delete");
        toAdd.addActionListener(this);
        popup.add(toAdd);
                
        data.setText("Data");
        data.setMnemonic('D');
        menuBar.add(data);
        
        about.setText("About");
        about.setMnemonic('A');
        toAdd = new JMenuItem("Help");
        toAdd.addActionListener(this);
        about.add(toAdd);
        toAdd = new JMenuItem("About");
        toAdd.addActionListener(this);
        about.add(toAdd);
        menuBar.add(about);

        setJMenuBar(menuBar);
        
        JLabel titleLabel = new JLabel("Title:");
        JLabel tagsLabel = new JLabel("Tags:");
        
        addButton = new JButton("Add Entry");
        updateButton = new JButton("Update Entry");
        deleteButton = new JButton("Delete Entry");
        clearButton = new JButton("Clear/Cancel");

        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        addButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            //.addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    //.addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(true, addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTitleField, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tagsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagField, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            //.addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel)
                    .addComponent(addTitleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagsLabel)
                    .addComponent(addTagField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                //.addComponent(addButton)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateButton)
                    .addComponent(deleteButton)
                    .addComponent(clearButton))
                .addContainerGap())
        );
                
        pack();
        
    }
    
    private void initFiles() {
        String str;
        try {
            BufferedReader in = new BufferedReader(new FileReader("saveinfo/notitia.cfg"));
            if ((str = in.readLine()) != null) {
                currentSaveName = str;
                System.err.println("Got current name from save");
            } else {
                currentSaveName = "saveinfo/data.not";
                System.err.println("Got current name from null");
            }
            in.close();
        } catch (Exception e) {
            currentSaveName = "saveinfo/data.not";
            System.err.println("Got current name from nofile:" +e.toString());
        }
        
        fileChooser.setCurrentDirectory(new File("saveinfo/"));
        fileChooser.setFileFilter(new NotitiaFileFilter());
    }

    private void tagsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            getEntries(tags.getSelectedValues());
        }
    }

    private void getEntries(Object[] selectedTags) {
        if (selectedTags == null) {
            return;
        }
        currentEntries = m_data.getEntries(selectedTags);
        drawEntries(currentEntries);
    }

    private void drawEntries(ArrayList<Entry> entries) {
        entryList.setListData(entries.toArray());
        Collections.sort(m_entries);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.err.println(e.toString());
        if (e.getActionCommand().equals("Exit")) {
            if (prompt("Do you want to save the database?", "Save")) {
                save();
            }
            System.exit(0);
        } else if (e.getActionCommand().equals("Save")) {
            save();
        } else if (e.getActionCommand().equals("Save As")) {
            saveAs();
        } else if (e.getActionCommand().equals("Help")) {
            
        } else if (e.getActionCommand().equals("New Database")) {
            newDatabase();
        } else if (e.getActionCommand().equals("Load")) {
            load();
        } else if (e.getActionCommand().equals("About")) {
            about();
        } else if (e.getActionCommand().equals("Add Entry")) {
            addFromForm();
        } else if (e.getActionCommand().equals("Update Entry")) {
            update();
        } else if (e.getActionCommand().equals("Delete Entry")) {
            delete();
        } else if (e.getActionCommand().equals("Clear/Cancel")) {
            clear();
        } else {
            System.err.println(e.toString());
        }
    }
    
    private boolean prompt(String string, String command) {
        int answer = JOptionPane.showOptionDialog(this, string, command, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (answer == JOptionPane.YES_OPTION) {
            return true;
        } else if (answer == JOptionPane.NO_OPTION) {
            return false;
        }
        return false;
    }

    private void about() {
        JOptionPane.showMessageDialog(this, "Notitia\nOpen Source: MIT Licence 2009\nAuthor: Juan Valencia\nTotal Entries: " + m_data.getTotalEntries());
    }

    private void newDatabase() {
        if (prompt("Do you want to save the current database?", "Save")) {
            save();
        }
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.CANCEL_OPTION) {
            System.err.println("Cancel New");
        } else if (returnValue == JFileChooser.APPROVE_OPTION) {
            
            System.err.println("Approve New");
            System.err.println(fileChooser.getSelectedFile().toString());
            if (fileChooser.getSelectedFile().exists() ||
                    new File(fileChooser.getSelectedFile().toString() + ".not").exists()) {
                if (prompt("This file already exists, do you want to open it?", "Open")) {
                    if (fileChooser.getSelectedFile().toString().endsWith(".not")) {
                        currentSaveName = fileChooser.getSelectedFile().toString();
                    } else {
                        currentSaveName = fileChooser.getSelectedFile().toString() + ".not";
                    }
                    renewData();
                    System.err.println(m_data.toString());
                }
            } else {
                if (fileChooser.getSelectedFile().toString().endsWith(".not")) {
                    currentSaveName = fileChooser.getSelectedFile().toString();
                } else {
                    currentSaveName = fileChooser.getSelectedFile().toString() + ".not";
                }
                renewData();
                System.err.println(m_data.toString());
            }
            System.err.println(currentSaveName);
        } else if (returnValue == JFileChooser.ERROR_OPTION) {
            System.err.println("Error New DB");
        }
    }

    private void save() {
        try {
            FileOutputStream save = new FileOutputStream(currentSaveName);
            PrintStream p = new PrintStream( save );            
            p.println(m_data);
            p.close();
            
            save = new FileOutputStream("saveinfo/notitia.cfg");
            p = new PrintStream( save );            
            p.println(currentSaveName);
            p.close();
        } catch(Exception e) {
            System.err.println("Error writing to file:" + e.toString());
        }
    }

    private void saveAs() {
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.CANCEL_OPTION) {
            System.err.println("Cancel Save");
        } else if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.err.println("Approve Save");
            System.err.println(fileChooser.getSelectedFile().toString());
            if (fileChooser.getSelectedFile().toString().endsWith(".not")) {
                currentSaveName = fileChooser.getSelectedFile().toString();
            } else {
                currentSaveName = fileChooser.getSelectedFile().toString() + ".not";
            }
        } else if (returnValue == JFileChooser.ERROR_OPTION) {
            System.err.println("Error Save");
        }

        save();
    }

    private void load() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.CANCEL_OPTION) {
            System.err.println("Cancel Load");
        } else if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (prompt("Do you want to save the current database?", "Save")) {
                save();
            }
            System.err.println("Approve Load");
            System.err.println(fileChooser.getSelectedFile().toString());
            currentSaveName = fileChooser.getSelectedFile().toString();
            renewData();
        } else if (returnValue == JFileChooser.ERROR_OPTION) {
            System.err.println("Error Load");
        }
    }

    private void addFromForm() {
        String tagsString = addTagField.getText();
        String entryString = addTextPane.getText();
        String titleString = addTitleField.getText();
        if (titleString == null || tagsString == null || entryString == null || tagsString.length() < 1 || entryString.length() < 1 || titleString.length() < 1) {
            JOptionPane.showMessageDialog(this, "Please fill out the title, tag, and entry fields!");
        } else {
            m_data.addEntry(titleString, tagsString, entryString);
            addEntryToGUI(tagsString, entryString);
            clear();
        }
    }
    
    // addEntry adds the entry to the gui
    public void addEntryToGUI(String tags, String entry) {
        String[] split = tags.split(",");
        for (int i = 0; i < split.length; i++) {
            if (!m_tags.contains(split[i].trim())) {
                m_tags.add(split[i].trim());
            }
            m_tagsMaster.add(split[i].trim());
        }
        Collections.sort(m_tags);
        this.tags.setListData(m_tags);
    }

    private void delete() {
        if (prompt("Are you sure you want to delete this entry?", "Del")) {
            reallyDelete();
        }
    }

    private void update() {
        if (prompt("Are you sure you want to update the entry?", "Upd")) {
            String tagsString = addTagField.getText();
            String entryString = addTextPane.getText();
            String titleString = addTitleField.getText();
            if (titleString == null || tagsString == null || entryString == null || tagsString.length() < 1 || entryString.length() < 1 || titleString.length() < 1) {
                JOptionPane.showMessageDialog(this, "Please fill out the title, tag, and entry fields!");
            } else {
                Object[] selectedTags = this.tags.getSelectedValues();
                reallyDelete();
                m_data.addEntry(titleString, tagsString, entryString);
                addEntryToGUI(tagsString, entryString);
                validateSelectedTags(selectedTags);
            }
        }
    }

    private void reallyDelete() {
        System.err.println("~~~" + entrySelected.getTitle());
        m_data.deleteEntry(entrySelected);
        String tags_forThis = entrySelected.getTags();
        String[] tagsToRemove = tags_forThis.split(",");
        String removingTag;
        Object[] selectedTags = this.tags.getSelectedValues();
        for (int i = 0; i < tagsToRemove.length; i++) {
            removingTag = tagsToRemove[i].trim();
            m_tagsMaster.remove(removingTag);
            if (m_tagsMaster.contains(removingTag)) {
            } else {
                m_tags.remove(removingTag);
                this.tags.validate();
            }
        }
        validateSelectedTags(selectedTags);
    }

    private void validateSelectedTags(Object[] selectedTags) {
        int[] indexes = new int[selectedTags.length];
        int total = 0;
        for (int m = 0; m < m_tags.size(); m++) {
            for (int n = 0; n < selectedTags.length; n++) {
                if (m_tags.get(m).equals(selectedTags[n])) {
                    indexes[total] = m;
                    total++;
                }
            }
        }
        for (int i=total;i<indexes.length;i++) {
            indexes[i] = -1;
        }
        this.tags.clearSelection();
        this.tags.setSelectedIndices(indexes);
        getEntries(this.tags.getSelectedValues());
        clear();
    }

    private void clear() {
        addTitleField.setText("");
        addTagField.setText("");
        addTextPane.setText("");
        entrySelected = null;
        entryList.clearSelection();
        addButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (isControlPressed == true) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                addFromForm();
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            //System.err.println("ctr");
            isControlPressed = true;
        }
        if (isControlPressed == true) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                addFromForm();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            //System.err.println("way");
            isControlPressed = false;
        }
        String tagsString = addTagField.getText();
        String entryString = addTextPane.getText();
        String titleString = addTitleField.getText();
        if (titleString != null && tagsString != null && entryString != null && tagsString.length() > 0 && entryString.length() > 0 && titleString.length() > 0) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public String getCurrentSaveName() {
        return currentSaveName;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Notitia().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList entryList;
    private javax.swing.JMenu menu;
    private javax.swing.JMenu edit;
    private javax.swing.JMenu data;
    private javax.swing.JMenu about;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPopupMenu popup;
    private javax.swing.JTabbedPane navigation;
    private javax.swing.JTabbedPane notebooks;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField addTagField;
    private javax.swing.JTextField addTitleField;
    private javax.swing.JTextPane addTextPane;
    private javax.swing.JList tags;
    private final javax.swing.JFileChooser fileChooser;
    private Data m_data;
    private Vector m_tags;
    private Vector m_tagsMaster;
    private Vector m_entries;
    ArrayList<Entry> currentEntries;
    private CustomEntryRenderer entryRenderer;
    private boolean isControlPressed;
    private String currentSaveName;
    private Entry entrySelected;
    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton clearButton;
    // End of variables declaration   
}

