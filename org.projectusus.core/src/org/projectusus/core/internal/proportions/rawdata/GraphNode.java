package org.projectusus.core.internal.proportions.rawdata;

import java.util.Set;

public interface GraphNode {

    Set<? extends GraphNode> getChildren();

    String getNodeName();

    String getEdgeStartLabel();

    String getEdgeMiddleLabel();

    String getEdgeEndLabel();

    boolean isVisibleFor( int limit );
}
