package userbehaviormodel;

import java.io.IOException;
import java.util.ArrayList;





import java.util.List;


import selab.dev.uiselfadaptive.data.CountInfoData;
import selab.dev.uiselfadaptive.util.LogUtil;
import userbehaviormodelgenerator.service.UBMGeneratorService;

public class UserBehaviorServiceImpl implements UBMGeneratorService {

	private String dirPath;

	public UserBehaviorServiceImpl() {

	}

	private void init(String dirPath) {
		System.out.println("UserBehavior: start!");
		this.dirPath = dirPath;
	}

	public List genCurBM(String dirPath) {
		System.out.println("UBMGen: start");
		init (dirPath);
		try {
			ArrayList tabNames = (ArrayList) LogUtil.analyzeLog(dirPath);
			System.out.println("UBMGen: tabnames"+tabNames.toString());
			
			if(tabNames!= null) {
				System.out.println("UBMGen: in if(TabNames not null");
				CountInfoData.getInstance().incrementCountAll(tabNames);

				return CountInfoData.getInstance().getOrderedTabNames();
			}
			else
				System.out.println("UserBehavior: LogUtill.analyzeLog Error : return null");

		} catch (IOException e) {

			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}



}