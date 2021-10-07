package app.service;

import app.domain.Status;
import app.domain.Transaction;
import core.framework.util.Maps;

import java.util.Map;

/**
 * @author gabo
 */
public class TransactionService {
    private final Map<String, Transaction> database = Maps.newHashMap();

    public Transaction getOrCreate(String id) {
        var trx = database.get(id);

        if (trx != null) {
            return trx;
        }

        trx = getTransaction(id);

        database.putIfAbsent(trx.id, trx);

        return trx;
    }

    private Transaction getTransaction(String id) {
        Transaction trx;
        trx = new Transaction();
        trx.id = id;
        trx.status = Status.PENDING;
        return trx;
    }

    public Transaction success(String id) {
        var trx = database.getOrDefault(id, getTransaction(id));
        trx.status = Status.READY;
        database.put(trx.id, trx);

        return trx;
    }

    public Transaction failed(String id) {
        var trx = database.getOrDefault(id, getTransaction(id));
        trx.status = Status.FAILED;
        database.put(trx.id, trx);

        return trx;
    }
}
