package usabilityimprover.streatgy;

import hostactivator.HostActivator;
import hostactivator.ISettingFelix;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.osgi.framework.Bundle;

import parameterobjects.objectclass.Streatgy;

public class StrategyFactory {
	private static Bundle bundle;
	
	static {
		bundle = HostActivator.getFelix();
	}

	public static Streatgy getStreatgy(Integer streatgyNum)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (streatgyNum.intValue() == 1) {

			Object tabView;
			
			tabView = getControlledObj(
					ISettingFelix.effectorService, "getTabView",
					null,null);
			System.out.println("StreatgyFactory : get tabview " + tabView.toString());

			Class[] parameterType = {List.class};
			Method method =  tabView.getClass().getDeclaredMethod("constructUITab",
					parameterType);

			return new Streatgy(method,tabView,streatgyNum.intValue());

		}
		return null;
	}

	public static Object getControlledObj(String serviceImplName,
			String methodgetName, Class[] parameterTypes,Object[] parameters)
					throws IllegalAccessException, IllegalArgumentException,
					InvocationTargetException {

		for (int i=0;i< bundle.getRegisteredServices().length;i++) {
			System.out.println("StreatgyFactory : in for length" +bundle.getRegisteredServices().length);
			Object service = bundle.getBundleContext().getService(bundle.getRegisteredServices()[i]);
			System.out.println("StreatgyFactory : service get"+service.getClass().getName());
			if (service.getClass().getName().equals(serviceImplName)) {
				System.out.println("StreatgyFactory : in if");
				try {
					Method serviceMethod = service.getClass()
							.getDeclaredMethod(methodgetName, parameterTypes);
					System.out.println("StreatgyFactory : get method" + serviceMethod.getName());

					return serviceMethod.invoke(service, parameters);

				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}

		}

		return null;
	}

}
