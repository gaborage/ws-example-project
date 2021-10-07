package app.domain;

import core.framework.api.json.Property;

/**
 * @author gabo
 */
public enum Status {
    @Property(name = "PENDING")
    PENDING,
    @Property(name = "READY")
    READY,
    @Property(name = "FAILED")
    FAILED
}
