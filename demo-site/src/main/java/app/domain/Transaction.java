package app.domain;

import core.framework.api.json.Property;

/**
 * @author gabo
 */
public class Transaction {
    @Property(name = "id")
    public String id;

    @Property(name = "status")
    public Status status;
}
