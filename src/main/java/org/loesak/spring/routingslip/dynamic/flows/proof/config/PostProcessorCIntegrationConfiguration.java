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
@Profile("PostProcessorC")
public class PostProcessorCIntegrationConfiguration {
	@Bean
	public MessageChannel postProcessorCMessageChannel() {
		return MessageChannels.direct("postProcessorCMessageChannel").get();
	}
	
	@Bean
	public IntegrationFlow postProcessorCFlow(MessageChannel postProcessorCMessageChannel, final ProcessChainRoutingSlipRouteStrategy postProcessingRoutingSlipRouteStrategy) {
		postProcessingRoutingSlipRouteStrategy.registerMessageChannel(postProcessorCMessageChannel);
		
		return IntegrationFlows
				.from(postProcessorCMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->post-process C";
					}
				})
				.get();
	}
}
