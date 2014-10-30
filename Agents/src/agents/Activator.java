package agents;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import agents.service.AgentInstancesService;
import agents.variable.BaseAgent;
import agents.variable.Jeep;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ArrayList agentList;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		agentList = new ArrayList();
		context.registerService(AgentInstancesService.class.getName(), new AgentInstances(agentList),null);
		makeAgent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	private void makeAgent()
	{
		URL configURL = context.getBundle().getResource("/agentSpec.txt");

		if (configURL != null) {
			InputStream input = null;
			try {
				input = configURL.openStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(input));

				String s;
				while ((s = br.readLine()) != null) {

					if(s.startsWith("//"))
						continue;
					String[] spec;
					spec = s.split("\t");

					if(spec[1].equals("Jeep"))
					{
						Jeep ag = new Jeep(spec[0], spec[1], Double.parseDouble(spec[2]),
								Double.parseDouble(spec[3]),Double.parseDouble(spec[4])
								,Double.parseDouble(spec[5]),Double.parseDouble(spec[6]),Double.parseDouble(spec[7]),
								Double.parseDouble(spec[8]),Double.parseDouble(spec[9]),Double.parseDouble(spec[10])
								,Double.parseDouble(spec[11]),
								Double.parseDouble(spec[12]),Double.parseDouble(spec[13]),
								Double.parseDouble(spec[14]),
								Integer.parseInt(spec[15]), Integer.parseInt(spec[16]));
						agentList.add(ag);
					}

					else
					{
						BaseAgent ag = new BaseAgent(spec[0], spec[1], Double.parseDouble(spec[2]),
								Double.parseDouble(spec[3]),Double.parseDouble(spec[4])
								,Double.parseDouble(spec[5]),Double.parseDouble(spec[6]),Double.parseDouble(spec[7]),
								Double.parseDouble(spec[8]),Double.parseDouble(spec[9]),Double.parseDouble(spec[10])
								,Double.parseDouble(spec[11]),
								Double.parseDouble(spec[12]),Double.parseDouble(spec[13]),
								Double.parseDouble(spec[14]),
								Integer.parseInt(spec[15]), Integer.parseInt(spec[16]));
						agentList.add(ag);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
}
