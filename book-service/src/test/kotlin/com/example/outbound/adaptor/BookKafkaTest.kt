package com.example.outbound.adaptor

import com.example.inbound.domain.BookStatus
import com.example.outbound.dto.BookChangeStatus
import com.example.outbound.dto.BookChanged
import com.example.outbound.dto.BookStatusEventDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import java.util.*

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics=arrayOf("TOPIC_BOOK"))
//    brokerProperties = arrayOf("listeners=PLAINTEXT://127.0.0.1:9092", "port=9092"))
class BookConsumerTest {

    lateinit var consumer: Consumer<Int, String>
    lateinit var producer: Producer<Int, String>
    @Autowired
    lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker
    @Autowired
    lateinit var objectMapper: ObjectMapper
    private val TOPIC_NAME = "TOPIC_BOOK";

    @BeforeEach
    fun beforeEach() {
        consumer = configureConsumer()
        producer = configureProducer()
    }

    @AfterEach
    fun afterEach() {
        consumer.close()
        producer.close()
    }


    @Test
    fun kafkaTest() {
        // kafka producer
        val bookStatusEventDto = BookStatusEventDto("TEST_ID", BookStatus.AVAILABLE)
        val producerRecord: ProducerRecord<Int, String> =
            ProducerRecord(TOPIC_NAME, 123, objectMapper.writeValueAsString(bookStatusEventDto))
        producer.send(producerRecord)

        // kafka consumer
        val consumerRecords: ConsumerRecord<Int, String> =
            KafkaTestUtils.getSingleRecord(consumer, TOPIC_NAME)

        assertNotNull(consumerRecords)
        assertEquals(123, consumerRecords.key())
        val bookStockEventDto = objectMapper.readValue(consumerRecords.value(), BookStatusEventDto::class.java)
        assertEquals("TEST_ID", bookStockEventDto.bookId);
        assertEquals(BookStatus.AVAILABLE, bookStockEventDto.bookStatus)
    }

    private fun configureProducer(): Producer<Int, String> {
        val props = KafkaTestUtils.producerProps(embeddedKafkaBroker)
        val producer = DefaultKafkaProducerFactory<Int, String>(props).createProducer()
        return producer
    }

    private fun configureConsumer(): Consumer<Int, String> {
        val props = KafkaTestUtils.consumerProps("consumer_group", "true", embeddedKafkaBroker)
        val consumer = DefaultKafkaConsumerFactory<Int, String>(props).createConsumer()
        consumer.subscribe(Collections.singleton(TOPIC_NAME))
        return consumer
    }
}