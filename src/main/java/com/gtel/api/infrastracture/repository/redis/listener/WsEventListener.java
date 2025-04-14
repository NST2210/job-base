package com.gtel.api.infrastracture.repository.redis.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Qualifier("WsEventListener")
public class WsEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //TODO
    }
}
