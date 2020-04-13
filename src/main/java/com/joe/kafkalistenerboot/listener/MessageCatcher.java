package com.joe.kafkalistenerboot.listener;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.*;

@Service
public class MessageCatcher {

    /*@Autowired
    JavaStreamingContext javaStreamingContext;*/

    /*@Autowired
    Map<String, Object> kafkaParams;*/

    /*@Autowired
    Set<String> topics;*/

    /*@Autowired
    SparkConf sparkConf;*/

    /*@KafkaListener(topics = "stream-topic", groupId = "sample-group", containerFactory = "messageKafkaListenerFactory")
    public void consume(Message message) {
        System.out.println("Consumed message: " + message);
    }*/

    public void consume() throws InterruptedException {
        SparkConf sparkConf = sparkConf();
        SparkSession spark= SparkSession.builder()
                .config(sparkConf)
                //.appName("Spark SQl")
                //.config("spark.cassandra.connection.host", "127.0.0.1")
                //.config("spark.cassandra.connection.port", "9042")
                //.config("spark.master", "local")
                //.config("spark.driver.allowMultipleContexts", "true")
                .getOrCreate();
        JavaSparkContext ctx = JavaSparkContext.fromSparkContext(SparkContext.getOrCreate(sparkConf));

        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(ctx, Durations.seconds(3));
        //System.out.println("topics-->"+topics);
        //System.out.println("kafkaParams-->"+kafkaParams.toString());

        JavaInputDStream<ConsumerRecord<String, String>> messages =
                KafkaUtils.createDirectStream(
                        javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String> Subscribe(topics(), kafkaParams()));

        JavaDStream<String> lines=
                messages.mapToPair(record-> new Tuple2<>(record.key(),record.value()))
                .map(row-> row._2());

        //lines.print();
        lines.foreachRDD(line-> {
            Dataset<Row> data = spark.read().json(line);
            Dataset<Row> newD= data.withColumn("results",data.col("results.user"));
            System.out.println(newD);
        });
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

    /*private void processLine(SparkSession spark, JavaRDD<String> line) {
        Dataset<Row> data = spark.read().json(line);
        System.out.println(data);
        //data.withColumn("results",Dataset.explode($"results"))
    }*/

    /*private SparkSession sparkSession() {
        return SparkSession.builder()
                .appName("Spark SQl")
                .config("spark.cassandra.connection.host", "127.0.0.1")
                .config("spark.cassandra.connection.port", "9042")
                .getOrCreate();
    }*/

    public SparkConf sparkConf(){
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("StreamingApp");
        sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");
        sparkConf.set("spark.master","local[2]");
        sparkConf.set("spark.streaming.kafka.allowNonConsecutiveOffsets","true");
        //sparkConf.set("spark.streaming.kafka.consumer.poll.ms", "512");
        return sparkConf;
    }

    public Map<String, Object> kafkaParams(){
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "127.0.0.1:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "sample-group");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        return kafkaParams;
    }

    public Set<String> topics(){
        return new HashSet<String>(Arrays.asList("stream-topic-mod"));
    }


}

