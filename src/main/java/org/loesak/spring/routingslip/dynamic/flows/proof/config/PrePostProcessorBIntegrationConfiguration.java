package org.loesak.spring.routingslip.dynamic.flows.proof.config;

import org.loesak.spring.routingslip.dynamic.flows.proof.config.BaseIntegrationConfiguration.ProcessChainRoutingSlipRouteStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
@Order(1)
public class PrePostProcessorBIntegrationConfiguration {

	@Configuration
	@Order(1)
	@Profile("PreProcessorB")
	public static class PreProcessorBIntegrationConfiguration {
		@Bean
		public MessageChannel preProcessorBMessageChannel() {
			return MessageChannels.direct("preProcessorBMessageChannel").get();
		}
		
		@Bean
		public IntegrationFlow preProcessorBFlow(MessageChannel preProcessorBMessageChannel, final ProcessChainRoutingSlipRouteStrategy preProcessingRoutingSlipRouteStrategy) {
			preProcessingRoutingSlipRouteStrategy.registerMessageChannel(preProcessorBMessageChannel);
			
			return IntegrationFlows
					.from(preProcessorBMessageChannel)
					.transform(new GenericTransformer<String, String>() {
						@Override
						public String transform(String source) {
							return source + "->pre-process B";
						}
					})
					.get();
		}
	}
	
	@Configuration
	@Order(3)
	@Profile("PostProcessorB")
	public static class PostProcessorBIntegrationConfiguration {
		@Bean
		public MessageChannel postProcessorBMessageChannel() {
			return MessageChannels.direct("postProcessorBMessageChannel").get();
		}
		
		@Bean
		public IntegrationFlow postProcessorBFlow(MessageChannel postProcessorBMessageChannel, final ProcessChainRoutingSlipRouteStrategy postProcessingRoutingSlipRouteStrategy) {
			postProcessingRoutingSlipRouteStrategy.registerMessageChannel(postProcessorBMessageChannel);
			
			return IntegrationFlows
					.from(postProcessorBMessageChannel)
					.transform(new GenericTransformer<String, String>() {
						@Override
						public String transform(String source) {
							return source + "->post-process B";
						}
					})
					.get();
		}
	}
	
}
