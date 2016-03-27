package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import algorithms.AlgEnum;
import data.Parameters;


public class CmdLineParser {
	private CommandLineParser parser;
	private CommandLine cmd;
	private Options options;
	private HelpFormatter helpText;

	public CmdLineParser() {
		parser = new BasicParser();
		options = new Options();
		helpText = new HelpFormatter();
		createOptions();
	}
	
	@SuppressWarnings("static-access")
	private void createOptions()
	{
		Option input = OptionBuilder.withArgName("file")
				.hasArgs(1)
				.isRequired(false)
				.withDescription("path to file with input data")
				.withLongOpt("input")
				.create('i');
		
		Option output = OptionBuilder.withArgName("path")
				.hasArgs(1)
				.isRequired(false)
				.withDescription("path where to store *.PNG files showing each dendrogram level clusterisation,"
						+ " leave empty if images are not needed. ")
				.withLongOpt("output")
				.create('o');
		
		Option k = OptionBuilder.withArgName("k")
				.hasArgs(1)
				.isRequired(false)
				.withDescription("number of clusters generated by clusterisation algorithm")
				.withLongOpt("clusters")
				.create('k');
		
		Option iterations = OptionBuilder.withArgName("iterations")
				.hasArgs(1)
				.withArgName("iterationNumber")
				.isRequired(false)
				.withDescription("number of maximum iterations made by clusterisation algorithm")
				.withLongOpt("number-of-iterations")
				.create('n');
				
		Option repeats = OptionBuilder.withArgName("repeats")
				.hasArgs(1)
				.withArgName("repeatsNumber")
				.isRequired(false)
				.withDescription("number of algorith repeats (new initialisation of clusters)")
				.withLongOpt("number-of-repeats")
				.create('r'); 
		
		Option dendrogramLevels = OptionBuilder.withArgName("dendrogram size")
				.hasArgs(1)
				.withArgName("dendrogramSize")
				.isRequired(false)
				.withDescription("max dendrogram height")
				.withLongOpt("dendrogram-size")
				.create('s');	
		
		Option dimensionsNamesFile = OptionBuilder.withArgName("path to file with dimensions names <dimensionNumber>" + Constans.delimiter + "<name>")
				.hasArgs(1)
				.withArgName("dimensionsNamesFilePath")
				.isRequired(false)
				.withDescription("path to file consisting name of each dimension, it is used when samples are coded as binary vectors."
						+ " File should consist in each line a pattern <dimensionNumber>" + Constans.delimiter + "<name>")
				.withLongOpt("dimNames")
				.create('d');
		
		Option epsilon = OptionBuilder.withDescription("Epsilon value expressed as 10^-epsilon, used in comparing values to 0.0, reducing round-off error. Default value is 10.")
				.hasArgs(1)
				.isRequired(false)
				.withArgName("epsilonValue")
				.withLongOpt("eps")
				.create('e');
		
		Option littleValue = OptionBuilder.withDescription("Value of diagonal matrix elements expressed as 10^-littleValue, used in forcing covariance matrix to be non-singular. Default value is 5.")
				.hasArgs(1)
				.isRequired(false)
				.withArgName("littleValue")
				.withLongOpt("littleVal")
				.create('l');
		
		Option numberOfNodes = OptionBuilder.withDescription("Maximum number of created nodes.")
				.hasArgs(1)
				.isRequired(false)
				.withArgName("maxNumberOfNodes")
				.withLongOpt("maxNumOfNodes")
				.create('w');
		
		Option responsibilityScallingFactor = OptionBuilder.withDescription("Scalling factor of static center responsibility computation values. In order to decrease it provide value in range (0;1).")
				.hasArgs(1)
				.isRequired(false)
				.withArgName("responsibilityScallingFactor")
				.withLongOpt("respSclFactor")
				.create('f');
		
		Option help = OptionBuilder.withDescription("prints this message")
				.hasArg(false)
				.isRequired(false)
				.withLongOpt("help")
				.create('h');
		
		options.addOption("km", "use-kmeans", false, "use kmeans clustering algorithm");
		options.addOption("gmm", "use-gaussian-mixture-model", false, "use gaussian-mixture-model clustering with expectation-maximisation");
		options.addOption("v", "verbose", false, "verbose program execution");
		options.addOption("c", "class-attribute", false, "indicates that FIRST column of data is class attribute, class shoud be indicated by string. When class attribute is provided "
				+ "then recall, precision and F-Measure will be calculated for each dendrogram level and for whole dednrogram. See \"Fast and effective text mining using linear-time "
				+ "document clustering\" by B. Larsen and C. Aone (1999)");
		options.addOption("nn", "not-normalise-data", false, "do not normalise data, and becaufe of that program can return visualisation of clustering process");
		options.addOption("ds", "disable-static-center", false, "disable feature of placing static (backgroud) center while going down in hierarchy");
		
		options.addOption(input);
		options.addOption(output);
		options.addOption(k);
		options.addOption(iterations);
		options.addOption(repeats);
		options.addOption(dendrogramLevels);
		options.addOption(dimensionsNamesFile);
		options.addOption(epsilon);
		options.addOption(littleValue);
		options.addOption(numberOfNodes);
		options.addOption(responsibilityScallingFactor);
		options.addOption(help);
	}
	
	public void parse(String[] args)
	{
		try {
			cmd = parser.parse(options, args);
		}
		catch( ParseException exp ) {
		    System.err.println(exp.getMessage());
		    System.exit(1);
		}
		if(cmd.hasOption('h') || cmd.hasOption("help") || args.length == 0)
		{
			viewHelp();
			System.exit(0);
		}
		else
		{
			parseParameters();
		}
	}

	private void parseParameters() {
		Parameters.setInputDataFilePath(parseInputFile());
		Parameters.setOutputFolder(parseOutputFile());
		Parameters.setDimensionsNamesFilePath(parseDimensionsNamesFile());
		
		int numberOfClusterisationAlgIterations = parsePositiveIntegerParameter(cmd.getOptionValue('n'),
													"Number of clusterisation algorithm iterations"
													+ " should be an integer positive value!");
		Parameters.setNumberOfClusterisationAlgIterations(numberOfClusterisationAlgIterations);
		
		int numberOfRepeats = parsePositiveIntegerParameter(cmd.getOptionValue('r'),
													"Number of clusterisation algorithm repeats"
													+ " should be an integer positive value!");
		
		Parameters.setNumberOfClusterisationAlgRepeats(numberOfRepeats);
		
		int k = parsePositiveIntegerParameter(cmd.getOptionValue("k"), "Number of clusters should"
															+ " be an integer positive value!");
		Parameters.setK(k);
		
		int dendrogramMaxHeight = parsePositiveIntegerParameter(cmd.getOptionValue('s'), "Maximum "
									+ "dedrogram height should be an positive integer value!");
		Parameters.setDendrogramMaxHeight(dendrogramMaxHeight);
		
		Parameters.setVerbose(cmd.hasOption('v'));
		
		Parameters.setMethod(parseMethod());
		
		Parameters.setClassAttribute(cmd.hasOption('c'));
		
		Parameters.setNormaliseData(!cmd.hasOption("nn"));
		
		Parameters.setDisableStaticCenter(cmd.hasOption("ds"));
		
		Parameters.setEpsilon(parseEpsilon());
		
		Parameters.setLittleValue(parseLittleValue());
		
		Parameters.setMaxNumberOfNodes(parseMaxNumberOfNodes());
		
		Parameters.setResponsibilityScallingFactor(parseResponsibilityScallingFactor());
	}

	private AlgEnum parseMethod() {
		if(cmd.hasOption("km"))
		{
			return AlgEnum.KMEANS;
		}
		else if(cmd.hasOption("gmm"))
		{
			return AlgEnum.GMM;
		}
		System.err.println("No clusterisation method was set!");
		System.exit(1);
		return null;
	}


	private double parseEpsilon() {
		int epsilon;
		if(cmd.hasOption('e'))
		{
			epsilon = parseIntegerParameter(cmd.getOptionValue('e'), "Epsilon "
					+ "value should be an integer value used as 10^-epsilon");
		}
		else
		{
			epsilon = 10; 
		}
		return Math.pow(10, (-1.0d)*(double)epsilon);//uzywane do porownywania doubli z 0.0 //10^-5 
	}
	
	private double parseResponsibilityScallingFactor() {
		double covarianceScallingFactor = 0;
		if(cmd.hasOption("f"))
		{
			covarianceScallingFactor = parseDoubleParameter(cmd.getOptionValue("f"), "Static center responsibility "
					+ "scalling factor value should be an real value! There will be no scalling (f:=1.0.");
		}
		else
		{
			covarianceScallingFactor = 1.0;
		}
		return covarianceScallingFactor;
	}
	
	private int parseMaxNumberOfNodes() {
		int maxNumberOfNodes = Integer.MAX_VALUE;
		if(cmd.hasOption("w"))
		{
			maxNumberOfNodes = parsePositiveIntegerParameter(cmd.getOptionValue("w"), "Number of nodes "
					+ "value should be an integer positive value!");
		}
		return maxNumberOfNodes;
	}

	private double parseLittleValue() {
		int littleValue;
		if(cmd.hasOption('l'))
		{
			littleValue = parseIntegerParameter(cmd.getOptionValue('l'), "Little value "
					+ "value should be an integer value used as 10^-littleValue");
		}
		else
		{
			littleValue = 5; 
		}
		return Math.pow(10, (-1.0d)*(double)littleValue);//uzywana do diagonala macierzy, aby macierz stala sie odwracalna// -1
	}
	
	private int parsePositiveIntegerParameter(String parsedOptionValue, String invalidArgMsg) {
		int parsedValue = -1;
		try
		{
			parsedValue = Integer.valueOf(parsedOptionValue);
			if(parsedValue <= 0)
			{
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException e)
		{
			System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
						+ " " + e.getMessage());
			System.exit(-1);
		}
		return parsedValue;
	}
	
	private int parseIntegerParameter(String parsedOptionValue, String invalidArgMsg) {
		int parsedValue = -1;
		try
		{
			parsedValue = Integer.valueOf(parsedOptionValue);
		}
		catch(NumberFormatException e)
		{
			System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
						+ " " + e.getMessage());
			System.exit(-1);
		}
		return parsedValue;
	}
	
	private double parseDoubleParameter(String parsedOptionValue, String invalidArgMsg) {
		double parsedValue = -1;
		try
		{
			parsedValue = Double.valueOf(parsedOptionValue);
		}
		catch(NumberFormatException e)
		{
			System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
						+ " " + e.getMessage());
			System.exit(-1);
		}
		return parsedValue;
	}

	private void viewHelp()
	{		 
		helpText.printHelp( "HClust", options );
	}	
	
	private Path parseInputFile()
	{
		File inputFile = null;
		if(cmd.hasOption('i'))
		{
			inputFile = new File(cmd.getOptionValue('i'));
			if(!inputFile.exists() || inputFile.isDirectory())
			{
				System.err.println("Input file shoud be existing file!");
				System.exit(1);
			}
		}
		else
		{
			System.err.println("No input file specified! Use -i option.");
			System.exit(1);
		}
		return inputFile.toPath();
	}
	
	private Path parseOutputFile() 
	{
		File outputFolder = null;
		if(cmd.hasOption('o'))
		{
			String outputFolderName = cmd.getOptionValue('o');
			outputFolder = new File(outputFolderName);
			if(outputFolder.isFile())
			{
				System.err.println(outputFolderName + " should be an path to directory!");
				System.exit(1);
			}
			if(!outputFolder.exists())
			{
				System.out.println(outputFolderName + " doesn't exist, creating folder.");
				try {
					Files.createDirectories(outputFolder.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.err.println("No output file specified! Use -o option.");
			System.exit(1);
		}
		return outputFolder.toPath();
	}
	
	private Path parseDimensionsNamesFile() {
		File mappingFile = null;
		if(cmd.hasOption('d'))
		{
			mappingFile = new File(cmd.getOptionValue('d'));
			if(!mappingFile.exists() || mappingFile.isDirectory())
			{
				System.err.println("Mapping file shoud be existing file with text!");
				System.exit(1);
			}
		}
		return mappingFile == null? null : mappingFile.toPath();
	}
}

