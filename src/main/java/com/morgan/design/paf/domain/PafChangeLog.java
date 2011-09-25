package com.morgan.design.paf.domain;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PafChangeLog {

	private static final String UDPRN_KEY = "udprn";
	private static final String THOROUGHFARE_DESCRIPTOR_KEY = "thoroughfare_descriptor";
	private static final String THOROUGHFARE_KEY = "thoroughfare";
	private static final String SUB_BUILDING_NAME = "sub_building_name";
	private static final String PAF_ADDRESS__KEY = "paf_address";
	private static final String ORGANISATION_KEY = "organisations";
	private static final String MAILSORT_KEY = "mailsort";
	private static final String LOCALITIES_KEY = "localities";
	private static final String BUILDING_NAMES_KEY = "building_names";

	private final Mode mode;
	private Date startDate;
	private Date endDate;

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

	public final void finish() {
		this.endDate = new Date();
	}

	public final void begin() {
		this.startDate = new Date();
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

	public final Date getStartDate() {
		return this.startDate;
	}

	public final Date getEndDate() {
		return this.endDate;
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
		if (BUILDING_NAMES_KEY.equalsIgnoreCase(definition.getName())) {
			this.buildNames = totalInsertCount;
		}
		else if (LOCALITIES_KEY.equalsIgnoreCase(definition.getName())) {
			this.localities = totalInsertCount;
		}
		else if (MAILSORT_KEY.equalsIgnoreCase(definition.getName())) {
			this.mailSort = totalInsertCount;
		}
		else if (ORGANISATION_KEY.equalsIgnoreCase(definition.getName())) {
			this.organisations = totalInsertCount;
		}
		else if (PAF_ADDRESS__KEY.equalsIgnoreCase(definition.getName())) {
			this.pafAddress = totalInsertCount;
		}
		else if (SUB_BUILDING_NAME.equalsIgnoreCase(definition.getName())) {
			this.subBuildingName = totalInsertCount;
		}
		else if (THOROUGHFARE_DESCRIPTOR_KEY.equalsIgnoreCase(definition.getName())) {
			this.thoroughfareDescriptor = totalInsertCount;
		}
		else if (THOROUGHFARE_KEY.equalsIgnoreCase(definition.getName())) {
			this.thoroughfare = totalInsertCount;
		}
		else if (UDPRN_KEY.equalsIgnoreCase(definition.getName())) {
			this.udprn = totalInsertCount;
		}
		else {
			throw new IllegalArgumentException("Unknown table: " + definition.getName());
		}
	}
}
