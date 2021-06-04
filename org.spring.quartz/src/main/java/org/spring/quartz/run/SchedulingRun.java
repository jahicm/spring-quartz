package org.spring.quartz.run;

import org.spring.quartz.config.SpringQuartzConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class SchedulingRun {

	public static void main(String[] args) {
		
		GenericApplicationContext ctx = new AnnotationConfigApplicationContext(SpringQuartzConfig.class);

		
	}

}
