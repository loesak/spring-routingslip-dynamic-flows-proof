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
@Order(2)
@Profile("PreProcessorA")
public class PreProcessorAIntegrationConfiguration {
	
	@Bean
	public MessageChannel preProcessorAMessageChannel() {
		return MessageChannels.direct("preProcessorAMessageChannel").get();
	}
	
	@Bean
	public IntegrationFlow preProcessorAFlow(MessageChannel preProcessorAMessageChannel, final ProcessChainRoutingSlipRouteStrategy preProcessingRoutingSlipRouteStrategy) {
		preProcessingRoutingSlipRouteStrategy.registerMessageChannel(preProcessorAMessageChannel);
		
		return IntegrationFlows
				.from(preProcessorAMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->pre-process A";
					}
				})
				.get();
	}
	
}
