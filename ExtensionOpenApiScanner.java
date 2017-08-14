package org.zaproxy.zap.extension.openapiscanner;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.view.ZapMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class ExtensionOpenApiScanner extends ExtensionAdaptor implements CommandLineListener {

    public static final String NAME = "ExtensionOpenApiScanner";

    private static final String THREAD_PREFIX = "ZAP-Scan-OpenAPI-";

    private ZapMenuItem menuOpenApiScanner = null;
    private int threadId = 1;

    private static final Logger LOG = Logger.getLogger(ExtensionOpenApiScanner.class);

    public ExtensionOpenApiScanner() {
        super(NAME);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        // Add the scanner menu option to the top menu
        if (getView() != null)
            extensionHook.getHookMenu().addToolsMenuItem(getMenuOpenApiScanner());

    }

    @Override
    public void unload() {
        super.unload();
    }

    /* Define scanner menu option to be added to the top menu */
    private ZapMenuItem getMenuOpenApiScanner() {
        if (menuOpenApiScanner == null) {
            menuOpenApiScanner = new ZapMenuItem("openapiscanner.topmenu.tools.openapiscanner");
            menuOpenApiScanner.setToolTipText(Constant.messages.getString("openapiscanner.topmenu.tools.openapiscanner.tooltip"));

            final ExtensionOpenApiScanner shadowCopy = this;
            menuOpenApiScanner.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    View.getSingleton().showMessageDialog(Constant.messages.getString("openapiscanner.topmenu.msg.example"));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() { new OpenApiScannerDialog(View.getSingleton().getMainFrame(), shadowCopy); }
                    });
                }
            });

        }
        return menuOpenApiScanner;
    }

    @Override
    public void execute(CommandLineArgument[] commandLineArguments) {

    }

    @Override
    public boolean handleFile(File file) {
        return false;
    }

    @Override
    public List<String> getHandledExtensions() {
        return null;
    }

    @Override
    public String getAuthor() {
        return Constant.messages.getString("openapiscanner.author");
    }
}
