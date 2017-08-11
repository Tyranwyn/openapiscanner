package org.zaproxy.zap.extension.openapiscanner;

import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.zaproxy.zap.Version;
import org.zaproxy.zap.view.ZapMenuItem;

import java.io.File;
import java.util.List;

public class ExtensionOpenApiScanner extends ExtensionAdaptor implements CommandLineListener {

    public static final String NAME = "ExtensionOpenApiScanner";

    private static final String THREAD_PREFIX = "ZAP-Scan-OpenAPI-";

    private ZapMenuItem menuOpenApiScanner = null;
    private int threadId = 1;

    private static final Logger LOG = Logger.getLogger(ExtensionOpenApiScanner.class);

    public ExtensionOpenApiScanner(String NAME) {
        super(NAME);
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
