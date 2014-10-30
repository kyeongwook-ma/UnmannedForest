package configurationcomposer.taskplaner;

import hostactivator.HostActivator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;





















import configurationcomposer.policy.MonitorForestStatusPolicy;
import agents.service.AgentInstancesService;
import agents.variable.BaseAgent;
import environments.service.EnvironmentsService;
import environments.variable.ForestCell;

public class ConfigurationScoreCalculator {
	private static final String MIN = "Min:";
	private int cellSize = 9;
	private AgentInstancesService agentInstances;
	private ArrayList cellList;
	private double monitoringDistance;
	private int Threadcount;
	private Vector threadList;
	private HashMap valueSet;
	private HashMap minScoreSet;
	private ArrayList maxList;
	private ArrayList minList;
	
	public ConfigurationScoreCalculator() {
		// TODO Auto-generated constructor stub
		agentInstances = (AgentInstancesService) HostActivator.getService(AgentInstancesService.class.getName());
		EnvironmentsService envService = (EnvironmentsService) HostActivator.getService(EnvironmentsService.class.getName());
		cellList = envService.getCellList();
		monitoringDistance = envService.getMonitoringDistance();
		threadList = new Vector();
		valueSet = new HashMap();
		minScoreSet = new HashMap();
		maxList = new ArrayList();
		minList = new ArrayList();
	}
	public ArrayList getOptimalConfiguration(){
		Threadcount = 0;
		CurrentScore.optimalAgentList.clear();
		threadList.clear();
		valueSet.clear();
		minScoreSet.clear();
//		cellSize = cellList.size();
		//cellSize = 9;

		final ICombinatoricsVector AgentCombination = Factory.createVector(agentInstances.getAgentIDList());
		System.out.println("agnetID " + agentInstances.getAgentIDList().toString());
		final Generator CombinationGen = Factory.createSimpleCombinationGenerator(AgentCombination, cellSize);
		System.out.println("Combination Gen : " + CombinationGen.getNumberOfGeneratedObjects());
		System.out.println("Combination Gen : " + CombinationGen.generateAllObjects().toString());
		List combinationGenList = CombinationGen.generateAllObjects();
		
//		for (final ICombinatoricsVector perm : CombinationGen) {
//			Threadcount++;
////		}
		for(int i=0;i<combinationGenList.size();i++)
		{
			Threadcount++;
		}
		
		System.out.println("Tread Count : " + Threadcount);
		
//		for (final ICombinatoricsVector perm : CombinationGen) {
//			final Generator PermutationGen = Factory.createPermutationGenerator(perm);
//			PermutationThread th = new PermutationThread(PermutationGen);
//			threadList.add(th);
//			th.start();
//		}
		for(int i=0;i<combinationGenList.size();i++)
		{
			final ICombinatoricsVector perm = (ICombinatoricsVector) combinationGenList.get(i);
			final Generator PermutationGen = Factory.createPermutationGenerator(perm);
			PermutationThread th = new PermutationThread(PermutationGen);
			threadList.add(th);
			th.start();
		}
		
		while(true)
		{
			if (threadList.size() == Threadcount) {
				
				for(int i = 0; i < threadList.size(); ++i) {
			
					Thread t = (Thread) threadList.get(i);
							
					if (t.isAlive()) {
						synchronized (t) {
							try {
								t.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return getOptimal();
			}
		}
	}
	
	private ArrayList getOptimal()
	{
		double maxVal = -1;
		double minVal = 1;
		ScoreSet maxSet = null;
		ArrayList retStr = new ArrayList();
		String minStr = null;
		Object drawUI = HostActivator.getService("selab.dev.unmannedforestmonitor.uiservice.DrawService");
		
		
		Iterator it = this.valueSet.keySet().iterator();
		
		while(it.hasNext())
		{
			System.out.println("in firstWhile : get Optimal");
			ArrayList str = (ArrayList) it.next();
			ScoreSet sc = (ScoreSet) valueSet.get(str);
//			LoggerUtil.write(LoggerUtil.LOGPANE,str.toString());
//			LoggerUtil.write(LoggerUtil.LOGPANE,"Cost:"+ String.format("%.3f",sc.getCost()) +"; Benefit:"+String.format("%.3f",sc.getBenefit())+"; Col.Score:"+String.format("%.3f",sc.getScore()));
			
//			drawSelected(String agents,String scores);
			
			Class[] drawAvailableParm = {String.class,String.class};
			String scoreString  = "Cost:";
			String temp = String.format("%.3f", new Double[] {new Double(sc.getScore())});
			scoreString += temp;
			temp = "; Benefit:";
			scoreString += temp;
			temp = String.format("%.3f",new Double[] {new Double(sc.getBenefit())})+ "; Col.Score:";
			scoreString += temp;
			temp = String.format("%.3f",new Double[] {new Double(sc.getScore())});
			scoreString += temp;
			
			Object[] drawAvailabeParmObj = {str.toString(),scoreString};
			
			try {
				Method drawAvailable = drawUI.getClass().getDeclaredMethod("drawAvailable", drawAvailableParm);
				try {
					drawAvailable.invoke(drawUI, drawAvailabeParmObj);
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
			
			
			
			if(maxVal < (((ScoreSet) this.valueSet.get(str)).getScore()))
			{
				System.out.println("in firstIF : get Optimal");
				retStr.clear();
				maxVal = ((ScoreSet) this.valueSet.get(str)).getScore();
				maxSet = (ScoreSet) this.valueSet.get(str);
//				for(String s :str)
//					retStr.add(AgentInstances.getAgent(s));
				for(int i=0;i<str.size();i++)
				{
					String s = (String) str.get(i);
					retStr.add(agentInstances.getAgent(s));
				}
				System.out.println(retStr.toString() + maxVal );
				System.out.println(maxSet.toString());
			}
		}
		
		Iterator minIt = this.minScoreSet.keySet().iterator();
		while(minIt.hasNext())
		{
			System.out.println("in secondWhile : get Optimal");
			String l = (String) minIt.next();
			Double val = (Double) this.minScoreSet.get(l);
			if(minVal > val.doubleValue())
			{
				minVal = val.doubleValue();
				minStr = l.toString();
			}
		}
		
		CurrentScore.OptimalScore = new Double(maxVal);
		CurrentScore.OptimalSet = maxSet;
		
		CurrentScore.optimalAgentList = retStr;
		
		/*LoggerUtil.write("Dynamic Cost:"+CurrentScore.OptimalSet.getCost());
		LoggerUtil.write("Dynamic Benefit:"+CurrentScore.OptimalSet.getBenefit());
		LoggerUtil.write("Dynamic Score:"+CurrentScore.OptimalScore);
		LoggerUtil.write("Dynamic Conf:"+CurrentScore.optimalAgentList);*/
		Class[] drawSelectedParm = {String.class,String.class};
		String scoreString  = "Cost:";
		String temp = String.format("%.3f", new Double[] {new Double(maxSet.getCost())});
		scoreString += temp;
		temp = "; Benefit:";
		scoreString += temp;
		temp = String.format("%.3f",new Double[] {new Double(maxSet.getBenefit())})+ "; Col.Score:";
		scoreString += temp;
		temp = String.format("%.3f",new Double[] {CurrentScore.OptimalScore});
		scoreString += temp;
//		

		Object[] drawSelectedParmObj = {retStr.toString(),scoreString};
		
		try {
			Method drawSelected = drawUI.getClass().getDeclaredMethod("drawSelected", drawSelectedParm);
			try {
				drawSelected.invoke(drawUI, drawSelectedParmObj);
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
		
		Class[] drawAgentParm = {Integer.class,String.class,String.class};
		Method drawAgents;
		try {
			drawAgents = drawUI.getClass().getDeclaredMethod("drawAgents", drawAgentParm);
			for(int i=0;i<cellSize;i++)
			{
				Object[] drawAgentParmObj = {new Integer(i),((BaseAgent)CurrentScore.optimalAgentList.get(i)).getAgentID(),
						((BaseAgent)CurrentScore.optimalAgentList.get(i)).getAgentType()};
				try {
					drawAgents.invoke(drawUI, drawAgentParmObj);
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
			}
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Max:"+CurrentScore.OptimalScore);
		maxList.add(CurrentScore.OptimalScore);
		System.out.println(MIN+minVal);
		minList.add(new Double(minVal));
		//LoggerUtil.write(minStr);
		
		//LoggerUtil.write("OPT:"+CurrentScore.optimalAgentList);
		
		return retStr;
	}
	private class PermutationThread extends Thread implements Runnable
	{
		Generator PermutationGen;
		
		public PermutationThread(Generator PermutationGen)
		{
			this.PermutationGen = PermutationGen;
		}
		public void run() {
			
			double maxOptimalValue = 0.0;
			double maxCost = 0.0;
			double maxBenefit = 0.0;
			double minCost = 0.0;
			double minBenefit = 0.0;
			double minScore = 1;
			String optimalConf = null;
			ArrayList agentList = new ArrayList();
			ArrayList minAgentList = new ArrayList();
			Iterator it = PermutationGen.iterator();

//			for (ICombinatoricsVector Cperm : PermutationGen) {

				//Logger.write(Cperm);
				/*if(Cperm.toString().equals("CombinatoricsVector=([JE5, JE1, UAV2, JE3, UAV1, JE2, HE2, HE1, JE6], size=9)"))
				{
					Logger.write("here");
				}*/
			while(it.hasNext()){
				ICombinatoricsVector Cperm = (ICombinatoricsVector) it.next();
				boolean isValidConfiguration = false;

				double cost = 0;
				double benefit = 0;

				for( int i = 0 ; i< cellSize; i++ )
				{
					ForestCell cell = (ForestCell) cellList.get(i);
					BaseAgent agent = (BaseAgent) agentInstances.getAgent((String) Cperm.getValue(i));

					if(MonitorForestStatusPolicy.checkValidation(cell, agent,"OPT"))
					{
						isValidConfiguration = true;
					}
					else
					{
						isValidConfiguration = false;
						break;
					}
					
					//COST 占쏙옙占�
					cost = cost + getCost(cell, agent);
					
					//BENEFIT 占쏙옙占�
					benefit = benefit + getBenefit(cell, agent);
				}
				
				double confValue = getConfValue(cost, benefit);
				if(confValue > maxOptimalValue && confValue != 0.0)
				{
					agentList.clear();
					/*String outStr = "0:["+Cperm.getValue(0)+","+Cperm.getValue(1)+","+Cperm.getValue(2)+"]"+"\t"+ 
							"1:["+Cperm.getValue(3)+","+Cperm.getValue(4)+","+Cperm.getValue(5)+"]"+"\t"+
							"2:["+Cperm.getValue(6)+","+Cperm.getValue(7)+","+Cperm.getValue(8)+"]"+"\t"
							+ " Cost:" + cost + "\t"+" Benefit:" + benefit + "\n";*/
					String outStr = Cperm.toString();
					maxOptimalValue = confValue;
					maxCost = cost;
					maxBenefit = benefit;
					optimalConf = outStr;
//					for(String s: Cperm)
					for(int i=0; i<Cperm.getSize();i++)
					{
						String s = (String) Cperm.getValue(i);
						agentList.add(s);
					}
					
				}
				if(confValue < minScore)
				{
					minCost = cost;
					minBenefit = benefit;
					minAgentList.clear();
					minScore = confValue;
					for(int i=0; i<Cperm.getSize();i++)
					{
						String s = (String) Cperm.getValue(i);
						minAgentList.add(s);
					}
				}
			}
			valueSet.put(agentList, new ScoreSet(maxCost/cellSize, maxBenefit/cellSize, maxOptimalValue));
			/*System.out.println(minCost+","+minBenefit);
			System.out.println(minAgentList.toString()+","+ minScore);*/
			minScoreSet.put(minAgentList.toString(), new Double(minScore));
			
		}
	}

	
	
	
	private double getCost(ForestCell cell, BaseAgent agent)
	{
		if(agent == null)
		{
			return 1;
		}
		double cost = 0.0;
		
		cost = cost + agent.getFuelCost() + agent.getDeprecationCost() + agent.getSumOfDeviceDeprecation();
		
		if(cell.getDensity().equals("High") && agent.getAgentType().equals("Jeep"))
		{
			cost = cost + agent.getOperatorCost();
		}
		
		//there must be operator in airplane
		if(agent.getAgentType().equals("AirPlane"))
		{
			cost = cost + agent.getOperatorCost();
		}
		
		
		double nomalizedCost = (cost - agentInstances.getMinCost()) / (agentInstances.getMaxCost() - agentInstances.getMinCost());
		
		//Logger.write("c:"+cost+" / nc:"+ nomalizedCost);
		
		return nomalizedCost;
	}
	
	private double getBenefit(ForestCell cell, BaseAgent agent)
	{
		if(agent == null)
		{
			return 0;
		}
		double correctness = agent.getAvgOfSensor();
		double completeness = 0.0;
		
		//占쏙옙占싹띰옙
		if(cell.getFeatureList().contains("Mountain"))
		{
			//占쏙옙철占�
			if(cell.getWeather().getCondition().equals("Rainy"))
			{
				completeness = agent.getMountainRainMovingDistance()/monitoringDistance;
			}

			else
			{
				completeness = agent.getMountainMovingDistance()/monitoringDistance;
			}
		}

		else
		{
			//占쏙옙철占�
			if(cell.getWeather().getCondition().equals("Rainy"))
			{
				completeness = agent.getRainMovingDistance()/monitoringDistance;
			}

			else
			{
				completeness = agent.getNormalMovingDistance()/monitoringDistance;
			}
		}
		
		double normalizedBenefit = 0.3 * correctness + 0.7 * completeness;
		//Logger.write(correctness+":"+completeness+":"+normalizedBenefit);
		
		return normalizedBenefit;
	}
	private double getConfValue(double cost, double benefit) {
		cost = cost / cellSize;//CellInstances.getCellList().size();
		benefit = benefit / cellSize;//CellInstances.getCellList().size();
		double colVal = benefit - cost;
		
		//Logger.write(cost +":"+benefit+":"+colVal);
		
		return colVal;
	}

}
