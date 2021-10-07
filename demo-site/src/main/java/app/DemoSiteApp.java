package app;

import core.framework.module.App;
import core.framework.module.SystemModule;

/**
 * @author gabo
 */
public class DemoSiteApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        http().gzip();
        site().security();

        load(new WebModule());
    }
}
