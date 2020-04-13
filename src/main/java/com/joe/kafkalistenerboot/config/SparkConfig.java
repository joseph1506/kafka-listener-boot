package com.joe.kafkalistenerboot.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;

@Configuration
public class SparkConfig {
    @Value("${bootstrapAddress}")
    String bootstrapAddress;

    @Value("${groupId}")
    String groupId;

    /*@Bean
    public SparkConf sparkConf(){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("StreamingApp");
        sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");
        sparkConf.set("spark.master","local");
        return sparkConf;
    }*/

    /*@Bean
    public JavaStreamingContext javaStreamingContext(){
        return new JavaStreamingContext(sparkConf(), Durations.seconds(1));
    }*/

    /*@Bean
    public Map<String, Object> kafkaParams(){
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", this.bootstrapAddress);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", this.groupId);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        return kafkaParams;
    }*/

    /*@Bean
    public Set<String> topics(){
        return new HashSet<String>(Arrays.asList("stream-topic"));
    }*/
}
