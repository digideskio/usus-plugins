package org.projectusus.core.internal.proportions.sqi.acd;

import java.util.ArrayList;
import java.util.List;

public class ClassNode implements ILinkedNode {

    private final String name;
    private final List<ILinkedNode> children;
    private boolean marked;

    public ClassNode( String name ) {
        this.name = name;
        children = new ArrayList<ILinkedNode>();
        marked = false;
    }

    public int getChildCount() {
        return children.size();
    }

    public List<ILinkedNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked( boolean state ) {
        marked = state;
    }

}