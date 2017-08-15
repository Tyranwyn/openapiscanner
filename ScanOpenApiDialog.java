package org.zaproxy.zap.extension.openapiscanner;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScanOpenApiDialog extends AbstractDialog {
    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "openapiscanner.scandialog.";

    private ButtonGroup rbGroup = new ButtonGroup();

    private JRadioButton rbXss = new JRadioButton(Constant.messages.getString(PREFIX + "xss"));
    private JRadioButton rbSqlI = new JRadioButton(Constant.messages.getString(PREFIX + "sqli"));
    private JRadioButton rbXmlI = new JRadioButton(Constant.messages.getString(PREFIX + "xmli"));
    private JRadioButton rbBO = new JRadioButton(Constant.messages.getString(PREFIX + "bufferoverflow"));
    private JRadioButton rbYetToDefine = new JRadioButton(Constant.messages.getString(PREFIX + "tba"));
    private JRadioButton rbCustom = new JRadioButton(Constant.messages.getString(PREFIX + "custom"));

    private JButton buttonScan = new JButton(Constant.messages.getString(PREFIX + "scan"));
    private JButton buttonCancel = new JButton(Constant.messages.getString("all.button.cancel"));

    ExtensionOpenApiScanner caller;
    private String url;

    // Constructor with given url
    public ScanOpenApiDialog(JFrame parent, ExtensionOpenApiScanner caller, String url) {
        super(parent, true);
        this.caller = caller;
        this.url = url;

        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        configureLayout();
    }

    // Constructor with given local file
    /*public ScanOpenApiDialog(JFrame parent, ExtensionOpenApiScanner caller, File file) {
        super(parent, true);
        this.caller = caller;

        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        configureLayout();
    }*/

    private void configureLayout() {
        this.setTitle(Constant.messages.getString(PREFIX + "title"));

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Configuring buttons
        buttonScan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onScanButton(); }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onCancelButton(); }
        });

        // Adding components to the button group and the frame
        // XSS radiobutton
        constraints.gridx = 0;
        constraints.gridy = 0;
        rbXss.setSelected(true);
        rbGroup.add(rbXss);
        add(rbXss, constraints);

        // SQLI radiobutton
        constraints.gridy = 1;
        rbGroup.add(rbSqlI);
        add(rbSqlI, constraints);

        // XMLI radiobutton
        constraints.gridy = 2;
        rbGroup.add(rbXmlI);
        add(rbXmlI, constraints);

        // Buffer overflow radiobutton
        constraints.gridy = 3;
        rbGroup.add(rbBO);
        add(rbBO, constraints);

        // TBD radiobutton
        constraints.gridy = 4;
        rbGroup.add(rbYetToDefine);
        add(rbYetToDefine, constraints);

        // Custom radiobutton
        constraints.gridy = 5;
        rbGroup.add(rbCustom);
        add(rbCustom, constraints);

        // Scan button
        constraints.gridx = 1;
        constraints.gridy = 6;
        add(buttonScan, constraints);

        // Cancel button
        constraints.gridx = 0;
        constraints.gridy = 6;
        add(buttonCancel, constraints);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void onScanButton() {
        dispose();
    }

    private void onCancelButton() {
        ScanOpenApiDialog.this.setVisible(false);
        ScanOpenApiDialog.this.dispose();
    }
}
