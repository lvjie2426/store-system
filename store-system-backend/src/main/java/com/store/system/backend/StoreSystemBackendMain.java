package com.store.system.backend;

import ch.qos.logback.classic.LoggerContext;
import com.quakoo.webframework.AbstractServer;
import com.quakoo.webframework.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreSystemBackendMain extends AbstractServer {

	private final static Logger logger = LoggerFactory.getLogger(StoreSystemBackendMain.class);

	public StoreSystemBackendMain(String[] anArgs) {
		super(anArgs);
	}

	@Override
	public void init(Config config, String[] args) {

		int minThreads = 64;
		int maxThreads = 256;

		if (minThreads >= 64) {
			config.setMin_thread(minThreads);
		} else {
			minThreads = 64;
			config.setMin_thread(minThreads);
		}
		if (maxThreads >= minThreads) {
			config.setMax_thread(maxThreads);
		} else {
			config.setMax_thread(minThreads * 2);
		}

		config.setContextPath("/");
		config.setPort(Integer.parseInt(args[1]));
		if (args.length > 2)
			config.setWebapp(args[2]);

		config.setCheckTime(1001);
	}

	public static void runServer(String... args) throws Exception {
		if (args == null || args.length < 2) {
			throw new RuntimeException("server args is error");
		}
		String projectName = args[0];

		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.setPackagingDataEnabled(false);
		context.setName(projectName);
        StoreSystemBackendMain server = new StoreSystemBackendMain(args);
		server.run(args);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			if (args == null || args.length < 2) {
				String separator = System.getProperty("file.separator");
				StringBuilder webapp = new StringBuilder().append(System.getProperty("user.dir")).append(separator)
						.append("src").append(separator).append("main").append(separator).append("webapp");
				args = new String[] { "StoreSystemBackendMain", "20005", webapp.toString() };
			}
			runServer(args);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("start server error", e);
		}

	}

}
