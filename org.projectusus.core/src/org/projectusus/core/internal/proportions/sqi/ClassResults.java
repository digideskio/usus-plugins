// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.sqi;

import java.util.List;

import org.projectusus.core.internal.proportions.model.IHotspot;
import org.projectusus.core.internal.proportions.model.MetricKGHotspot;

import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class ClassResults extends Results<String, MethodResults> {

    private final String className;
    private final int lineNo;

    public ClassResults( String className, int lineNo ) {
        this.className = className;
        this.lineNo = lineNo;
    }

    public void setCCResult( BetterDetailAST methodAST, int value ) {
        getResults( methodAST ).setCCResult( value );
    }

    public void setMLResult( BetterDetailAST methodAST, int value ) {
        getResults( methodAST ).setMLResult( value );
    }

    private MethodResults getResults( BetterDetailAST resultAST ) {
        BetterDetailAST methodAST = findEnclosingMethod( resultAST );
        String methodName = getNameOfMethod( methodAST );
        MethodResults methodResults = getResults( methodName, new MethodResults( getClassName(), methodName, methodAST.getLineNo() ) );
        return methodResults;
    }

    private String getNameOfMethod( BetterDetailAST methodAST ) {
        if( methodAST == null ) {
            return ""; //$NON-NLS-1$
        }

        return methodAST.findFirstToken( TokenTypes.IDENT ).getText();
    }

    private BetterDetailAST findEnclosingMethod( BetterDetailAST anAST ) {
        BetterDetailAST methodAST = anAST;
        while( methodAST != null && isNeitherMethodNorConstructor( methodAST ) ) {
            methodAST = methodAST.getParent();
        }
        return methodAST;
    }

    private boolean isNeitherMethodNorConstructor( BetterDetailAST methodAST ) {
        int type = methodAST.getType();
        return TokenTypes.METHOD_DEF != type && TokenTypes.CTOR_DEF != type;
    }

    public String getClassName() {
        return className;
    }

    public int getLineNo() {
        return lineNo;
    }

    @Override
    public int getViolationBasis( IsisMetrics metric ) {
        if( metric.isMethodTest() ) {
            return super.getViolationBasis( metric );
        }
        return 1;
    }

    @Override
    public int getViolationCount( IsisMetrics metric ) {
        if( metric.isMethodTest() ) {
            return super.getViolationCount( metric );
        }
        return metric.isViolatedBy( this ) ? 1 : 0;
    }

    @Override
    public void addHotspots( IsisMetrics metric, List<IHotspot> hotspots ) {
        if( metric.isMethodTest() ) {
            super.addHotspots( metric, hotspots );
        } else if( metric.isViolatedBy( this ) ) {
            hotspots.add( new MetricKGHotspot( className, this.getResultCount() ) );
        }
    }
}
