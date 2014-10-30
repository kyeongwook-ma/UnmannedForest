package badsymptomchecker;
import java.util.ArrayList;
import java.util.List;

import badsymptomchecker.rule.Rule;
import badsymptomchecker.rule.WrongTabRule;
import badsymptomchecker.services.BadSymptomService;

public class BadSymptomServiceImpl implements BadSymptomService {

	public BadSymptomServiceImpl() {
	}

	private ArrayList diffList;

	private void initRuleSet() {
		diffList = new ArrayList();
	}


	public ArrayList reason(List currentBM, String designModel) {
		initRuleSet();

		//LogData check 
		compareBM(currentBM,designModel);

		//������ ��� Diagnois ������
		return diffList;
	}
	private void compareBM(List currentBM,String designModel)
	{
		List ruleList = new ArrayList();
		initRuleList(ruleList);
		
		for(Object rule : ruleList) {
			Rule castedRule = (Rule) rule;
			System.out.println(castedRule.getRuleType());
			if(castedRule.myRule(currentBM, designModel))
				diffList.add(new Integer(castedRule.getRuleType()));
		}

		System.out.println("BadSystom : diff check done");
		
		
	}
	private void initRuleList(List ruleList) {
		ruleList.add(new WrongTabRule());
	}

}
