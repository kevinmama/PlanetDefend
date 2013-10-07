package starsector.mod.nf.support;

import static starsector.mod.nf.support.PreDefinedCargoItemType.GREEN_CREW;
import static starsector.mod.nf.support.PreDefinedCargoItemType.REGULAR_CREW;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.VerificationsInOrder;

import org.junit.Assert;
import org.junit.Test;

import com.fs.starfarer.api.campaign.CargoAPI;


public class CargoSupportTest {
	
	@Test
	public void testConvertCargoItem(@Injectable final CargoAPI cargo){
		
		//
		// the original
		//
		
		new NonStrictExpectations() {
			{
				cargo.getQuantity(GREEN_CREW.getType(), GREEN_CREW.getId());
				result = 100;
				
				cargo.getQuantity(REGULAR_CREW.getType(), REGULAR_CREW.getId());
				result = 50;
				
			}
		};
		
		float result = CargoSupport.convertCargoItem(
				cargo, GREEN_CREW.getType(), GREEN_CREW.getId(), 40, Float.MAX_VALUE,
				cargo, REGULAR_CREW.getType(), REGULAR_CREW.getId(), 0, 70,
				100
		);
		
		Assert.assertEquals(20f, result, 0.01f);

		new VerificationsInOrder() {
			{
				float num1;
				float num2;
				
				cargo.removeItems(GREEN_CREW.getType(), GREEN_CREW.getId(), num1 = withCapture());
				cargo.addItems(REGULAR_CREW.getType(), REGULAR_CREW.getId(), num2 = withCapture());
				
				Assert.assertEquals(60f, num1, 0.01f);
				Assert.assertEquals(20f, num2, 0.01f);
			}
		};
		
	}
	

	   // Declare the mocks you need through mock fields or mock parameters.
//	   public void testDoOperationAbc(@Injectable final DependencyXyz mock) {
//	      new NonStrictExpectations() { // record desired results for method invocations
//	         // An internal dependency ("new-ed" later) can be mocked as well:
//	         @Mocked AnotherDependency anotherMock;
//
//	         {
//	            // Simply invoke a mocked method/constructor to record an expectation.
//	            anotherMock.doSomething("test");
//	            result = 123; // assign results (values to return, exceptions to throw)
//
//	            // Will cause an exception to be thrown if called:
//	            DependencyXyz.someStaticMethod(); result = new IOException();
//	         }
//	      };
//
//	      tested.doOperation("some data"); // exercise the code under test
//
//	      new Verifications() {{ // verify expected invocations, if any
//	         // Use argument matchers (anyXyz, etc.) for any subset of parameters.
//	         mock.complexOperation(true, anyInt, null);
//	         times = 1; // specify invocation count constraints if/as needed
//
//	         // Easily capture any argument for use in arbitrary assertions:
//	         ComplexObject obj;
//	         int num;
//	         mock.anotherComplexOp(obj = withCapture(), num = withCapture());
//	         assertTrue(obj.getData().isUppercase());
//	         assertTrue(num > 0 && num < 10);
//	      }};
//	   }
	
	
}
