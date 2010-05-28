package org.projectusus.core.filerelations.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.junit.Before;
import org.junit.Test;
import org.projectusus.core.filerelations.ClassDescriptorCleanup;
import org.projectusus.core.filerelations.internal.metrics.ACDCalculator;
import org.projectusus.core.internal.proportions.rawdata.UsusModel;

public class FileRelationClassDescriptorTest {

    private IFile file = mock( IFile.class );
    private Packagename packagename = Packagename.of( "packagename" ); //$NON-NLS-1$
    private String class1 = "Name1"; //$NON-NLS-1$
    private Classname classname1 = new Classname( class1 );
    private String class2 = "Name2"; //$NON-NLS-1$
    private Classname classname2 = new Classname( class2 );
    private String class3 = "Name3"; //$NON-NLS-1$
    private Classname classname3 = new Classname( class3 );

    @Before
    public void init() {
        UsusModel.clear();
    }

    @Test
    public void oneClassDescriptorNoRelations() {
        ClassDescriptor.of( file, classname1, packagename );
        checkOne();
        assertEquals( 0, FileRelation.getAllRelations().size() );
    }

    @Test
    public void twoClassDescriptorsOneRelation() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor target = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, target );
        checkTwo();
        assertEquals( 1, FileRelation.getAllRelations().size() );
    }

    @Test
    public void threeClassDescriptorsTwoRelations() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, middle );
        ClassDescriptor target = ClassDescriptor.of( file, classname3, packagename );
        FileRelation.of( middle, target );
        checkThree();
        assertEquals( 2, FileRelation.getAllRelations().size() );
    }

    @Test
    public void threeClassDescriptorsTwoRelationsFirstAndSecondRemovedInBetween() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, middle );
        checkTwo();
        assertEquals( 1, FileRelation.getAllRelations().size() );

        source.prepareRemoval();
        cleanupDescriptors( 1 );
        checkOne();
        assertEquals( 0, FileRelation.getAllRelations().size() );
        middle.prepareRemoval();
        cleanupDescriptors( 1 );
        checkNull();
        assertEquals( 0, FileRelation.getAllRelations().size() );

        ClassDescriptor source2 = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle2 = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source2, middle2 );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        ClassDescriptor target = ClassDescriptor.of( file, classname3, packagename );
        FileRelation.of( middle2, target );
        checkThree();
    }

    @Test
    public void threeClassDescriptorsTwoRelationsSecondAndFirstRemovedInBetween() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, middle );
        checkTwo();
        assertEquals( 1, FileRelation.getAllRelations().size() );

        middle.prepareRemoval();
        assertEquals( 1, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 1 );
        checkOne();

        source.prepareRemoval();
        assertEquals( 0, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 1 );
        checkNull();

        ClassDescriptor source2 = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle2 = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source2, middle2 );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        ClassDescriptor target = ClassDescriptor.of( file, classname3, packagename );
        FileRelation.of( middle2, target );
        checkThree();
    }

    @Test
    public void threeClassDescriptorsTwoRelationsSecondRemovedInBetween() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, middle );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        middle.prepareRemoval();
        assertEquals( 1, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 1 );
        checkOne();

        ClassDescriptor middle2 = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, middle2 );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        ClassDescriptor target = ClassDescriptor.of( file, classname3, packagename );
        FileRelation.of( middle2, target );
        checkThree();
    }

    @Test
    public void twoClassDescriptorsOneRelationRemoved() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor target = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, target );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        target.prepareRemoval();
        assertEquals( 1, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 1 );
        checkOne();
    }

    @Test
    public void twoClassDescriptorsOneRelationBothRemovedSourceFirst() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor target = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, target );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        source.prepareRemoval();
        target.prepareRemoval();
        assertEquals( 0, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 2 );
        checkNull();
    }

    @Test
    public void twoClassDescriptorsOneRelationBothRemovedTargetFirst() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor target = ClassDescriptor.of( file, classname2, packagename );
        FileRelation.of( source, target );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        checkTwo();

        target.prepareRemoval();
        source.prepareRemoval();
        assertEquals( 0, FileRelation.getAllRelations().size() );
        cleanupDescriptors( 2 );
        checkNull();
    }

    @Test
    public void removeRelation() {
        ClassDescriptor source = ClassDescriptor.of( file, classname1, packagename );
        ClassDescriptor middle = ClassDescriptor.of( file, classname2, packagename );
        FileRelation relation = FileRelation.of( source, middle );
        assertEquals( 1, FileRelation.getAllRelations().size() );
        assertEquals( 2, ClassDescriptor.getAll().size() );

        relation.getTargetDescriptor().removeFromPool();
        relation.remove();
    }

    private void checkNull() {
        assertEquals( 0, ClassDescriptor.getAll().size() );
        assertEquals( 0.0, ACDCalculator.getRelativeACD(), 0.0001 );
    }

    private void checkOne() {
        Set<ClassDescriptor> descriptors = ClassDescriptor.getAll();
        assertEquals( 1, descriptors.size() );
        for( ClassDescriptor descriptor : descriptors ) {
            assertEquals( 0, descriptor.getTransitiveRelationsFrom().size() );
            assertEquals( 0, descriptor.getTransitiveRelationsTo().size() );
        }
        assertEquals( 1.0, ACDCalculator.getRelativeACD(), 0.0001 );
    }

    private void checkTwo() {
        Set<ClassDescriptor> descriptors = ClassDescriptor.getAll();
        assertEquals( 2, descriptors.size() );
        for( ClassDescriptor descriptor : descriptors ) {
            if( descriptor.getClassname().toString().equals( class1 ) ) {
                assertEquals( 1, descriptor.getTransitiveRelationsFrom().size() );
                assertEquals( 0, descriptor.getTransitiveRelationsTo().size() );
            } else if( descriptor.getClassname().toString().equals( class2 ) ) {
                assertEquals( 0, descriptor.getTransitiveRelationsFrom().size() );
                assertEquals( 1, descriptor.getTransitiveRelationsTo().size() );
            } else {
                fail( "Found unknown ClassDescriptor" ); //$NON-NLS-1$
            }
        }
        assertEquals( 3 / 4.0, ACDCalculator.getRelativeACD(), 0.0001 );
    }

    private void checkThree() {
        Set<ClassDescriptor> descriptors = ClassDescriptor.getAll();
        assertEquals( 3, descriptors.size() );
        for( ClassDescriptor descriptor : descriptors ) {
            if( descriptor.getClassname().toString().equals( class1 ) ) {
                assertEquals( 2, descriptor.getTransitiveRelationsFrom().size() );
                assertEquals( 0, descriptor.getTransitiveRelationsTo().size() );
            } else if( descriptor.getClassname().toString().equals( class2 ) ) {
                assertEquals( 1, descriptor.getTransitiveRelationsFrom().size() );
                assertEquals( 1, descriptor.getTransitiveRelationsTo().size() );
            } else if( descriptor.getClassname().toString().equals( class3 ) ) {
                assertEquals( 0, descriptor.getTransitiveRelationsFrom().size() );
                assertEquals( 2, descriptor.getTransitiveRelationsTo().size() );
            } else {
                fail( "Found unknown ClassDescriptor" ); //$NON-NLS-1$
            }
        }
        assertEquals( 6 / 9.0, ACDCalculator.getRelativeACD(), 0.0001 );
    }

    private void cleanupDescriptors( int count ) {
        Set<ClassDescriptor> descriptorsForCleanup = ClassDescriptorCleanup.extractDescriptorsRegisteredForCleanup();
        assertEquals( count, descriptorsForCleanup.size() );
        for( ClassDescriptor descriptor : descriptorsForCleanup ) {
            descriptor.removeFromPool();
        }
    }

}
