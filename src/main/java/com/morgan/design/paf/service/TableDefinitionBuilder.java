package com.morgan.design.paf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

	public static final List<TableDefinition> parseDefinitionFiles(final File[] definitionFiles) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		final List<TableDefinition> definitions = Lists.newArrayList();
		for (final File fileDef : definitionFiles) {
			try {
				final DocumentBuilder db = dbf.newDocumentBuilder();
				final Document parse = db.parse(fileDef);
				final Element element = parse.getDocumentElement();

				final TableDefinition tableDefinition = new TableDefinition();
				definitions.add(tableDefinition);
				tableDefinition.setName(element.getAttribute("name"));
				tableDefinition.setFileName(element.getAttribute("fileName"));

				final List<ColumnDefinition> columnDefinitions = Lists.newArrayList();
				tableDefinition.setColumns(columnDefinitions);
				final NodeList nodeList = element.getElementsByTagName("column");
				for (int i = 0; i < nodeList.getLength(); i++) {

					final ColumnDefinition definition = new ColumnDefinition();
					columnDefinitions.add(definition);

					final NodeList nodes = nodeList.item(i).getChildNodes();
					for (int x = 0; x < nodes.getLength(); x++) {
						setColumnDefinitionFields(definition, nodes.item(x));
					}
				}

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
		}
		return definitions;
	}

	private static void setColumnDefinitionFields(final ColumnDefinition definition, final Node currentNode) {
		if (isNodeEqual(currentNode, "name")) {
			definition.setName(currentNode.getTextContent());
		}
		if (isNodeEqual(currentNode, "length")) {
			definition.setLength(Integer.valueOf(currentNode.getTextContent()));
		}
		if (isNodeEqual(currentNode, "type")) {
			definition.setType(currentNode.getTextContent());
		}
	}

	private static boolean isNodeEqual(final Node node, final String toMatch) {
		return node.getNodeName().equals(toMatch);
	}
}
