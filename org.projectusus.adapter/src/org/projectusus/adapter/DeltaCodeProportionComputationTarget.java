// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.adapter;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

public class DeltaCodeProportionComputationTarget implements ICodeProportionComputationTarget {

    private final Map<IProject, List<IFile>> changes;
    private final Map<IProject, List<IFile>> deletions;
    private final List<IProject> removedProjects;

    public DeltaCodeProportionComputationTarget( List<IResourceDelta> deltas ) throws CoreException {
        ChangedResourcesCollector changeCollector = new ChangedResourcesCollector();
        for( IResourceDelta delta : deltas ) {
            delta.accept( changeCollector );
        }
        removedProjects = changeCollector.getRemovedProjects();
        changes = changeCollector.getChanges();
        deletions = changeCollector.getDeletions();
    }

    public DeltaCodeProportionComputationTarget( IResourceDelta delta ) throws CoreException {
        this( createList( delta ) );
    }

    private static <T> List<T> createList( T element ) {
        List<T> list = new ArrayList<T>();
        list.add( element );
        return list;
    }

    public Collection<IFile> getFiles( IProject project ) throws CoreException {
        return getFilesFrom( project, changes );
    }

    public Collection<IProject> getProjects() {
        Set<IProject> result = new HashSet<IProject>();
        result.addAll( changes.keySet() );
        result.addAll( deletions.keySet() );
        return result;
    }

    public Collection<IFile> getRemovedFiles( IProject project ) throws CoreException {
        return getFilesFrom( project, deletions );
    }

    public Collection<IProject> getRemovedProjects() {
        return unmodifiableList( removedProjects );
    }

    // internal
    // /////////

    private Collection<IFile> getFilesFrom( IProject project, Map<IProject, List<IFile>> collector ) {
        List<IFile> result = new ArrayList<IFile>();
        if( collector.containsKey( project ) ) {
            result.addAll( collector.get( project ) );
        }
        return result;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    private boolean isEmpty() {
        return changes.isEmpty() && deletions.isEmpty() && removedProjects.isEmpty();
    }
}
