/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.arch.attributes;

import java.io.Serializable;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAttributeData implements Serializable {

    private static final long serialVersionUID = 1990400415318745737L;
    private final String attribute;
    private final String attributeID;
    private final double value;
    private final TARDISAttributeOperation operation;

    public TARDISAttributeData(String attribute, String attributeID, double value, TARDISAttributeOperation operation) {
        this.attribute = attribute;
        this.attributeID = attributeID;
        this.value = value;
        this.operation = operation;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getAttributeID() {
        return attributeID;
    }

    public double getValue() {
        return value;
    }

    public TARDISAttributeOperation getOperation() {
        return operation;
    }
}
