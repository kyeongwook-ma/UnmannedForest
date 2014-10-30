package selab.dev.unmannedforestmonitor.osgi;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kr.ac.sogang.unmannedforestmonitor.R;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import selab.dev.unmannedforestmonitor.uiservice.DrawService;
import selab.dev.unmannedforestmonitor.uiservice.DrawUI;
import android.content.res.Resources;
import android.util.Log;

public class BundleController {

	private String absolutePath;
	private Resources res;

	private File bundlesDir;
	private File newBundlesDir;
	private File cacheDir;

	private Properties felixProperties;
	private Felix felix;
	
	public BundleController(String absolutePath,Resources res, Object effectorParam) {
		this.absolutePath = absolutePath;
		this.res = res;
		
		//directory ����
		makeDir();

		//fliex �ʱ�ȭ context ���� 
		makeFelix();
		getFelix().getBundleContext().registerService(DrawService.class.getName(),new DrawUI(), null);
		
		
		/*
		getFelix().getBundleContext().registerService(
				IEffector.class.getName(), new Effector((TabView) effectorParam), null);
		*/
		/* install bundles */
		installBundles();
	
	}
	
	public void setEffector(Object effectorParam) {
		
	}

	private void installBundles()  {
		
		try {
			installAndStartBundle(R.raw.hostactivator,"hostactivator");
			
			/* User Behavior Model에서 분석 결과 얻어옴 */
			Class[] classes = {Bundle.class};
			Object[] objects = { felix.getBundle() };
			startServiceMethod("HostActivator","hostactivator.HostActivator" ,"setFelix"
					, classes, objects);
			
			
			installAndStartBundle(R.raw.parameterobjects, "parameterobjects");
			installAndStartBundle(R.raw.badsymptomcheckerservices, "badsymptomcheckerservices");
			installAndStartBundle(R.raw.ubmgeneratorservice, "ubmgeneratorservice");
			installAndStartBundle(R.raw.usabilityimproverservice, "usabilityimproverservice");
			installAndStartBundle(R.raw.mapebundleservices, "mapebundleservices");
			
			installAndStartBundle(R.raw.agentsservice, "agentsservice");
			installAndStartBundle(R.raw.environmentsservice, "environmentsservice");
			installAndStartBundle(R.raw.agents, "agents");
			installAndStartBundle(R.raw.environments, "environments");
			
			installAndStartBundle(R.raw.configurationcomposer, "configurationcomposer");
			installAndStartBundle(R.raw.nulladaptreasoner, "nulladaptreasonser");
			installAndStartBundle(R.raw.collaborationmonitor, "collaborationmonitor");
			
			
			installAndStartBundle(R.raw.mapebundle, "mapebundle");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeDir(){

		bundlesDir = new File(absolutePath+ "/felix/bundle");
		if(!bundlesDir.exists()){
			if(!bundlesDir.mkdirs()){
				throw new IllegalStateException("Unable to create bundlesDir dir");
			}
		}
		newBundlesDir = new File(absolutePath+"/felix/newbundle");
		if(!newBundlesDir.exists()){
			if(!newBundlesDir.mkdirs()){
				throw new IllegalStateException("Unable to create newBundleDir dir");
			}
		}
		cacheDir = new File(absolutePath+"/felix/cache");
		if (!cacheDir.exists()) {
			if (!cacheDir.mkdirs()) {
				throw new IllegalStateException("Unable to create felixcache dir");
			}
		}
	}
	//���� ��ġ 
	public void installAndStartBundle(int bundleId, String bundleName )throws Exception {
		InputStream is = res.openRawResource(bundleId);
		Bundle bundle = getFelix().getBundleContext()
				.installBundle(absolutePath+"felix/bundle/" + bundleName + ".jar", is);
		
		bundle.start();
		Log.i(bundleName,Integer.toString(bundle.getState()));
	}

	private void makeFelix() {
		//Properties ����
		felixProperties = new FelixConfig(absolutePath).getConfigProps();
				
		
		//FelixFrameWork ������ ����
		felix = new Felix(felixProperties);
		
		
		try {
			felix.start();

			Log.i("Felix ����","����");
		} catch (BundleException e) {
			e.printStackTrace();
		}

	
		
	}


	public Bundle getFelix() {
		return felix;	
	}
	
	public Object startServiceMethod(String bundleSName,String serviceImplName,String methodgetName,Class[] parameterTypes,Object[] parameters) 
	{
		Object returnObject = null;
		for(Bundle b : felix.getBundleContext().getBundles())
		{
			if(b.getSymbolicName().equals(bundleSName))
			{
				for(ServiceReference<?> ref : b.getRegisteredServices())
				{
					Object service = b.getBundleContext().getService(ref);
					if(service.getClass().getName().equals(serviceImplName))
					{
						try {
							Method method = service.getClass().getDeclaredMethod(methodgetName, parameterTypes);
							try {
							returnObject = method.invoke(service, parameters);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}



		return returnObject;
	}


}
