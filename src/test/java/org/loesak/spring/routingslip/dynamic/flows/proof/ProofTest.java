package org.loesak.spring.routingslip.dynamic.flows.proof;

import org.junit.Test;
import org.loesak.spring.routingslip.dynamic.flows.proof.config.BaseIntegrationConfiguration;
import org.loesak.spring.routingslip.dynamic.flows.proof.config.PostProcessorCIntegrationConfiguration;
import org.loesak.spring.routingslip.dynamic.flows.proof.config.PrePostProcessorBIntegrationConfiguration;
import org.loesak.spring.routingslip.dynamic.flows.proof.config.PreProcessorAIntegrationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class ProofTest {
	
	@Test
	public void verify_scenario_one() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("PreProcessorA", "PreProcessorB", "PostProcessorB", "PostProcessorC");
		context.register(BaseIntegrationConfiguration.class, PreProcessorAIntegrationConfiguration.class, PrePostProcessorBIntegrationConfiguration.class, PostProcessorCIntegrationConfiguration.class);
		context.refresh();
		
		DirectChannel inputMessageChannel = context.getBean("inputMessageChannel", DirectChannel.class);
		DirectChannel outputMessageChannel = context.getBean("outputMessageChannel", DirectChannel.class);
		
		outputMessageChannel.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println("scenario one: " + arg0.getPayload());
			}
		});
		
		Message<String> startMessage = MessageBuilder.withPayload("start").build();
		inputMessageChannel.send(startMessage);
		
		context.close();
	}

	@Test
	public void verify_scenario_two() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("PreProcessorA", "PostProcessorB", "PostProcessorC");
		context.register(BaseIntegrationConfiguration.class, PreProcessorAIntegrationConfiguration.class, PrePostProcessorBIntegrationConfiguration.class, PostProcessorCIntegrationConfiguration.class);
		context.refresh();
		
		DirectChannel inputMessageChannel = context.getBean("inputMessageChannel", DirectChannel.class);
		DirectChannel outputMessageChannel = context.getBean("outputMessageChannel", DirectChannel.class);
		
		outputMessageChannel.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println("scenario two: " + arg0.getPayload());
			}
		});
		
		Message<String> startMessage = MessageBuilder.withPayload("start").build();
		inputMessageChannel.send(startMessage);
		
		context.close();
	}

	@Test
	public void verify_scenario_three() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("PreProcessorA", "PostProcessorB");
		context.register(BaseIntegrationConfiguration.class, PreProcessorAIntegrationConfiguration.class, PrePostProcessorBIntegrationConfiguration.class, PostProcessorCIntegrationConfiguration.class);
		context.refresh();
		
		DirectChannel inputMessageChannel = context.getBean("inputMessageChannel", DirectChannel.class);
		DirectChannel outputMessageChannel = context.getBean("outputMessageChannel", DirectChannel.class);
		
		outputMessageChannel.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println("scenario three: " + arg0.getPayload());
			}
		});
		
		Message<String> startMessage = MessageBuilder.withPayload("start").build();
		inputMessageChannel.send(startMessage);
		
		context.close();
	}

	@Test
	public void verify_scenario_four() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("PostProcessorB");
		context.register(BaseIntegrationConfiguration.class, PreProcessorAIntegrationConfiguration.class, PrePostProcessorBIntegrationConfiguration.class, PostProcessorCIntegrationConfiguration.class);
		context.refresh();
		
		DirectChannel inputMessageChannel = context.getBean("inputMessageChannel", DirectChannel.class);
		DirectChannel outputMessageChannel = context.getBean("outputMessageChannel", DirectChannel.class);
		
		outputMessageChannel.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println("scenario four: " + arg0.getPayload());
			}
		});
		
		Message<String> startMessage = MessageBuilder.withPayload("start").build();
		inputMessageChannel.send(startMessage);
		
		context.close();
	}

	@Test
	public void verify_scenario_five() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(BaseIntegrationConfiguration.class, PreProcessorAIntegrationConfiguration.class, PrePostProcessorBIntegrationConfiguration.class, PostProcessorCIntegrationConfiguration.class);
		context.refresh();
		
		DirectChannel inputMessageChannel = context.getBean("inputMessageChannel", DirectChannel.class);
		DirectChannel outputMessageChannel = context.getBean("outputMessageChannel", DirectChannel.class);
		
		outputMessageChannel.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println("scenario five: " + arg0.getPayload());
			}
		});
		
		Message<String> startMessage = MessageBuilder.withPayload("start").build();
		inputMessageChannel.send(startMessage);
		
		context.close();
	}
}
