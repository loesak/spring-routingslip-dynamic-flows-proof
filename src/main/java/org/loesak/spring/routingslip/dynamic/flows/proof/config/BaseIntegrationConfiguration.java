package org.loesak.spring.routingslip.dynamic.flows.proof.config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.routingslip.RoutingSlipRouteStrategy;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.integration.transformer.support.RoutingSlipHeaderValueMessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class BaseIntegrationConfiguration {
	
	@Bean
	public MessageChannel inputMessageChannel() {
		return MessageChannels.direct("inputMessageChannel").get();
	}
	
	@Bean
	public MessageChannel preProcessMessageChannel() {
		return MessageChannels.direct("preProcessMessageChannel").get();
	}
	
	@Bean
	public MessageChannel processMessageChannel() {
		return MessageChannels.direct("processMessageChannel").get();
	}
	
	@Bean
	public MessageChannel postProcessMessageChannel() {
		return MessageChannels.direct("postProcessMessageChannel").get();
	}
	
	@Bean
	public MessageChannel outputMessageChannel() {
		return MessageChannels.direct("outputMessageChannel").get();
	}
	
	@Bean
	public ProcessChainRoutingSlipRouteStrategy preProcessingRoutingSlipRouteStrategy() {
		return new ProcessChainRoutingSlipRouteStrategy();
	}
	
	@Bean
	public ProcessChainRoutingSlipRouteStrategy postProcessingRoutingSlipRouteStrategy() {
		return new ProcessChainRoutingSlipRouteStrategy();
	}
	
	@Bean
	public IntegrationFlow startFlow(MessageChannel inputMessageChannel, MessageChannel preProcessMessageChannel, final ProcessChainRoutingSlipRouteStrategy preProcessingRoutingSlipRouteStrategy) {
		return IntegrationFlows
				.from(inputMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->sending to pre-processing channel";
					}
				})
				.enrichHeaders(new Consumer<HeaderEnricherSpec>() {
					@Override
					public void accept(HeaderEnricherSpec spec) {
						spec.header(IntegrationMessageHeaderAccessor.ROUTING_SLIP, new RoutingSlipHeaderValueMessageProcessor(preProcessingRoutingSlipRouteStrategy, "processMessageChannel"))
						.defaultOverwrite(true);
					}})
				.channel(preProcessMessageChannel)
				.get();
	}
	
	@Bean
	public IntegrationFlow startPreProcessingFlow(final MessageChannel preProcessMessageChannel) {
		return IntegrationFlows
				.from(preProcessMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->beginning pre-processing";
					}
				})
				.get();
	}
	
	@Bean
	public IntegrationFlow processFlow(MessageChannel processMessageChannel, MessageChannel postProcessMessageChannel, final ProcessChainRoutingSlipRouteStrategy postProcessingRoutingSlipRouteStrategy) {
		return IntegrationFlows
				.from(processMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->processing message";
					}
				})
				.enrichHeaders(new Consumer<HeaderEnricherSpec>() {
					@Override
					public void accept(HeaderEnricherSpec spec) {
						spec.header(IntegrationMessageHeaderAccessor.ROUTING_SLIP, new RoutingSlipHeaderValueMessageProcessor(postProcessingRoutingSlipRouteStrategy, "outputMessageChannel"))
							.defaultOverwrite(true);
					}})
				.channel(postProcessMessageChannel)
				.get();
	}
	
	@Bean
	public IntegrationFlow startPostProcessingFlow(final MessageChannel postProcessMessageChannel) {
		return IntegrationFlows
				.from(postProcessMessageChannel)
				.transform(new GenericTransformer<String, String>() {
					@Override
					public String transform(String source) {
						return source + "->beginning post-processing";
					}
				})
				.get();
	}
	
	public static class ProcessChainRoutingSlipRouteStrategy implements RoutingSlipRouteStrategy {
		
		Queue<MessageChannel> chain = new LinkedList<MessageChannel>();

		@Override
		public Object getNextPath(Message<?> requestMessage, Object reply) {
			return this.chain.poll();
		}
		
		public void registerMessageChannel(MessageChannel messageChannel) {
			this.chain.add(messageChannel);
		}
		
	}

}
