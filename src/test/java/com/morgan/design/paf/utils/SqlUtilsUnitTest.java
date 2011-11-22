package com.morgan.design.paf.utils;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.util.FileLoaderUtils;
import com.morgan.design.paf.util.SqlUtils;
import com.morgan.design.paf.util.TableDefinitionBuilder;

public class SqlUtilsUnitTest {

	@Test
	public void shouldCorretlyCreateBatchUpdateStatement() {

		final File[] retrieveFiles = FileLoaderUtils.loadDefinitionFiles("src\\main\\resources\\definitions\\");
		assertThat(retrieveFiles.length, Is.is(10));

		final List<TableDefinition> definitionFiles = TableDefinitionBuilder.parseDefinitionFiles("src\\main\\resources\\definitions\\");
		assertThat(SqlUtils.createBatchUpdateStatement(definitionFiles.get(0)),
				Is.is("INSERT INTO `building_names` (`BuildingNameKey`,`BuildingName`) VALUES (?,?)"));
		assertThat(SqlUtils.createBatchUpdateStatement(definitionFiles.get(1)),
				Is.is("INSERT INTO `localities` (`LocalityKey`,`PostTown`,`DependentLocality`,`DoubleDependentLocality`) VALUES (?,?,?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(2)),
				Is.is("INSERT INTO `mailsort` (`PostcodeOutwardCode`,`PostcodeSector`,`ResidueIdentifier`,`DirectWithinResidueIndicator`) VALUES (?,?,?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(3)),
				Is.is("INSERT INTO `organisations` (`OrganisationKey`,`PostcodeType`,`OrganisationName`,`DepartmentName`) VALUES (?,?,?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(4)),
				Is.is("INSERT INTO `paf_address` (`PostcodeOutwardCode`,`PostcodeInwardCode`,`AddressKey`,`LocalityKey`,`ThoroughfareKey`,`ThoroughfareDescriptorKey`,`DependentThoroughfareKey`,`DependentThoroughfareDescriptorKey`,`BuildingNumber`,`BuildingNameKey`,`SubBuildingNameKey`,`NumberOfHouseholds`,`OrganisationKey`,`PostcodeType`,`ConcatenationIndicator`,`DeliveryPointSuffix`,`SmallUserOrganisationIndicator`,`POBoxNumber`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"));
		assertThat(SqlUtils.createBatchUpdateStatement(definitionFiles.get(5)),
				Is.is("INSERT INTO `sub_building_name` (`SubBuildingNameKey`,`SubBuildingName`) VALUES (?,?)"));
		assertThat(SqlUtils.createBatchUpdateStatement(definitionFiles.get(6)),
				Is.is("INSERT INTO `thoroughfare` (`ThoroughfareKey`,`ThoroughfareName`) VALUES (?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(7)),
				Is.is("INSERT INTO `thoroughfare_descriptor` (`ThoroughfareDescriptorKey`,`ThoroughfareDescriptor`,`ApprovedAbbreviation`) VALUES (?,?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(8)),
				Is.is("INSERT INTO `udprn` (`Postcode`,`AddressKey`,`UdprnKey`,`OrganisationKey`,`PostcodeType`,`DeliveryPointSuffix`,`SuOrganisationIndicator`) VALUES (?,?,?,?,?,?,?)"));
		assertThat(
				SqlUtils.createBatchUpdateStatement(definitionFiles.get(9)),
				Is.is("INSERT IGNORE INTO `welsh_address` (`PostcodeOutwardCode`,`PostcodeInwardCode`,`AddressKey`,`LocalityKey`,`ThoroughfareKey`,`ThoroughfareDescriptorKey`,`DependentThoroughfareKey`,`DependentThoroughfareDescriptorKey`,`BuildingNumber`,`BuildingNameKey`,`SubBuildingNameKey`,`NumberOfHouseholds`,`OrganisationKey`,`PostcodeType`,`ConcatenationIndicator`,`DeliveryPointSuffix`,`SmallUserOrganisationIndicator`,`POBoxNumber`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"));
	}
}
