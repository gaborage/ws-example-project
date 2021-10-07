package app.web.ws;

import app.service.TransactionService;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.Request;
import core.framework.web.rate.LimitRate;
import core.framework.web.websocket.Channel;
import core.framework.web.websocket.ChannelListener;

/**
 * @author gabo
 */
public class TransactionStatusListener implements ChannelListener<GetStatusRequest, GetStatusResponse> {
    @Inject
    TransactionService service;

    @Override
    public void onConnect(Request request, Channel<GetStatusResponse> channel) {
        channel.join("private");
    }

    @Override
    @LimitRate("status")
    public void onMessage(Channel<GetStatusResponse> channel, GetStatusRequest message) {
        if (Strings.isBlank(message.id)) {
            channel.close();
            return;
        }

        var trx = service.getOrCreate(message.id);
        var result = new GetStatusResponse();
        result.status = trx.status.name();
        channel.context().put("id", trx.id);
        channel.send(result);
    }
}
