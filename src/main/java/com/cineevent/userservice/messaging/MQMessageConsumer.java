package com.cineevent.userservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MQMessageConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message){

        log.info("Message arrived! Message: " + message);
    }
}