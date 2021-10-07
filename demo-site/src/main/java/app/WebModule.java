package app;

import app.service.TransactionService;
import app.web.LanguageManager;
import app.web.StatusPage;
import app.web.TransactionController;
import app.web.ws.GetStatusRequest;
import app.web.ws.GetStatusResponse;
import app.web.ws.TransactionStatusListener;
import core.framework.http.HTTPMethod;
import core.framework.module.Module;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gabo
 */
public class WebModule extends Module {
    @Override
    protected void initialize() {
        bind(LanguageManager.class);
        bind(TransactionService.class);
        http().limitRate().add("status", 5, 1, TimeUnit.SECONDS);
        ws().listen("/ws/transaction/status", GetStatusRequest.class, GetStatusResponse.class, bind(TransactionStatusListener.class));

        site().staticContent("/static").cache(Duration.ofHours(1));

        List<String> messages = List.of("messages/main.properties", "messages/main_en.properties", "messages/main_en_CA.properties");
        site().message(messages, "en_US", "en_CA");

        var controller = bind(TransactionController.class);
        site().template("/template/status.html", StatusPage.class);
        http().route(HTTPMethod.GET, "/status", controller::index);
        http().route(HTTPMethod.GET, "/webhook/:id/failed", controller::failed);
        http().route(HTTPMethod.GET, "/webhook/:id/success", controller::success);
    }
}
