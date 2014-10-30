package badsymptomchecker.rule;


import java.util.List;


public class WrongTabRule implements Rule{
	
	private int ruleType;

	public boolean myRule(List currentBM,String designModel) {
		ruleType = 1;
		String recommendTab = currentBM.toString();
		
		if(recommendTab.equals(designModel)) {
			return false;
		} else {
			return true;
		}		

	}
	
	public int getRuleType() {
		return ruleType;
	}

}
