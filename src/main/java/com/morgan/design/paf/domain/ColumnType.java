package com.morgan.design.paf.domain;

public enum ColumnType {
	VARCHAR, INT;

	public static ColumnType fromString(final String type) {
		if ("A".equalsIgnoreCase(type.toString()) || "VARCHAR".equalsIgnoreCase(type.toString())) {
			return ColumnType.VARCHAR;
		}
		if ("N".equalsIgnoreCase(type.toString()) || "INT".equalsIgnoreCase(type.toString())) {
			return ColumnType.INT;
		}
		throw new IllegalArgumentException("Unknown Column Type: " + type);
	}

	public boolean isVarChar() {
		return this.equals(VARCHAR);
	}
}
