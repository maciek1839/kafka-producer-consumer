package com.showmeyourcode.kafka.java.kafka_producer;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AvroTest {

    @Test
    void shouldWriteAvroDataToFile() throws IOException {
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("avro/t.avsc");
        if (schemaStream == null) {
            throw new IllegalArgumentException("Schema file not found in classpath");
        }

        // Convert the InputStream to String
        String schemaString = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);
        Schema schema = new Schema.Parser().parse(schemaString);

        // Create records
        GenericRecord record1 = new GenericData.Record(schema);
        record1.put("name", "John Doe");
        record1.put("age", 30L);
        record1.put("phoneNumber", "123-456-7890");

        GenericRecord record2 = new GenericData.Record(schema);
        record2.put("name", "Jane Smith");
        record2.put("age", 25L);
        record2.put("phoneNumber", "987-654-3210");

        // Specify the file path
        File avroFile = new File("src/test/resources/AvroUserRecordTest.avro");

        // Write data to Avro file
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, avroFile);
        dataFileWriter.append(record1);
        dataFileWriter.append(record2);
        dataFileWriter.close();

        // Verify the file was created and contains records
        assertTrue(avroFile.exists(), "Avro file should be created");
        assertTrue(avroFile.length() > 0, "Avro file should contain data");
    }
}
