package environments;

import hostactivator.HostActivator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;



import environments.service.EnvironmentsService;
import environments.variable.ForestCell;

public class EnvironmentsServiceImp implements EnvironmentsService{
	
	ArrayList cellList;
	private double monitoringDistance = 100;
	public EnvironmentsServiceImp(ArrayList cellList) {
		// TODO Auto-generated constructor stub
		this.cellList = cellList;
		
	}

	public ArrayList getCellList() {
		// TODO Auto-generated method stub
		return cellList;
	}
	public void changeRandomEnvironment()
	{
		for(int i =0;i<cellList.size();i++)
		{
			ForestCell fc = (ForestCell) cellList.get(i);
			//change weather condition
			int n = new Random().nextInt(4);
			if(n==0)
			{
				fc.getWeather().setCondition("Cloudy");
				//fc.getWeather().setVisibility(10);
			}
			else if(n==1)
			{
				fc.getWeather().setCondition("Rainy");
				//fc.getWeather().setVisibility(10);
			}
			else if(n==2)
			{
				fc.getWeather().setCondition("Sunny");
				//foggy after rain
				//fc.getWeather().setVisibility(3);
				//fc.getWeather().setVisibility(10);
			}
			
			int v = new Random().nextInt(10);
			
			if(v == 1)
			{
				fc.getWeather().setVisibility(3);
			}
			else
				fc.getWeather().setVisibility(10);
			
			int w = new Random().nextInt(10);
			
			if(w == 1)
			{
				fc.getWeather().getWind().setVelocity(25);
			}
			else
				fc.getWeather().getWind().setVelocity(5);
			
			
//		MainView.drawEnvironment();
			drawEnvironment(i, fc);
		}

	}

	public void initEnvironment() {
		// TODO Auto-generated method stub
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("/data/cellSpec.txt"));

			String s;
			while ((s = in.readLine()) != null) {
				String[] cellSpec;
				cellSpec = s.split("\t");

				// int x, int y, String feature, String condition, double
				// windVelocity, String windDirection, String density
				ForestCell fc = new ForestCell(Integer.parseInt(cellSpec[0]),
						Integer.parseInt(cellSpec[1]), cellSpec[2],
						cellSpec[3], Double.parseDouble(cellSpec[4]),
						cellSpec[5], Double.parseDouble(cellSpec[6]),
						cellSpec[7]
						);
				cellList.add(fc);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<9;i++)
		{
			drawEnvironment(i, (ForestCell)cellList.get(i));
		}
	}
	
	
	private void drawEnvironment (int i,ForestCell fc) {
		
		Object drawUI = HostActivator.getService("selab.dev.unmannedforestmonitor.uiservice.DrawService");
		Class[] parameterDrawEnv = {Integer.class,Object.class};
		Object[] parameterDrawEnvObj = {new Integer(i),fc};
		
		try {
			Method drawEnvironments = drawUI.getClass().getDeclaredMethod("drawEnvironments", parameterDrawEnv);
			
			try {
				drawEnvironments.invoke(drawUI, parameterDrawEnvObj);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double getMonitoringDistance() {
		return monitoringDistance;
	}
}
