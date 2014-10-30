package agents;

import java.util.ArrayList;

import agents.service.AgentInstancesService;
import agents.variable.BaseAgent;


public class AgentInstances implements AgentInstancesService{
	private  ArrayList  agentList;
	private  double maxCost = 0.0;
	private  double minCost = 0.0;

	public AgentInstances(ArrayList agentList)
	{
		this.agentList = agentList;
	}
	
	public  ArrayList  getAgentList() {
		return agentList;
	}

	public  void setAgentList(ArrayList  agentList) {
		this.agentList = agentList;
	}

	public  void printAgents() {
//		for(BaseAgent agent : agentList)
//		{
//			LoggerUtil.write(LoggerUtil.LOGPANE, agent.getAgentType().name());
//		}
	}

	public  ArrayList getAgentIDList() {
		ArrayList idList = new ArrayList();
//		for(BaseAgent a : agentList)
//		{
//			idList.add(a.getAgentID());
//		}
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			idList.add(a.getAgentID());
		}
		
		//idList.add(null);
		
		return idList;
	}
	
	public  Object getAgent(String id)
	{
		
//		for(BaseAgent a : agentList)
//		{
//			if(a.getAgentID().equals(id))
//			{
//				return a;
//			}
//		}
//		
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			
			if(a.getAgentID().equals(id))
				{
					return a;
				}
		}
		return null;
	}
	
	public  double getMaxFuelCost()
	{
		double retVal = 0.0;
//		for(BaseAgent a : agentList)
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal < a.getFuelCost())
				retVal = a.getFuelCost();
		}
		
		return retVal;
	}
	
	public  double getMinFuelCost()
	{
		double retVal = 1000.0;
//		for(BaseAgent a : agentList)
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal > a.getFuelCost())
				retVal = a.getFuelCost();
		}
		
		return retVal;
	}
	
	public  double getMaxDeprecationCost()
	{
		double retVal = 0.0;
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal < a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public  double getMinDeprecationCost()
	{
		double retVal = 1000.0;
//		for(BaseAgent a : agentList)
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
		
			if(retVal > a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public  double getMaxDeviceDeprecationCost()
	{
		double retVal = 0.0;
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal < a.getSumOfDeviceDeprecation())
				retVal = a.getSumOfDeviceDeprecation();
		}
		
		return retVal;
	}
	
	public  double getMinDeviceDeprecationCost()
	{
		double retVal = 1000.0;
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal > a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public  double getMaxOperatorCost()
	{
		double retVal = 0.0;
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal < a.getOperatorCost())
				retVal = a.getOperatorCost();
		}
		
		return retVal;
	}
	
	public  double getMinOperatorCost()
	{
		double retVal = 1000.0;
		for(int i=0;i<agentList.size();i++)
		{
			BaseAgent a = (BaseAgent) agentList.get(i);
			if(retVal > a.getOperatorCost())
				retVal = a.getOperatorCost();
		}
		
		return retVal;
	}
	
	public  double getMaxCost()
	{
		if(maxCost == 0.0)
			return getMaxFuelCost()+getMaxOperatorCost()+getMaxDeviceDeprecationCost()+getMaxDeprecationCost();
		else
			return maxCost;
	}
	
	public  double getMinCost()
	{
		if(minCost == 0.0)
			return getMinFuelCost()+getMinOperatorCost()+getMinDeviceDeprecationCost()+getMinDeprecationCost();
		else
			return minCost;
	}
	
	
	

}
