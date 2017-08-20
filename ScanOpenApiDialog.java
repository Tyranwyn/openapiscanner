package org.zaproxy.zap.extension.openapiscanner;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ScanOpenApiDialog extends AbstractDialog {
    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "openapiscanner.scandialog.";

    private ButtonGroup rbGroup = new ButtonGroup();
    private ButtonGroup rbTypeGroup = new ButtonGroup();

    private JRadioButton rbXss = new JRadioButton(Constant.messages.getString(PREFIX + "xss"));
    private JRadioButton rbSqlI = new JRadioButton(Constant.messages.getString(PREFIX + "sqli"));
    private JRadioButton rbXmlI = new JRadioButton(Constant.messages.getString(PREFIX + "xmli"));
    private JRadioButton rbBO = new JRadioButton(Constant.messages.getString(PREFIX + "bufferoverflow"));
    private JRadioButton rbJson = new JRadioButton(Constant.messages.getString(PREFIX + "json"));

    private JRadioButton rbUrl = new JRadioButton(Constant.messages.getString(PREFIX + "url"));
    private JRadioButton rbBody = new JRadioButton(Constant.messages.getString(PREFIX + "body"));

    private JButton buttonScan = new JButton(Constant.messages.getString(PREFIX + "scan"));
    private JButton buttonCancel = new JButton(Constant.messages.getString("all.button.cancel"));

    ExtensionOpenApiScanner caller;
    private String url = "";
    private File file = null;

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
    public ScanOpenApiDialog(JFrame parent, ExtensionOpenApiScanner caller, File file) {
        super(parent, true);
        this.caller = caller;
        this.file = file;

        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        configureLayout();
    }

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
        rbXss.setActionCommand("0");
        rbGroup.add(rbXss);
        add(rbXss, constraints);

        // SQLI radiobutton
        constraints.gridy = 1;
        rbSqlI.setActionCommand("1");
        rbGroup.add(rbSqlI);
        add(rbSqlI, constraints);

        // XMLI radiobutton
        constraints.gridy = 2;
        rbXmlI.setActionCommand("2");
        rbGroup.add(rbXmlI);
        add(rbXmlI, constraints);

        // Buffer overflow radiobutton
        constraints.gridy = 3;
        rbBO.setActionCommand("3");
        rbGroup.add(rbBO);
        add(rbBO, constraints);

        // TBD radiobutton
        constraints.gridy = 4;
        rbJson.setActionCommand("4");
        rbGroup.add(rbJson);
        add(rbJson, constraints);

        // URL rb
        constraints.gridx = 0;
        constraints.gridy = 5;
        rbUrl.setActionCommand("url");
        rbUrl.setSelected(true);
        rbTypeGroup.add(rbUrl);
        add(rbUrl, constraints);

        // Body rb
        constraints.gridx = 1;
        constraints.gridy = 5;
        rbBody.setActionCommand("body");
        rbTypeGroup.add(rbBody);
        add(rbBody, constraints);

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
//        System.out.println(rbGroup.getSelection().getActionCommand());
        if (caller != null) {
            if (!url.isEmpty()) {
                // Parsing in other thread
                try {
                    caller.importOpenApiDefinition(new URI(url, false),true, rbGroup.getSelection().getActionCommand(), rbTypeGroup.getSelection().getActionCommand());
                } catch (URIException e) {
                    View.getSingleton().showWarningDialog(thisDialog, Constant.messages.getString(PREFIX + "badurl"));
                }
            } else if (url.isEmpty() || file != null) {
                // Parsing imported file in another thread
                caller.importOpenApiDefinition(file, true, rbGroup.getSelection().getActionCommand(), rbTypeGroup.getSelection().getActionCommand());
            }
        }
        setVisible(false);
        dispose();
    }

    private void onCancelButton() {
        ScanOpenApiDialog.this.setVisible(false);
        ScanOpenApiDialog.this.dispose();
    }
}
