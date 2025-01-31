/*
 * Copyright 2020 Alibaba Group Holding Limited.
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

package com.alibaba.graphscope.common.ir.type;

import com.alibaba.graphscope.common.ir.tools.config.GraphOpt;

import org.apache.calcite.linq4j.Ord;
import org.apache.calcite.rel.type.RelDataTypeFamily;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelRecordType;
import org.apache.calcite.rel.type.StructKind;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Denote DataType of an entity or a relation, including opt, label and attributes
 */
public class GraphSchemaType extends RelRecordType {
    protected GraphOpt.Source scanOpt;
    protected GraphLabelType labelType;

    /**
     * @param scanOpt   entity or relation
     * @param labelType
     * @param fields    attribute fields, each field denoted by {@link RelDataTypeField} which consist of property name, property id and type
     */
    public GraphSchemaType(
            GraphOpt.Source scanOpt, GraphLabelType labelType, List<RelDataTypeField> fields) {
        this(scanOpt, labelType, fields, false);
    }

    protected GraphSchemaType(
            GraphOpt.Source scanOpt, List<RelDataTypeField> fields, boolean isNullable) {
        this(scanOpt, GraphLabelType.DEFAULT, fields, isNullable);
    }

    /**
     * add a constructor to accept {@code isNullable}, a nullable GraphSchemaType will be created after left outer join
     * @param scanOpt
     * @param labelType
     * @param fields
     * @param isNullable
     */
    public GraphSchemaType(
            GraphOpt.Source scanOpt,
            GraphLabelType labelType,
            List<RelDataTypeField> fields,
            boolean isNullable) {
        super(StructKind.NONE, fields, isNullable);
        this.scanOpt = scanOpt;
        this.labelType = labelType;
    }

    public GraphOpt.Source getScanOpt() {
        return scanOpt;
    }

    public GraphLabelType getLabelType() {
        return labelType;
    }

    @Override
    protected void generateTypeString(StringBuilder sb, boolean withDetail) {
        sb.append("Graph_Schema_Type");

        sb.append("(");
        Iterator var3 =
                Ord.zip((List) Objects.requireNonNull(this.fieldList, "fieldList")).iterator();

        while (var3.hasNext()) {
            Ord<RelDataTypeField> ord = (Ord) var3.next();
            if (ord.i > 0) {
                sb.append(", ");
            }

            RelDataTypeField field = ord.e;
            if (withDetail) {
                sb.append(field.getType().getFullTypeString());
            } else {
                sb.append(field.getType().toString());
            }

            sb.append(" ");
            sb.append(field.getName());
        }

        sb.append(")");
    }

    @Override
    protected void computeDigest() {
        StringBuilder sb = new StringBuilder();
        generateTypeString(sb, false);
        digest = sb.toString();
    }

    @Override
    public boolean isStruct() {
        return false;
    }

    @Override
    public RelDataTypeFamily getFamily() {
        return scanOpt;
    }
}
