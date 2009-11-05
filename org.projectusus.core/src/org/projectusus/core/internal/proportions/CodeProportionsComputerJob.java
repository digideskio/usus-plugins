// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions;

import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;
import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.CC;
import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.KG;
import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.ML;
import static org.projectusus.core.internal.util.CoreTexts.codeProportionsComputerJob_computing;
import static org.projectusus.core.internal.util.CoreTexts.codeProportionsComputerJob_name;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.projectusus.core.internal.proportions.checkstyledriver.CheckstyleDriver;
import org.projectusus.core.internal.proportions.model.CodeProportion;
import org.projectusus.core.internal.proportions.modelupdate.ComputationRunModelUpdate;
import org.projectusus.core.internal.proportions.sqi.IsisMetrics;
import org.projectusus.core.internal.proportions.sqi.NewWorkspaceResults;
import org.projectusus.core.internal.proportions.sqi.jdtdriver.JDTDriver;
import org.projectusus.core.internal.yellowcount.YellowCount;

class CodeProportionsComputerJob extends Job {

    private static final Object FAMILY = new Object();
    private final ICodeProportionComputationTarget target;

    CodeProportionsComputerJob( ICodeProportionComputationTarget target ) {
        super( codeProportionsComputerJob_name );
        setRule( getWorkspace().getRoot() );
        setPriority( Job.DECORATE );
        this.target = target;
    }

    @Override
    public boolean belongsTo( Object family ) {
        return family == FAMILY;
    }

    @Override
    protected IStatus run( IProgressMonitor mo ) {
        IProgressMonitor monitor = mo == null ? new NullProgressMonitor() : mo;
        monitor.beginTask( codeProportionsComputerJob_computing, 1024 );
        return performRun( monitor );
    }

    private IStatus performRun( IProgressMonitor monitor ) {
        IStatus result = Status.OK_STATUS;
        List<CodeProportion> collector = new ArrayList<CodeProportion>();
        try {
            performComputation( collector, monitor );
        } catch( CoreException cex ) {
            result = cex.getStatus();
        } finally {
            getModel().update( new ComputationRunModelUpdate( collector, result.isOK() ) );
            monitor.done();
        }
        return result;
    }

    private void performComputation( List<CodeProportion> collector, IProgressMonitor monitor ) throws CoreException {
        collector.add( YellowCount.getInstance().count() );
        computeCheckstyleBasedMetrics( collector, new SubProgressMonitor( monitor, 512 ) );
    }

    private void computeCheckstyleBasedMetrics( List<CodeProportion> collector, IProgressMonitor monitor ) throws CoreException {
        new JDTDriver( target ).run( monitor );
        new CheckstyleDriver( target ).run( monitor );

        NewWorkspaceResults results = NewWorkspaceResults.getInstance();

        for( IsisMetrics metric : Arrays.asList( CC, KG, ML ) ) {
            collector.add( results.getCodeProportion( metric ) );
        }
    }

    private UsusModel getModel() {
        return (UsusModel)UsusModel.getUsusModel();
    }
}
