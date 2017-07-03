package br.unirio.bsi.pm.optimalpathfinder.xml;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import br.unirio.bsi.pm.optimalpathfinder.xml.XmlUtils;
import br.unirio.bsi.pm.optimalpathfinder.model.Track;

public class TrackParser {
	/**
	 * Importa uma pista em formato TRK de um arquivo
	 */
	public Track loadFromFile(String countryCode) throws Exception {
		String filename = "data/tracks/";
		filename = filename.concat(countryCode);
		filename = filename.concat("/track.xml");

		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\Z");
		String content = scanner.next();
		scanner.close();
		return load(content);
	}

	/**
	 * Importa uma pista em formato TRK de uma string
	 */
	public Track load(String content) throws Exception {
		Track track = new Track();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(content)));

		Element rootElement = doc.getDocumentElement();

		if (rootElement != null)
			loadTrack(rootElement, track);

		return track;
	}

	private void loadTrack(Element rootElement, Track track) {

		track.setPNGPath(XmlUtils.getStringNode(rootElement, "file", ""));

		int rows = XmlUtils.getIntNode(rootElement, "rows");
		track.setRows(rows);

		int columns = XmlUtils.getIntNode(rootElement, "columns");
		track.setColumns(columns);

		int borderWidth = XmlUtils.getIntNode(rootElement, "borderWidth");
		track.setBorderWidth(borderWidth);

		int borderHeight = XmlUtils.getIntNode(rootElement, "borderHeight");
		track.setBorderHeight(borderHeight);

		int firstColumnWidth = XmlUtils.getIntNode(rootElement, "firstColumnWidth");
		track.setFirstColumnWidth(firstColumnWidth);

		int firstRowHeight = XmlUtils.getIntNode(rootElement, "firstRowHeight");
		track.setfirstRowHeight(firstRowHeight);

		track.newTrackPointMap(rows, columns);

		loadMap(rootElement, track);

		loadCheckPoints(rootElement, track);

	}

	/**
	 * Load the map into the Track class
	 */

	private void loadMap(Element rootElement, Track track) {
		for (Element lineElement : XmlUtils.getElements(rootElement, "line")) {
			int row = XmlUtils.getIntAttribute(lineElement, "y");

			ArrayList<Integer> ColumnsIndexes = new ArrayList<Integer>();
			ColumnsIndexes = XmlUtils.getIntegerInsideTags(lineElement);

			for (int i = 0; i < ColumnsIndexes.size(); i++) {
				track.setTrackPoint(row, ColumnsIndexes.get(i), false, true);
			}

		}
	}

	/**
	 * Set the checkpoints on the map
	 */

	private void loadCheckPoints(Element rootElement, Track track) {
		int row, column, checkpointIndex = 0;

		Element passesElement = XmlUtils.getSingleElement(rootElement, "passes");

		for (Element pointElement : XmlUtils.getElements(passesElement, "point")) {
			column = XmlUtils.getIntAttribute(pointElement, "x");
			row = XmlUtils.getIntAttribute(pointElement, "y");

			if (track.isStartPointSet() == false) {
				track.setStartPoint(row, column);
				track.setStartPoint();
			}

			track.setTrackPoint(row, column, true, true, ++checkpointIndex);
			track.createCheckPoint(row, column);

		}
	}

}
