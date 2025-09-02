package de.ruu.lib.cdi.common;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CDIExtension implements Extension
{
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd)
	{
		log.debug("beginning the scanning process");
	}

	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat)
	{
		log.debug("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
	}

	void afterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager bm)
	{
		StringBuilder sb = new StringBuilder("finished the deployment validation process, managed beans:\n");
		bm.getBeans(Object.class).forEach(bean -> sb.append(bean.getBeanClass().getName()).append("\n"));
		log.debug(sb.toString());
	}

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd)
	{
		log.debug("finished the scanning process");
	}
}