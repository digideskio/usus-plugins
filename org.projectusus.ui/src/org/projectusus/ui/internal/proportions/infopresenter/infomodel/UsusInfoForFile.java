// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal.proportions.infopresenter.infomodel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.JavaModelException;

class UsusInfoForFile implements IUsusInfo {

    private final IFile file;

    UsusInfoForFile( IResource resource ) {
        super();
        this.file = (IFile)resource;
    }

    public String[] getCodeProportionInfos() {
        try {
            List<String> result = new ArrayList<String>();
            addFormattedProportion( result );
            return result.toArray( new String[0] );
        } catch( JavaModelException jmox ) {
            return new String[] { "Error in calculating metrics values." }; //$NON-NLS-1$
        }
    }

    @SuppressWarnings( "unused" )
    protected void addFormattedProportion( List<String> result ) throws JavaModelException {
        // dummy for subclasses
    }

    public String formatTitle() {
        return file.getName();
    }
}
