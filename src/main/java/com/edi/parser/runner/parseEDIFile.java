package com.edi.parser.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.berryworks.edireader.json.fromedi.EdiToJson;
import com.edi.parser.model.DistributionCenter;
import com.edi.parser.model.LineItem;
import com.edi.parser.model.PO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class parseEDIFile implements Runnable {
	private File ediFile, jsonFile;
	private boolean summarize, annotate, format, recover;
	private String itemsku = "";

	// Set defaults for options
	public parseEDIFile() {
		summarize = false;
		annotate = false;
		format = true;
		recover = false;
	}

	@Override
	public void run() {
		final EdiToJson ediToJson = new EdiToJson();
		Reader jsonReader = null;
		ediToJson.setFormatting(format);
		ediToJson.setAnnotated(annotate);
		ediToJson.setSummarize(summarize);
		// ediToJson.setRecover(recover);

		try (Reader reader = new BufferedReader(
				ediFile == null ? new InputStreamReader(System.in) : new FileReader(ediFile));
				Writer writer = new BufferedWriter(
						jsonFile == null ? new OutputStreamWriter(System.out) : new FileWriter(jsonFile))) {
			ediToJson.asJson(reader, writer);

			// Read and parse the JSON file
			jsonReader = Files.newBufferedReader(Paths.get("output.json"));
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = parser.parse(jsonReader).getAsJsonObject();
			JsonArray interchangesArray = jsonObject.getAsJsonArray("interchanges");
			StringBuilder poBuilder = new StringBuilder();
			interchangesArray.getAsJsonArray().forEach(interchanges -> {
				JsonArray functionalGroupArray = interchanges.getAsJsonObject().get("functional_groups")
						.getAsJsonArray();
				functionalGroupArray.getAsJsonArray().forEach(functionalGroup -> {
					JsonArray transactionsArray = functionalGroup.getAsJsonObject().get("transactions")
							.getAsJsonArray();
					transactionsArray.getAsJsonArray().forEach(transaction -> {
						PO po = new PO();
						capturePOData(transaction, po);
						poBuilder.append(po);
						poBuilder.append(System.getProperty("line.separator"));
					});

				});
			});
			Path path = Paths.get("PODetails.txt");
			byte[] strToBytes = poBuilder.toString().getBytes();
			Files.write(path, strToBytes);
			// close reader
			jsonReader.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void capturePOData(JsonElement transaction, PO po) {
		List<LineItem> allLineItems = new ArrayList<>();
		JsonArray segmentsArray = transaction.getAsJsonObject().get("segments").getAsJsonArray();
		segmentsArray.getAsJsonArray().forEach(segment -> {
			Set<String> allKeys = segment.getAsJsonObject().keySet();
			if (allKeys.contains("BEG_03")) {
				po.setPoNumber(segment.getAsJsonObject().get("BEG_03").getAsString());
			}
			if (allKeys.contains("N1-4000_loop")) {
				DistributionCenter rdc = new DistributionCenter();
				JsonArray dcSegmentArray = segment.getAsJsonObject().get("N1-4000_loop").getAsJsonArray();
				dcSegmentArray.forEach(dcSegment -> {
					Set<String> dcSegmentKeys = dcSegment.getAsJsonObject().keySet();
					if (dcSegmentKeys.contains("N1_04")) {
						rdc.setDcNumber(dcSegment.getAsJsonObject().get("N1_04").getAsString());
					}
					if (dcSegmentKeys.contains("N3_01")) {
						rdc.setAddress(dcSegment.getAsJsonObject().get("N3_01").getAsString());
					}

					if (dcSegmentKeys.contains("N4_01")) {
						rdc.setCity(dcSegment.getAsJsonObject().get("N4_01").getAsString());
					}

					if (dcSegmentKeys.contains("N4_02")) {
						rdc.setState(dcSegment.getAsJsonObject().get("N4_02").getAsString());
					}

					if (dcSegmentKeys.contains("N4_03")) {
						rdc.setZipCode(dcSegment.getAsJsonObject().get("N4_03").getAsString());
					}
				});
				po.setDistributionCenter(rdc);
			}

			if (allKeys.contains("PO1-8000_loop")) {
				LineItem lineItem = new LineItem();
				JsonArray lineItemSegmentArray = segment.getAsJsonObject().get("PO1-8000_loop").getAsJsonArray();
				lineItemSegmentArray.forEach(lineSegment -> {
					Set<String> lineSegmentKeys = lineSegment.getAsJsonObject().keySet();
					if (lineSegmentKeys.contains("PO1_01")) {
						lineItem.setLineItemId(lineSegment.getAsJsonObject().get("PO1_01").getAsString());
					}
					if (lineSegmentKeys.contains("PO1_02")) {
						lineItem.setQty(lineSegment.getAsJsonObject().get("PO1_02").getAsString());
					}

					if (lineSegmentKeys.contains("PO1_04")) {
						lineItem.setPurchasePrice(lineSegment.getAsJsonObject().get("PO1_04").getAsString());
					}

					if (lineSegmentKeys.contains("PO1_07")) {
						lineItem.setBpn(lineSegment.getAsJsonObject().get("PO1_07").getAsString());
					}

					if (lineSegmentKeys.contains("PO1_09")) {
						itemsku = lineSegment.getAsJsonObject().get("PO1_09").getAsString();
						if (!(itemsku.endsWith("-"))) {
							itemsku += "-";
						}
					}
					if (lineSegmentKeys.contains("PO1_11")) {
						itemsku += lineSegment.getAsJsonObject().get("PO1_11").getAsString();
					}
					lineItem.setSku(itemsku);
				});
				allLineItems.add(lineItem);
			}
			po.setAllLineItems(allLineItems);
		});
	}

	private static void badArgs() {
		System.err.println("Invalid command line arguments");
		logUsage();
	}

	private static void logUsage() {
		log();
		log("Usage Summary");
		log("=============");
		log();
		log("Read EDI from an input file, write JSON to an output file");
		log("  java -jar <jarFileName>  <ediInputFile>  <jsonOutputFile>  <options>");
		log();
		log("Read EDI from an input file, write JSON to stdout");
		log("  java -jar <jarFileName>  <ediInputFile>  <options>");
		log();
		log("Read EDI from stdin, write JSON to stdout");
		log("  java -jar <jarFileName>  <options>");
		log();
		log("Display this usage summary");
		log("  java -jar <jarFileName>  help");
		log();
		log();
		log("options");
		log("  ", "--summarize={yes|no}", ":", "if yes, omit segment-level detail (default is no)");
		log("  ", "--annotate={yes|no}", ":", "if yes, include descriptive \"annotations\" (default is no)");
		log("  ", "--format={yes|no}", ":", "if yes, format JSON output (default is yes)");
		log("  ", "--recover={yes|no}", ":", "if yes, ignore any recoverable EDI errors (default is no)");
		log();
	}

	private static void log(Object... items) {
		StringBuilder sb = new StringBuilder();
		for (Object item : items) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(item.toString());
		}
	}

	public static void main(String[] args) {
		final parseEDIFile driver = new parseEDIFile();

		// Args beginning with "--" are treated as options.
		boolean establishedInputFile = false;
		for (String arg : args) {
			if (arg.startsWith("--")) {
				// --option=value
				final String[] split = arg.split("=");
				if (split.length != 2) {
					badArgs();
					return;
				}
				String optionName = split[0];
				String yesOrNo = split[1];
				switch (optionName) {
				case "--summarize":
					driver.setSummarize("yes".equalsIgnoreCase(yesOrNo));
					break;
				case "--annotate":
					driver.setAnnotate("yes".equalsIgnoreCase(yesOrNo));
					break;
				case "--format":
					driver.setFormat("yes".equalsIgnoreCase(yesOrNo));
					break;
				case "--recover":
					driver.setRecover("yes".equalsIgnoreCase(yesOrNo));
					break;
				default:
					badArgs();
					return;
				}
			} else if ("help".equals(arg) && args.length == 1) {
				logUsage();
				return;
			} else {
				if (establishedInputFile) {
					driver.setOutputFile(new File(arg));
				} else {
					driver.setInputFile(new File(arg));
					establishedInputFile = true;
				}
			}
		}
		try {
			driver.run();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void setSummarize(boolean summarize) {
		this.summarize = summarize;
	}

	private void setAnnotate(boolean annotate) {
		this.annotate = annotate;
	}

	private void setFormat(boolean format) {
		this.format = format;
	}

	public void setRecover(boolean recover) {
		this.recover = recover;
	}

	public void setInputFile(File ediFile) {
		this.ediFile = ediFile;
	}

	public void setOutputFile(File jsonFile) {
		this.jsonFile = jsonFile;
	}
}
