package org.luizlopes.config;

import lombok.extern.slf4j.Slf4j;
import org.luizlopes.domain.Jogadores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private Jogadores jogadores;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-poc")
                .withSockJS().setSessionCookieNeeded(true);
    }

    @Bean
    public PresenceChannelInterceptor presenceChannelInterceptor() {
        return new PresenceChannelInterceptor();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(presenceChannelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(8);
        registration.setInterceptors(presenceChannelInterceptor());
    }


    public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

        @Override
        public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

            StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

            // ignore non-STOMP messages like heartbeat messages
            if (sha.getCommand() == null) {
                return;
            }

            String sessionId = sha.getSessionId();

            final Object principal = ((UsernamePasswordAuthenticationToken) message.getHeaders().get("simpUser")).getPrincipal();
            final String user = ((User) principal).getUsername();

            switch (sha.getCommand()) {
                case CONNECT:
                    log.debug("STOMP Connect [sessionId: " + sessionId + "]");
                    //jogadores.connectUser(user);
                    break;
                case CONNECTED:
                    log.debug("STOMP Connected [sessionId: " + sessionId + "]");
                    break;
                case DISCONNECT:
                    log.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                    jogadores.disconnectUser(user);
                    break;
                default:
                    break;
            }
        }
    }


}
