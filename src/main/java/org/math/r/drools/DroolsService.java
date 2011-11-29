/**
 * The drools service
 */
package org.math.r.drools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.NullArgumentException;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatelessSession;
import org.drools.compiler.DroolsError;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.CSVPrinter;

/**
 * @author Sms Chauhan A lot in this class has been taken from the Drools node present in the
 *         nexusBPM project. The class mostly relies on the contribution of
 *         the project members.
 */
public class DroolsService {

  private String[] inputColumns = null;

  private List<String> expectedInputColumns = null;

  private List<String> outputColumns = null;

  private StatelessSession session = null;

  public DroolsService(String rules, String expectedInputColumnsCSV, String outputColumnsCSV)
          throws Exception {
    this.expectedInputColumns = new ArrayList<String>();
    this.outputColumns = new ArrayList<String>();
    // validate all inputs
    validateInputs(rules, expectedInputColumnsCSV, expectedInputColumns, outputColumnsCSV,
            outputColumns);
    this.session = createSession(rules);
  }

  public String execute(String inputCSV) throws Exception {
    String outputCSV = null;
    ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
    // verify input CSV
    validateInputCSV(inputCSV);
    // read the inputs columns from the input CSV string
    InputStream inputCSVStream = new ByteArrayInputStream(inputCSV.getBytes());
    CSVParser parser = new CSVParser(inputCSVStream);
    this.inputColumns = parser.getLine();
    // verify input columns
    verifyInputColumns(this.expectedInputColumns, this.inputColumns);
    // open an output stream for the output CSV string and print the column names
    CSVPrinter printer = new CSVPrinter(csvOutputStream);
    printer.writeln(this.outputColumns.toArray(new String[this.outputColumns.size()]));
    // get a drools session from the rules file

    // run the rules and write the results to the output CSV
    runRules(this.inputColumns, this.outputColumns, parser, printer, this.session);
    // convert the ByteArrayOutputStream to a string
    outputCSV = new String(csvOutputStream.toByteArray(), "UTF-8");
    return outputCSV;

  }

  protected void validateInputCSV(String inputCSV) throws Exception {
    if (inputCSV == null || inputCSV == "") {
      throw new NullArgumentException("The input dataset is emtpy!");
    }
  }

  protected void validateInputs(String rules, String expectedInputColumnsCSV,
          List<String> expectedInputColumns, String outputColumnsCSV, List<String> outputColumns)
          throws Exception

  {
    if (rules == null || rules == "") {
      throw new NullArgumentException("Empty rules file!");
    }
    StringTokenizer tokenizer = null;
    if (expectedInputColumnsCSV != null && expectedInputColumnsCSV != "") {

      tokenizer = new StringTokenizer(expectedInputColumnsCSV.replace(" ", ""), ",");

      while (tokenizer.hasMoreTokens()) {
        expectedInputColumns.add(tokenizer.nextToken());
      }
    } else {
      throw new NullArgumentException("No input columns found!");
    }
    if (expectedInputColumnsCSV != null && expectedInputColumnsCSV != "") {
      tokenizer = new StringTokenizer(outputColumnsCSV.replace(" ", ""), ",");

      while (tokenizer.hasMoreTokens()) {
        outputColumns.add(tokenizer.nextToken());
      }
    } else {
      throw new NullArgumentException("No output columns found!");
    }
  }

  protected void verifyInputColumns(List<String> expectedInputColumns, String[] inputColumns)
          throws Exception {
    for (String column : expectedInputColumns) {
      boolean found = false;
      for (int index = 0; index < inputColumns.length; index++) {
        if (inputColumns[index].equals(column)) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new IllegalArgumentException(
                "Error: The input CSV file does not contain the column '" + column
                        + "' which is required by the rules file!");
      }
    }
  }

  protected StatelessSession createSession(String rules) throws Exception {
    InputStream rulesStream = new ByteArrayInputStream(rules.getBytes());
    try {
      PackageBuilder builder = new PackageBuilder();
      builder.addPackageFromDrl(new InputStreamReader(rulesStream));
      PackageBuilderErrors errors = builder.getErrors();
      if (errors != null && errors.getErrors() != null && errors.getErrors().length > 0) {
        StringBuilder b = new StringBuilder();
        b.append("Error parsing rules file:\r\n");
        for (DroolsError error : errors.getErrors()) {
          b.append(error).append("\r\n");
        }
        throw new IllegalArgumentException(b.toString());
      }

      RuleBase ruleBase = RuleBaseFactory.newRuleBase();
      ruleBase.addPackage(builder.getPackage());

      return ruleBase.newStatelessSession();
    } finally {
      if (rulesStream != null) {
        try {
          rulesStream.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  protected void runRules(String[] inputColumns, List<String> outputColumns, CSVParser parser,
          CSVPrinter printer, StatelessSession session) throws Exception {
    String[] outputRow = new String[outputColumns.size()];
    String[] inputRow;

    Map<String, String> inputMap = new HashMap<String, String>();
    Map<String, String> outputMap = new HashMap<String, String>();

    session.setGlobal("output", outputMap);

    while ((inputRow = parser.getLine()) != null) {
      // write the values for the input row
      for (int index = 0; index < inputColumns.length; index++) {
        inputMap.put(inputColumns[index], inputRow[index]);
      }
      if (outputMap.size() > 0) {
        outputMap.clear();
      }
      session.execute(inputMap);

      if (outputMap.size() > 0) {
        for (int index = 0; index < outputColumns.size(); index++) {
          outputRow[index] = outputMap.get(outputColumns.get(index));
        }
        printer.writeln(outputRow);
      }
    }
  }
}
