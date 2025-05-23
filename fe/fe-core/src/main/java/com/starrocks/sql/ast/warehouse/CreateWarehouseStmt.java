// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.starrocks.sql.ast.warehouse;

import com.starrocks.common.util.PrintableMap;
import com.starrocks.sql.ast.AstVisitor;
import com.starrocks.sql.ast.DdlStmt;
import com.starrocks.sql.parser.NodePosition;

import java.util.Map;

public class CreateWarehouseStmt extends DdlStmt {
    private final boolean ifNotExists;
    private final String warehouseName;
    private Map<String, String> properties;
    private final String comment;

    public CreateWarehouseStmt(boolean ifNotExists,
                               String warehouseName,
                               Map<String, String> properties,
                               String comment) {
        this(ifNotExists, warehouseName, properties, comment, NodePosition.ZERO);
    }

    public CreateWarehouseStmt(boolean ifNotExists, String warehouseName,
                               Map<String, String> properties,
                               String comment,
                               NodePosition pos) {
        super(pos);
        this.ifNotExists = ifNotExists;
        this.warehouseName = warehouseName;
        this.properties = properties;
        this.comment = comment;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public boolean isSetIfNotExists() {
        return ifNotExists;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public String getComment() {
        return comment;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitCreateWarehouseStatement(this, context);
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE WAREHOUSE ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append("'").append(warehouseName).append("' ");
        if (comment != null) {
            sb.append("COMMENT \"").append(comment).append("\" ");
        }
        sb.append("WITH PROPERTIES(").append(new PrintableMap<>(properties, " = ", true, false)).append(")");
        return sb.toString();
    }
}