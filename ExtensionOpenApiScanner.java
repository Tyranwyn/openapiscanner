package org.zaproxy.zap.extension.openapiscanner;

import io.swagger.models.Scheme;
import org.apache.commons.httpclient.URI;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.openapiscanner.converter.swagger.SwaggerConverter;
import org.zaproxy.zap.extension.openapiscanner.network.Requestor;
import org.zaproxy.zap.extension.spider.ExtensionSpider;
import org.zaproxy.zap.model.ValueGenerator;
import org.zaproxy.zap.spider.parser.SpiderParser;
import org.zaproxy.zap.view.ZapMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtensionOpenApiScanner extends ExtensionAdaptor {

    public static final String NAME = "ExtensionOpenApiScanner";

    private static final String THREAD_PREFIX = "ZAP-Scan-OpenAPI-";

    private ZapMenuItem menuOpenApiScanner = null;
    private int threadId = 1;
    private SpiderParser customSpider;

    private static final Logger LOG = Logger.getLogger(ExtensionOpenApiScanner.class);

    public ExtensionOpenApiScanner() {
        super(NAME);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        /* Custom spider is added in order to explore Open API definitions. */
        ExtensionSpider spider = (ExtensionSpider) Control.getSingleton()
                .getExtensionLoader()
                .getExtension(ExtensionSpider.NAME);
        customSpider = new OpenApiSpider();
        if (spider != null) {
            spider.addCustomParser(customSpider);
            LOG.debug("Added custom Open API spider.");
        } else {
            LOG.warn("Custom Open API spider could not be added.");
        }

        // Add the scanner menu option to the top menu
        if (getView() != null)
            extensionHook.getHookMenu().addToolsMenuItem(getMenuOpenApiScanner());

        extensionHook.addApiImplementor(new OpenApiAPI(this));

    }

    @Override
    public void unload() {
        super.unload();
        ExtensionSpider spider = (ExtensionSpider) Control.getSingleton()
                .getExtensionLoader()
                .getExtension(ExtensionSpider.NAME);
        if (spider != null) {
            spider.removeCustomParser(customSpider);
            LOG.debug("Removed custom Open API spider.");
        }
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
                        public void run() { new ImportOpenApiDialog(View.getSingleton().getMainFrame(), shadowCopy); }
                    });
                }
            });

        }
        return menuOpenApiScanner;
    }

    @Override
    public String getAuthor() {
        return Constant.ZAP_TEAM + " plus Joanna Bona, Artur Grzesica, Michal Materniak and Marcin Spiewak -- Addition:" + Constant.messages.getString("openapiscanner.author");
    }

    public List<String> importOpenApiDefinition(File file, boolean initViaUi, String type, String type2) {
        try {
            importOpenApiDefinition((Scheme) null, FileUtils.readFileToString(file), initViaUi, type, type2);
        } catch (IOException e) {
            if (initViaUi) {
                View.getSingleton().showWarningDialog(Constant.messages.getString("openapiscanner.io.error"));
            }
            LOG.warn(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public List<String> importOpenApiDefinition(URI uri, boolean initViaUi, String type, String type2) {
        Requestor requestor = new Requestor(HttpSender.MANUAL_REQUEST_INITIATOR);
        requestor.addListener(new HistoryPersister());
        try {
            return importOpenApiDefinition(
                    Scheme.forValue(uri.getScheme().toLowerCase()), requestor.getResponseBody(uri), initViaUi, type, type2);
        } catch (IOException e) {
            if (initViaUi) {
                View.getSingleton().showWarningDialog(Constant.messages.getString("openapiscanner.io.error"));
            }
            LOG.warn(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private List<String> importOpenApiDefinition(final Scheme defaultScheme, final String definition, final boolean initViaUi, final String type, final String type2) {
        final List<String> errors = new ArrayList<>();

        Thread thread = new Thread(THREAD_PREFIX + threadId++) {
            @Override
            public void run() {
                try {
                    Requestor requestor = new Requestor(HttpSender.MANUAL_REQUEST_INITIATOR);
                    requestor.setSiteOverride("");
                    requestor.addListener(new HistoryPersister());

                    SwaggerConverter converter = new SwaggerConverter(defaultScheme, definition, getValueGenerator());

                    System.out.println(type);
                    // Check radiobutton selected
                    String path = "";
                    switch (type) {
                        case "0":
                            path = Constant.getZapHome() + "fuzzers/xss.txt";
                            break;
                        case "1":
                            path = Constant.getZapHome() + "fuzzers/sqli.txt";
                            break;
                        case "2":
                            path = Constant.getZapHome() + "fuzzers/xmli.txt";
                            break;
                        case "3":
                            path = Constant.getZapHome() + "fuzzers/bufferoverflow.txt";
                            break;
                        case "4":
                            // path = Constant.getZapHome() + "fuzzers/.txt";
                            path = "";
                            break;
                        default:
                            break;
                    }

                    // Merge request models with chosen library
                    FuzzerLibraryParser libParser = new FuzzerLibraryParser(path);
                    MergeLibReqModel merger = new MergeLibReqModel(libParser.getList(), converter.getRequestModels(), type2);

//                    errors.addAll(requestor.run(converter.getRequestModels()));
                    errors.addAll(requestor.run(merger.getConvertedRequestModels()));
                    errors.addAll(converter.getErrorMessages());
                    if (errors.size() > 0) {
                        logErrors(errors, initViaUi);
                        if (initViaUi) {
                            View.getSingleton().showWarningDialog(Constant.messages.getString("openapiscanner.parse.warn"));
                        }
                    } else {
                        if (initViaUi) {
                            View.getSingleton().showMessageDialog(Constant.messages.getString("openapiscanner.parse.ok"));
                        }
                    }
                } catch (Exception e) {
                    if (initViaUi) {
                        View.getSingleton().showWarningDialog(Constant.messages.getString("openapiscanner.parse.error"));
                    }
                    logErrors(errors, initViaUi);
                    LOG.warn(e.getMessage(), e);
                }
            }
        };
        thread.start();

        if (! initViaUi) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOG.debug(e.getMessage(), e);
            }
            return errors;
        }
        return null;
    }

    private ValueGenerator getValueGenerator() {
        // Always get the latest ValueGenerator as it could have changed
        ExtensionSpider spider = Control.getSingleton()
                .getExtensionLoader()
                .getExtension(ExtensionSpider.class);
        if (spider != null) {
            return spider.getValueGenerator();
        }
        return null;
    }

    private void logErrors(List<String> errors, boolean initViaUi) {
        if (errors != null) {
            for (String error : errors) {
                if (initViaUi) {
                    View.getSingleton().getOutputPanel().append(error + "\n");
                } else {
                    LOG.warn(error);
                }
            }
        }
    }
}
