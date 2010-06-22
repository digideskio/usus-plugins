package org.projectusus.core.internal.proportions.rawdata;

import org.projectusus.core.filerelations.model.ClassDescriptor;

public class ACDStatistic extends DefaultStatistic {

    public ACDStatistic( JavaModelPath path ) {
        super( path, calculateCcdLimit( new ClassCountVisitor().getClassCount() ) );
    }

    public ACDStatistic() {
        super( calculateCcdLimit( new ClassCountVisitor().getClassCount() ) );
    }

    @Override
    public void inspectClass( SourceCodeLocation location, MetricsResults results ) {
        addViolation( location, results.get( MetricsResults.CCD ) );
    }

    public int getCCDSum() {
        return getViolationSum();
    }

    public double getRelativeACD() {
        int numberOfClasses = ClassDescriptor.getAll().size();
        if( numberOfClasses == 0 ) {
            return 0.0;
        }
        return getCCDSum() / (double)(numberOfClasses * numberOfClasses);
    }

    public static int calculateCcdLimit( int classCount ) {
        // int classCount = new ClassCountVisitor().getClassCount();
        double log_5_classCount = Math.log( classCount ) / Math.log( 5 );
        double factor = 1.5 / Math.pow( 2, log_5_classCount );
        double limit = factor * classCount;
        return (int)limit;
    }

}