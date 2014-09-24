package net.canadensys.dataportal.occurrence.model;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Simple test for OccurrenceViewModel behavior
 * @author cgendreau
 *
 */
public class OccurrenceViewModelTest {
	
	@Test
	public void testOccurrenceViewModel(){
		OccurrenceViewModel ovm = new OccurrenceViewModel();
		ovm.addMultimediaViewModel(new MultimediaViewModel("1", "1", null, null, null, true, null));
		ovm.addMultimediaViewModel(new MultimediaViewModel("2", "2", null, null, null, false, null));
		
		assertEquals(1, ovm.getImageViewModelList().size());
		assertEquals(1, ovm.getOtherMediaViewModelList().size());
	}
}
