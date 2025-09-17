package de.ruu.app.backend.api.ws.rs.health;

import com.sun.management.OperatingSystemMXBean;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Startup;

import java.lang.management.ManagementFactory;

@Startup
@ApplicationScoped
public class SystemStartupCheck implements HealthCheck
{
	@Override
	public HealthCheckResponse call()
	{
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

		double cpuUsed  = bean.getCpuLoad();
		String cpuUsage = String.valueOf(cpuUsed);

		return
				HealthCheckResponse
						.named(getClass().getSimpleName() + " Startup Check")
						.status(cpuUsed < 0.95)
						.build();
	}
}