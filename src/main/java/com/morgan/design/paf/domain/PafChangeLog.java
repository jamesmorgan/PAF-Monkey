package com.morgan.design.paf.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PafChangeLog {

	private final Mode mode;
	private int buildNames;
	private int localities;
	private int mailSort;
	private int organisations;
	private int pafAddress;
	private int subBuildingName;
	private int thoroughfareDescriptor;
	private int thoroughfare;
	private int udprn;

	private PafChangeLog(final Mode mode) {
		this.mode = mode;
	}

	public static PafChangeLog createSourceLog() {
		return new PafChangeLog(Mode.SOURCE);
	}

	public static PafChangeLog createUpdateLog() {
		return new PafChangeLog(Mode.UPDATE);
	}

	public final int getBuildNames() {
		return this.buildNames;
	}

	public final int getLocalities() {
		return this.localities;
	}

	public final int getMailSort() {
		return this.mailSort;
	}

	public final int getOrganisations() {
		return this.organisations;
	}

	public final int getPafAddress() {
		return this.pafAddress;
	}

	public final int getSubBuildingName() {
		return this.subBuildingName;
	}

	public final int getThoroughfareDescriptor() {
		return this.thoroughfareDescriptor;
	}

	public final int getThoroughfare() {
		return this.thoroughfare;
	}

	public final int getUdprn() {
		return this.udprn;
	}

	public final Mode getMode() {
		return this.mode;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public void setCount(final TableDefinition definition, final int totalInsertCount) {
		if (definition.getName().equalsIgnoreCase("building_names")) {
			this.buildNames = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("localities")) {
			this.localities = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("mailsort")) {
			this.mailSort = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("organisation")) {
			this.organisations = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("paf_address")) {
			this.pafAddress = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("sub_building_name")) {
			this.subBuildingName = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("thoroughfare_descriptor")) {
			this.thoroughfareDescriptor = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("thoroughfare")) {
			this.thoroughfare = totalInsertCount;
		}
		else if (definition.getName().equalsIgnoreCase("udprn")) {
			this.udprn = totalInsertCount;
		}
	}
}
