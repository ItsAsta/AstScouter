/*
 * Created by JFormDesigner on Tue Sep 10 16:21:04 BST 2019
 */

package org.rspeer.gui;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.miginfocom.swing.MigLayout;
import org.rspeer.AstScouter;
import org.rspeer.bot.Commands;
import org.rspeer.io.LeechFile;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Asta
 */
public class GUI extends JFrame {

    public GUI() {
        this.properties = new Properties();
        initComponents();
    }

    public static JDA jda;
    private Properties properties;
    private static final File SETTINGS_PATH = new File(Script.getDataDirectory().toAbsolutePath().toString(), "AntScouter_" + "Settings.ini");
    private static final Timer TIMER = new Timer();
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Asta
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel worldSettingsPanel;
    private JSpinner worldStart;
    private JLabel label2;
    private JSpinner worldEnd;
    private JPanel discordPanel;
    private JLabel label4;
    private JTextField serverTokenId;
    private JLabel label7;
    private JTextField serverChannelId;
    private JPanel gpPanel;
    private JSpinner minValue;
    private JLabel label1;
    private JSpinner maxValue;
    private JPanel fileSettingsPanel;
    private JLabel label3;
    private JTextField leechFileName;
    private JPanel buttonBar;
    private JButton startBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void saveSettings() {
        try {
            properties.clear();
            properties.put("world_start", String.valueOf(worldStart.getValue()));
            properties.put("world_end", String.valueOf(worldEnd.getValue()));
            properties.put("server_token_id", serverTokenId.getText());
            properties.put("server_channel_id", serverChannelId.getText());
            properties.put("min_value", String.valueOf(minValue.getValue()));
            properties.put("max_value", String.valueOf(maxValue.getValue()));
            properties.put("leech_file_name", leechFileName.getText());
            properties.store(new FileOutputStream(SETTINGS_PATH), "GUI Settings");
//            Log.fine("AS", "SAVED SETTINGS!");
        } catch (Exception e) {
            Log.severe("AS", "Can't Save Settings! Exception: " + e);
        }
    }

    public void loadSettings() {
        try {
            if (!SETTINGS_PATH.exists()) {
                SETTINGS_PATH.createNewFile();
            }

            properties.load(new FileInputStream(SETTINGS_PATH));


            String[] textNames = {"server_token_id", "server_channel_id", "leech_file_name"};
            JTextField[] textField = {serverTokenId, serverChannelId, leechFileName};

            for (int x = 0; x < textField.length; x++) {
                String value = properties.getProperty(textNames[x]);
                if (value != null) {
                    textField[x].setText(value);
                }
            }

            String[] spinnerNames = {"world_start", "world_end", "min_value", "max_value"};
            JSpinner[] spinners = {worldStart, worldEnd, minValue, maxValue};

            for (int x = 0; x < spinnerNames.length; x++) {
                String value = properties.getProperty(spinnerNames[x]);
                if (value != null) {
                    spinners[x].setValue(Integer.parseInt(value));
                }
            }

//            Log.fine("AS", "LOADED SETTINGS!");
        } catch (Exception e) {
            Log.severe("AS", "Can't Load Settings! Exception: " + e);
            Log.severe(e);
        }
    }

    private void startBtnActionPerformed() throws LoginException {
        Settings.worldStart = (Integer) worldStart.getValue();
        Settings.worldEnd = (Integer) worldEnd.getValue();
        Settings.serverTokenId = serverTokenId.getText();
        Settings.channelId = serverChannelId.getText();
        Settings.minValue = ((Integer) minValue.getValue());
        Settings.maxValue = ((Integer) maxValue.getValue());
        Settings.valueRange = IntStream.rangeClosed(Settings.minValue, Settings.maxValue).boxed().collect(Collectors.toList());
        Settings.worldRange = IntStream.rangeClosed(Settings.worldStart, Settings.worldEnd).boxed().collect(Collectors.toList());
        Settings.leechFileName = leechFileName.getText();

        try {
            LeechFile.createFile();
        } catch (IOException e) {
            Log.severe(e);
        }

        TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    AstScouter.ready = false;
                    LeechFile.addLeeches();
                } catch (IOException e) {
                    Log.severe("Can't fetch leeches from the file! Exception: " + e);
                }
            }
        }, 0, 300_000);


        saveSettings();

        jda = new JDABuilder(AccountType.BOT).setToken(Settings.serverTokenId).build();
        jda.addEventListener(new Commands());

        Log.log(Level.WARNING, "AS", "World Range | " + Settings.worldStart + " TO " + Settings.worldEnd);
        Log.log(Level.WARNING, "AS", "Value Range | " + Commands.withSuffix(Settings.minValue * 1_000_000) + " TO "
                + Commands.withSuffix(Settings.maxValue * 1_000_000));

        setVisible(false);
        AstScouter.start = true;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Asta
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        worldSettingsPanel = new JPanel();
        worldStart = new JSpinner();
        label2 = new JLabel();
        worldEnd = new JSpinner();
        discordPanel = new JPanel();
        label4 = new JLabel();
        serverTokenId = new JTextField();
        label7 = new JLabel();
        serverChannelId = new JTextField();
        gpPanel = new JPanel();
        minValue = new JSpinner();
        label1 = new JLabel();
        maxValue = new JSpinner();
        fileSettingsPanel = new JPanel();
        label3 = new JLabel();
        leechFileName = new JTextField();
        buttonBar = new JPanel();
        startBtn = new JButton();

        //======== this ========
        setTitle("AstScouter");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {


            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //======== worldSettingsPanel ========
                {
                    worldSettingsPanel.setBorder(new TitledBorder(new EtchedBorder(), "World Settings", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
                    worldSettingsPanel.setMinimumSize(new Dimension(130, 130));
                    worldSettingsPanel.setPreferredSize(new Dimension(120, 130));
                    worldSettingsPanel.setLayout(new MigLayout(
                        "flowy,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- worldStart ----
                    worldStart.setModel(new SpinnerNumberModel(301, 301, null, 1));
                    worldSettingsPanel.add(worldStart, "cell 0 0,align center center,grow 0 0");

                    //---- label2 ----
                    label2.setText("TO");
                    worldSettingsPanel.add(label2, "cell 0 1,align center center,grow 0 0");

                    //---- worldEnd ----
                    worldEnd.setModel(new SpinnerNumberModel(301, 301, null, 1));
                    worldSettingsPanel.add(worldEnd, "cell 0 2,align center center,grow 0 0");
                }
                contentPanel.add(worldSettingsPanel, "cell 1 0,grow");

                //======== discordPanel ========
                {
                    discordPanel.setMinimumSize(new Dimension(250, 130));
                    discordPanel.setPreferredSize(new Dimension(250, 130));
                    discordPanel.setBorder(new TitledBorder(new EtchedBorder(), "Discord Bot Settings", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
                    discordPanel.setLayout(new MigLayout(
                        "hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label4 ----
                    label4.setText("Server Token");
                    discordPanel.add(label4, "cell 0 0,align center center,grow 0 0");

                    //---- serverTokenId ----
                    serverTokenId.setMinimumSize(new Dimension(200, 25));
                    serverTokenId.setPreferredSize(new Dimension(200, 25));
                    serverTokenId.setHorizontalAlignment(SwingConstants.CENTER);
                    discordPanel.add(serverTokenId, "cell 0 1,align center center,grow 0 0");

                    //---- label7 ----
                    label7.setText("Channel ID");
                    discordPanel.add(label7, "cell 0 2,align center center,grow 0 0");

                    //---- serverChannelId ----
                    serverChannelId.setHorizontalAlignment(SwingConstants.CENTER);
                    serverChannelId.setPreferredSize(new Dimension(200, 25));
                    serverChannelId.setMinimumSize(new Dimension(200, 25));
                    discordPanel.add(serverChannelId, "cell 0 3,align center center,grow 0 0");
                }
                contentPanel.add(discordPanel, "cell 2 0,grow");

                //======== gpPanel ========
                {
                    gpPanel.setBorder(new TitledBorder(new EtchedBorder(), "Value (M)", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
                    gpPanel.setMinimumSize(new Dimension(150, 130));
                    gpPanel.setPreferredSize(new Dimension(130, 130));
                    gpPanel.setLayout(new MigLayout(
                        "hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- minValue ----
                    minValue.setModel(new SpinnerNumberModel(1, 1, null, 1));
                    minValue.setMinimumSize(new Dimension(85, 32));
                    minValue.setMaximumSize(new Dimension(120, 32));
                    minValue.setPreferredSize(new Dimension(85, 32));
                    gpPanel.add(minValue, "cell 0 0,align center center,grow 0 0");

                    //---- label1 ----
                    label1.setText("TO");
                    gpPanel.add(label1, "cell 0 1,align center center,grow 0 0");

                    //---- maxValue ----
                    maxValue.setModel(new SpinnerNumberModel(1, 1, null, 1));
                    gpPanel.add(maxValue, "cell 0 2");
                }
                contentPanel.add(gpPanel, "cell 1 1,align center center,grow 0 0");

                //======== fileSettingsPanel ========
                {
                    fileSettingsPanel.setBorder(new TitledBorder(new EtchedBorder(), "File Settings", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
                    fileSettingsPanel.setMinimumSize(new Dimension(250, 130));
                    fileSettingsPanel.setPreferredSize(new Dimension(250, 130));
                    fileSettingsPanel.setLayout(new MigLayout(
                        "hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                        "[]"));

                    //---- label3 ----
                    label3.setText("Leech File Name");
                    label3.setHorizontalAlignment(SwingConstants.CENTER);
                    fileSettingsPanel.add(label3, "cell 0 0,grow");

                    //---- leechFileName ----
                    leechFileName.setHorizontalAlignment(SwingConstants.CENTER);
                    leechFileName.setMinimumSize(new Dimension(200, 20));
                    leechFileName.setPreferredSize(new Dimension(200, 25));
                    fileSettingsPanel.add(leechFileName, "cell 0 1,align center center,grow 0 0");
                }
                contentPanel.add(fileSettingsPanel, "cell 2 1,align center center,grow 0 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]",
                    // rows
                    null));

                //---- startBtn ----
                startBtn.setText("START");
                startBtn.addActionListener(e -> {
                    try {
                        startBtnActionPerformed();
                    } catch (LoginException ex) {
                        ex.printStackTrace();
                    }
                });
                buttonBar.add(startBtn, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(555, 425);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
