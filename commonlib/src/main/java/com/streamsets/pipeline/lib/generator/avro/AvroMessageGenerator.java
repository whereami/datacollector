/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.lib.generator.avro;

import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.lib.generator.DataGeneratorException;
import com.streamsets.pipeline.lib.util.AvroTypeUtil;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class AvroMessageGenerator extends BaseAvroDataGenerator {

  private DatumWriter<Object> datumWriter;
  private BinaryEncoder binaryEncoder;
  private final OutputStream outputStream;

  public AvroMessageGenerator(
      boolean schemaInHeader,
      OutputStream outputStream,
      Schema schema,
      Map<String, Object> defaultValueMap
  ) throws IOException {
    super(schemaInHeader, schema, defaultValueMap);
    this.outputStream = outputStream;
    this.binaryEncoder = EncoderFactory.get().binaryEncoder(outputStream, null);

    if(!schemaInHeader) {
      initialize();
    }
  }

  @Override
  protected void initializeWriter() {
    datumWriter = new GenericDatumWriter<>(schema);
  }

  @Override
  public void writeRecord(Record record) throws IOException, DataGeneratorException {
    try {
      datumWriter.write(
          AvroTypeUtil.sdcRecordToAvro(record, schema, defaultValueMap),
          binaryEncoder
      );
    } catch (StageException e) {
      throw new DataGeneratorException(e.getErrorCode(), e.getParams()); // params includes cause
    }
  }

  @Override
  protected Flushable getFlushable() {
    return binaryEncoder;
  }

  @Override
  protected Closeable getCloseable() {
    return outputStream;
  }
}
