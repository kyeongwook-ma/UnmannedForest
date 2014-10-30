package usabilityimprover;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import usabilityimprover.service.UsabilityImprover;
import usabilityimprover.streatgy.StrategyFactory;


/**
 * Explanation : 
 * @version : 2014. 6. 10.
 * @author : se
 * UsabilityImprover / package usabilityimprover;
 */

public class UsabilityImproverImpl implements UsabilityImprover {

	private ArrayList genStreatgies(ArrayList diagnosis) {

		ArrayList strategies = new ArrayList();

		for(Object type : diagnosis) {
			try {
				strategies.add(StrategyFactory.getStreatgy((Integer) type));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Usability : streatgie ������");
		return strategies;
	}

	public ArrayList adapt(ArrayList diagnosis) {
		try {
			return genStreatgies(diagnosis);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return diagnosis;
	}
}

