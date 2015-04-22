/*
 * Copyright 2014, Stratio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.index.schema.mapping;

import com.stratio.cassandra.index.schema.Schema;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValuesType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ColumnMapperStringTest {

    @Test
    public void testConstructorWithoutArgs() {
        ColumnMapperString mapper = new ColumnMapperString(null, null, null);
        Assert.assertEquals(ColumnMapper.INDEXED, mapper.isIndexed());
        Assert.assertEquals(ColumnMapper.SORTED, mapper.isSorted());
        Assert.assertEquals(ColumnMapperString.DEFAULT_CASE_SENSITIVE, mapper.isCaseSensitive());
    }

    @Test
    public void testConstructorWithAllArgs() {
        ColumnMapperString mapper = new ColumnMapperString(false, true, false);
        Assert.assertFalse(mapper.isIndexed());
        Assert.assertTrue(mapper.isSorted());
        Assert.assertFalse(mapper.isCaseSensitive());
    }

    @Test()
    public void testValueNull() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", null);
        Assert.assertNull(parsed);
    }

    @Test
    public void testValueInteger() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3);
        Assert.assertEquals("3", parsed);
    }

    @Test
    public void testValueLong() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3l);
        Assert.assertEquals("3", parsed);
    }

    @Test
    public void testValueFloatWithoutDecimal() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3f);
        Assert.assertEquals("3.0", parsed);
    }

    @Test
    public void testValueFloatWithDecimalFloor() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3.5f);
        Assert.assertEquals("3.5", parsed);

    }

    @Test
    public void testValueFloatWithDecimalCeil() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3.6f);
        Assert.assertEquals("3.6", parsed);
    }

    @Test
    public void testValueDoubleWithoutDecimal() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3d);
        Assert.assertEquals("3.0", parsed);
    }

    @Test
    public void testValueDoubleWithDecimalFloor() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3.5d);
        Assert.assertEquals("3.5", parsed);

    }

    @Test
    public void testValueDoubleWithDecimalCeil() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", 3.6d);
        Assert.assertEquals("3.6", parsed);

    }

    @Test
    public void testValueStringWithoutDecimal() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", "3");
        Assert.assertEquals("3", parsed);
    }

    @Test
    public void testValueStringWithDecimalFloor() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", "3.2");
        Assert.assertEquals("3.2", parsed);
    }

    @Test
    public void testValueStringWithDecimalCeil() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", "3.6");
        Assert.assertEquals("3.6", parsed);

    }

    @Test
    public void testValueUUID() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String parsed = mapper.base("test", UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        Assert.assertEquals("550e8400-e29b-41d4-a716-446655440000", parsed);
    }

    @Test
    public void testFieldsIndexedSorted() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        List<Field> fields = mapper.fields("name", "hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(field);
        Assert.assertEquals("hello", field.stringValue());
        Assert.assertEquals("name", field.name());
        Assert.assertEquals(false, field.fieldType().stored());
        field = fields.get(1);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testFieldsIndexedUnsorted() {
        ColumnMapperString mapper = new ColumnMapperString(true, false, true);
        List<Field> fields = mapper.fields("name", "hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(1, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(field);
        Assert.assertEquals("hello", field.stringValue());
        Assert.assertEquals("name", field.name());
        Assert.assertEquals(false, field.fieldType().stored());
    }

    @Test
    public void testFieldsUnindexedSorted() {
        ColumnMapperString mapper = new ColumnMapperString(false, true, true);
        List<Field> fields = mapper.fields("name", "hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(1, fields.size());
        Field field = fields.get(0);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testFieldsUnindexedUnsorted() {
        ColumnMapperString mapper = new ColumnMapperString(false, false, true);
        List<Field> fields = mapper.fields("name", "hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(0, fields.size());
    }

    @Test
    public void testCaseSensitiveNull() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, null);
        List<Field> fields = mapper.fields("name", "Hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(field);
        Assert.assertEquals("Hello", field.stringValue());
    }

    @Test
    public void testCaseSensitiveTrue() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        List<Field> fields = mapper.fields("name", "Hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(field);
        Assert.assertEquals("Hello", field.stringValue());
        field = fields.get(1);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testCaseSensitiveDefault() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        List<Field> fields = mapper.fields("name", "Hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Assert.assertNotNull(field);
        Assert.assertEquals("Hello", field.stringValue());
        field = fields.get(1);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testCaseSensitiveFalse() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, false);
        List<Field> fields = mapper.fields("name", "Hello");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        Field field = fields.get(0);
        Assert.assertNotNull(field);
        Assert.assertEquals("hello", field.stringValue());
        field = fields.get(1);
        Assert.assertEquals(DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testExtractAnalyzers() {
        ColumnMapperString mapper = new ColumnMapperString(true, true, true);
        String analyzer = mapper.getAnalyzer();
        Assert.assertEquals(ColumnMapper.KEYWORD_ANALYZER, analyzer);
    }

    @Test
    public void testParseJSONWithoutArgs() throws IOException {
        String json = "{fields:{age:{type:\"string\"}}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNotNull(columnMapper);
        Assert.assertEquals(ColumnMapperString.class, columnMapper.getClass());
        Assert.assertEquals(ColumnMapper.INDEXED, columnMapper.isIndexed());
        Assert.assertEquals(ColumnMapper.SORTED, columnMapper.isSorted());
        Assert.assertEquals(ColumnMapperString.DEFAULT_CASE_SENSITIVE, ((ColumnMapperString)columnMapper).isCaseSensitive());
    }

    @Test
    public void testParseJSONWithAllArgs() throws IOException {
        String json = "{fields:{age:{type:\"string\", indexed:\"false\", sorted:\"true\", case_sensitive:\"false\"}}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNotNull(columnMapper);
        Assert.assertEquals(ColumnMapperString.class, columnMapper.getClass());
        Assert.assertFalse(columnMapper.isIndexed());
        Assert.assertTrue(columnMapper.isSorted());
        Assert.assertFalse(((ColumnMapperString)columnMapper).isCaseSensitive());
    }

    @Test
    public void testParseJSONEmpty() throws IOException {
        String json = "{fields:{}}";
        Schema schema = Schema.fromJson(json);
        ColumnMapper columnMapper = schema.getMapper("age");
        Assert.assertNull(columnMapper);
    }

    @Test(expected = IOException.class)
    public void testParseJSONInvalid() throws IOException {
        String json = "{fields:{age:{}}";
        Schema.fromJson(json);
    }
}