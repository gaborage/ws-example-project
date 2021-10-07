package app.web;

import app.domain.Transaction;
import app.service.TransactionService;
import app.web.ws.GetStatusResponse;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.WebSocketContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gabo
 */
public class TransactionController {
    @Inject
    WebSocketContext context;
    @Inject
    LanguageManager languageManager;
    @Inject
    TransactionService service;

    public Response index(Request request) {
        return Response.html("/template/status.html", new StatusPage(), languageManager.language());
    }

    public Response success(Request request) {
        var id = request.pathParam("id");

        List<Channel<GetStatusResponse>> room = context.room("private");
        var channel = room.stream().filter(c -> Strings.equals(Objects.requireNonNull(c.context().get("id")).toString(), id)).collect(Collectors.toList());
        channel.forEach(this::_doSuccess);
        return Response.text("done");
    }

    private void _doSuccess(Channel<GetStatusResponse> channel) {
        var id = Objects.requireNonNull(channel.context().get("id")).toString();
        var trx = service.success(id);
        send(channel, trx);
    }

    private void _doFailed(Channel<GetStatusResponse> channel) {
        var id = Objects.requireNonNull(channel.context().get("id")).toString();
        var trx = service.failed(id);
        send(channel, trx);
    }

    private void send(Channel<GetStatusResponse> c, Transaction trx) {
        var message = new GetStatusResponse();
        message.status = trx.status.name();
        c.send(message);
    }

    public Response failed(Request request) {
        var id = request.pathParam("id");

        List<Channel<GetStatusResponse>> room = context.room("private");
        var channel = room.stream().filter(c -> Strings.equals(Objects.requireNonNull(c.context().get("id")).toString(), id)).collect(Collectors.toList());
        channel.forEach(this::_doFailed);
        return Response.text("done");
    }
}
