package com.morgan.design.paf.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.TableDefinition;

/**
 * @author James Edward Morgan
 */
public class TableDefinitionBuilder {

	private static final Logger logger = LoggerFactory.getLogger(TableDefinitionBuilder.class);

	private static final String TYPE = "type";
	private static final String LENGTH = "length";
	private static final String NAME = "name";
	private static final String NULLABLE = "nullable";
	private static final String COLUMN = "column";
	private static final String IGNORE_DUPLICATES = "ignoreDuplicates";
	private static final String FILE_NAME = "fileName";

	public static final List<TableDefinition> parseDefinitionFiles(final String definitionDirectory) {
		final List<TableDefinition> definitions = Lists.newArrayList();
		for (final File fileDef : FileLoaderUtils.loadDefinitionFiles(definitionDirectory)) {
			final TableDefinition tableDef = loadTableDefinition(fileDef);
			if (null != tableDef) {
				definitions.add(tableDef);
			}
		}
		return definitions;
	}

	public static final TableDefinition loadTableDefinition(final File fileDef) {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document parse = db.parse(fileDef);
			final Element element = parse.getDocumentElement();

			final TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setName(element.getAttribute(NAME));
			tableDefinition.setFileName(element.getAttribute(FILE_NAME));

			if (element.hasAttribute(IGNORE_DUPLICATES)) {
				tableDefinition.setIgnoreDuplicates(BooleanUtils.toBoolean(element.getAttribute(IGNORE_DUPLICATES)));
			}

			final List<ColumnDefinition> columnDefinitions = Lists.newArrayList();
			tableDefinition.setColumns(columnDefinitions);
			final NodeList nodeList = element.getElementsByTagName(COLUMN);

			for (int i = 0; i < nodeList.getLength(); i++) {

				final ColumnDefinition definition = new ColumnDefinition();
				columnDefinitions.add(definition);

				final Node item = nodeList.item(i);

				setNullableColumn(definition, item);

				final NodeList nodes = item.getChildNodes();
				for (int x = 0; x < nodes.getLength(); x++) {
					setColumnDefinitionFields(definition, nodes.item(x));
				}
			}
			return tableDefinition;
		}
		catch (final ParserConfigurationException pce) {
			logger.error("ParserConfigurationException: ", pce);
		}
		catch (final SAXException se) {
			logger.error("SAXException: ", se);
		}
		catch (final IOException ioe) {
			logger.error("IOException: ", ioe);
		}
		return null;
	}

	private static void setNullableColumn(final ColumnDefinition definition, final Node item) {
		final Node nullableCollumn = item.getAttributes()
			.getNamedItem(NULLABLE);
		if (null != nullableCollumn) {
			definition.setNullable(BooleanUtils.toBoolean(nullableCollumn.getNodeValue()));
		}
	}

	private static void setColumnDefinitionFields(final ColumnDefinition definition, final Node currentNode) {
		if (isNodeEqual(currentNode, NAME)) {
			definition.setName(currentNode.getTextContent());
		}
		if (isNodeEqual(currentNode, LENGTH)) {
			definition.setLength(Integer.valueOf(currentNode.getTextContent()));
		}
		if (isNodeEqual(currentNode, TYPE)) {
			definition.setType(currentNode.getTextContent());
		}
	}

	private static boolean isNodeEqual(final Node node, final String toMatch) {
		return node.getNodeName()
			.equals(toMatch);
	}
}
