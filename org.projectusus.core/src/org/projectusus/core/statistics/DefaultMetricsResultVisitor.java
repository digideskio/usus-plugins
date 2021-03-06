package org.projectusus.core.statistics;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.projectusus.core.IMetricsResultVisitor;
import org.projectusus.core.basis.JavaModelPath;
import org.projectusus.core.basis.MetricsResults;
import org.projectusus.core.basis.SourceCodeLocation;
import org.projectusus.core.filerelations.model.Packagename;

/**
 * Default implementation of <code>IMetricsResultVisitor</code>.
 * <p>
 * Implementors of raw data visitors can use this implementation as a basis for their own implementations. It provides empty inspection methods that can be overwritten if desired.
 * <p>
 * The visitors can visit the whole raw data tree or only a subtree. To identify the subtree to be visited, the visitor specifies the root node of the subtree by passing a
 * <code>JavaModelPath</code> object in the constructor.
 * <p>
 * To let the visitor visit the raw data tree, invoke the <code>visit()</code> method on it.
 * 
 * @author Nicole Rauch
 * 
 */
public abstract class DefaultMetricsResultVisitor implements IMetricsResultVisitor {

    private final JavaModelPath path;

    public DefaultMetricsResultVisitor() {
        this( new JavaModelPath() );
    }

    public DefaultMetricsResultVisitor( JavaModelPath modelPath ) {
        path = modelPath;
    }

    public void inspectProject( @SuppressWarnings( "unused" ) IProject project, @SuppressWarnings( "unused" ) MetricsResults results ) {
        // do nothing with the data
    }

    public void inspectPackage( @SuppressWarnings( "unused" ) Packagename pkg, @SuppressWarnings( "unused" ) MetricsResults results ) {
        // do nothing with the data
    }

    public void inspectFile( @SuppressWarnings( "unused" ) IFile file, @SuppressWarnings( "unused" ) MetricsResults results ) {
        // do nothing with the data
    }

    public void inspectClass( @SuppressWarnings( "unused" ) SourceCodeLocation location, @SuppressWarnings( "unused" ) MetricsResults results ) {
        // do nothing with the data
    }

    public void inspectMethod( @SuppressWarnings( "unused" ) SourceCodeLocation location, @SuppressWarnings( "unused" ) MetricsResults results ) {
        // do nothing with the data
    }

    public JavaModelPath getPath() {
        return path;
    }

    public void visit() {
        UsusModelProvider.acceptAndGuide( this );
    }

    public String getLabel() {
        return ""; //$NON-NLS-1$
    }
}
