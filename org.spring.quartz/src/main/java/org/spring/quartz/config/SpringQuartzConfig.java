package org.spring.quartz.config;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.spring.quartz.beans.SimpleCronBO;
import org.spring.quartz.beans.SimpleTriggerBO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class SpringQuartzConfig {

	// SimpleTrigger triggers jobDetailSimpleTrigger job that invokes
	// SimpleTriggerBO
	@Bean
	public SimpleTrigger simpleJobTrigger() throws ClassNotFoundException, NoSuchMethodException {
		SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
		simpleTriggerFactoryBean.setJobDetail(jobDetailSimpleTrigger());
		simpleTriggerFactoryBean.setStartDelay(10000);
		simpleTriggerFactoryBean.setRepeatInterval(3000);
		simpleTriggerFactoryBean.afterPropertiesSet();

		return simpleTriggerFactoryBean.getObject();

	}

	// MethodInvokingJobDetailFactoryBean is not suitable for persistance tasks due
	// to
	// serialization issues. For persistence task use
	// org.springframework.scheduling.quartz.JobDetailFactoryBean
	@Bean
	public JobDetail jobDetailCron() throws ClassNotFoundException, NoSuchMethodException {
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		jobDetailFactoryBean.setTargetObject(createCronSimpleBO());
		jobDetailFactoryBean.setTargetMethod("executeMethod");
		jobDetailFactoryBean.setConcurrent(false);
		jobDetailFactoryBean.afterPropertiesSet();

		return jobDetailFactoryBean.getObject();
	}

	@Bean
	public JobDetail jobDetailSimpleTrigger() throws ClassNotFoundException, NoSuchMethodException {
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		jobDetailFactoryBean.setTargetObject(createSimpleTrigerBO());
		jobDetailFactoryBean.setTargetMethod("executeMethod");
		jobDetailFactoryBean.setConcurrent(false);
		jobDetailFactoryBean.afterPropertiesSet();

		return jobDetailFactoryBean.getObject();
	}

	@Bean
	public SimpleCronBO createCronSimpleBO() {
		return new SimpleCronBO();
	}

	@Bean
	public SimpleTriggerBO createSimpleTrigerBO() {
		return new SimpleTriggerBO();
	}

	// CronTriggers triggers jobDetailCron job that invokes SimpleCronBO
	@Bean
	public CronTrigger cronTrigger() throws ClassNotFoundException, NoSuchMethodException, ParseException {
		CronTriggerFactoryBean conTriggerBean = new CronTriggerFactoryBean();
		conTriggerBean.setJobDetail(jobDetailCron());
		// run ever 10 sec
		conTriggerBean.setCronExpression("0/10 * * * * ? *");
		conTriggerBean.afterPropertiesSet();

		return conTriggerBean.getObject();

	}

	// Scheduler wires 2 types of triggers CronTrigger and SimpleTrigger
	@Bean
	public SchedulerFactoryBean start() throws ClassNotFoundException, NoSuchMethodException, ParseException {
		SchedulerFactoryBean schedulerBean = new SchedulerFactoryBean();
		schedulerBean.setTriggers(cronTrigger(), simpleJobTrigger());
		return schedulerBean;
	}

}
