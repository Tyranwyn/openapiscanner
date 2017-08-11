package org.zaproxy.zap.extension.openapiscanner;

import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.zaproxy.zap.Version;
import org.zaproxy.zap.view.ZapMenuItem;

import javax.swing.*;
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
            menuOpenApiScanner.setToolTipText("openapiscanner.topmenu.tools.openapiscanner.tooltip");

            menuOpenApiScanner.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {

                });
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
        return null;
    }
}
