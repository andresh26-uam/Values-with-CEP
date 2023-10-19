package de.hsh.inform.swa;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.espertech.esper.collection.Pair;

import de.hsh.inform.swa.bat4cep.Bat4CEP;
import de.hsh.inform.swa.bat4cep.BatTestCase;
import de.hsh.inform.swa.bat4cep.util.BatConfig;
import de.hsh.inform.swa.bat4cep.util.RunResult;
import de.hsh.inform.swa.cep.Action;
import de.hsh.inform.swa.cep.AttributeCondition;
import de.hsh.inform.swa.cep.ConstantAttribute;
import de.hsh.inform.swa.cep.Event;
import de.hsh.inform.swa.cep.EventAttribute;
import de.hsh.inform.swa.cep.Rule;
import de.hsh.inform.swa.cep.operators.attributes.aggregation.AvgAggregateAttribute;
import de.hsh.inform.swa.cep.operators.attributes.aggregation.MaxAggregateAttribute;
import de.hsh.inform.swa.cep.operators.attributes.aggregation.MinAggregateAttribute;
import de.hsh.inform.swa.cep.operators.attributes.aggregation.SumAggregateAttribute;
import de.hsh.inform.swa.cep.operators.attributes.arithmetic.AdditionOperator;
import de.hsh.inform.swa.cep.operators.attributes.arithmetic.SubtractionOperator;
import de.hsh.inform.swa.cep.operators.attributes.comparison.EqualToAttributeComparisonOperator;
import de.hsh.inform.swa.cep.operators.attributes.comparison.GreaterThanAttributeComparisonOperator;
import de.hsh.inform.swa.cep.operators.attributes.comparison.LessThanAttributeComparisonOperator;
import de.hsh.inform.swa.cep.operators.attributes.logic.AndAttributeOperator;
import de.hsh.inform.swa.cep.operators.attributes.logic.NotAttributeOperator;
import de.hsh.inform.swa.cep.operators.attributes.logic.OrAttributeOperator;
import de.hsh.inform.swa.cep.operators.events.AndEventOperator;
import de.hsh.inform.swa.cep.operators.events.NotEventOperator;
import de.hsh.inform.swa.cep.operators.events.SequenceEventOperator;
import de.hsh.inform.swa.cep.windows.TimeWindow;
import de.hsh.inform.swa.util.EventHandler;
import de.hsh.inform.swa.util.IndividualLogsCSV;
import de.hsh.inform.swa.util.LogCSV;
import de.hsh.inform.swa.util.SimpleLogger;
import de.hsh.inform.swa.util.data.AttributeConfig;
import de.hsh.inform.swa.util.data.DataCreator;
import de.hsh.inform.swa.util.data.DataCreatorConfig;
import de.hsh.inform.swa.util.data.DatasetEnum;
/**
 * Main class in which the test cases and the wanted rules are defined.
 * 
 * If you start the project without any changes, the first test case presented in "Bat4CEP: A Bat Algorithm for Mining of 
 * Complex Event Processing Rules" by R. Bruns and J. Dunkel will be executed.
 * All other test cases described in the paper are included as comments in main(). If you want to reproduce these test cases, 
 * it is sufficient to uncomment them.
 * 
 * @author Software Architecture Research Group
 *
 */
public class BatTestMain {
	
    public final static Map<String, Rule> rules = new TreeMap<>();
    private final static Map<String, BatTestCase> bat_tests = new TreeMap<>();

    private final static int TEST_RUNS = 10;
    private static final int NUMBER_OF_THREADS = 5;
    
    //bat default config
    private final static int DEFAULT_SWARM_SIZE = 200;	
    private final static int DEFAULT_TIMESTEPS = 500;
    private final static double DEFAULT_MIN_FREQUENCE = 0.0;
    private final static double DEFAULT_MAX_FREQUENCE = 2.5;
    private final static double DEFAULT_PULSE_RATE = 0.1;
    private final static double DEFAULT_ALPHA = 0.9;
    private final static double DEFAULT_LOUDNESS = 1.0;
    private final static double DEFAULT_GAMMA = 0.9;
    
    // data default config
    private final static int DEFAULT_EVENT_SIZE = 15000;
    private final static int DEFAULT_EVENT_TYPES = 10;
    private final static int DEFAULT_EVENT_ATTRIBUTES = 3;
    private final static int DEFAULT_MIN_INTERMEDIATE_SEC = 1;
    private final static int DEFAULT_MAX_INTERMEDIATE_SEC = 10;
    
    public static void main(String[] args) throws FileNotFoundException {
    	
        initRules();
        initBatTests();
        
        List<BatTestCase> tests = new ArrayList<>();
        
        /**
         * In the following, all test cases from the Bat4CEP paper can be found in the form of comments. 
         * If you want to run these test cases, you just have to comment them out.
         */
 
        	/** testing complex rules */
        tests.add(bat_tests.get("complex#1"));
//        tests.add(bat_tests.get("complex#2"));
//        tests.add(bat_tests.get("complex#3"));
//        tests.add(bat_tests.get("complex#4"));
//        tests.add(bat_tests.get("complex#5"));
//        tests.add(bat_tests.get("complex#6"));
//        tests.add(bat_tests.get("complex#7"));
//        tests.add(bat_tests.get("complex#8"));
//        tests.add(bat_tests.get("complex#9"));
//        tests.add(bat_tests.get("complex#10"));
//        tests.add(bat_tests.get("complex#11"));
//        tests.add(bat_tests.get("complex#12"));
//        tests.add(bat_tests.get("complex#13"));
        	/** testing arithmetic rules */
//        tests.add(bat_tests.get("arithmetic#5"));
//        tests.add(bat_tests.get("arithmetic#6"));
//        tests.add(bat_tests.get("arithmetic#1"));
//        tests.add(bat_tests.get("arithmetic#2"));
//        tests.add(bat_tests.get("arithmetic#3"));     
        	/** testing rules with an or operator */
//        tests.add(bat_tests.get("or#1"));
           	/** testing rules with a not operator in the ACT */
//        tests.add(bat_tests.get("not#1"));
//        tests.add(bat_tests.get("not#3"));
//        tests.add(bat_tests.get("not#2"));
//        tests.add(bat_tests.get("not#4"));
        	/** testing rules with aggregations*/
//        tests.add(bat_tests.get("aggregation#1"));
//        tests.add(bat_tests.get("aggregation#2"));
//        tests.add(bat_tests.get("aggregation#3"));
//        tests.add(bat_tests.get("aggregation#4"));
//        tests.add(bat_tests.get("aggregation#5"));
        	/** testing rules with different window sizes */
//        tests.add(bat_tests.get("window#1"));
//        tests.add(bat_tests.get("window#2"));
//        tests.add(bat_tests.get("window#3"));
//        tests.add(bat_tests.get("window#4"));
//	      tests.add(bat_tests.get("window#5"));
//	      tests.add(bat_tests.get("window#6"));
//	      tests.add(bat_tests.get("window#7"));
//	      tests.add(bat_tests.get("window#8"));
        	/** testing rules with different number of hits**/
//        tests.add(bat_tests.get("hit#6"));
//        tests.add(bat_tests.get("hit#5"));
//        tests.add(bat_tests.get("hit#4"));
//        tests.add(bat_tests.get("hit#3"));
//        tests.add(bat_tests.get("hit#2"));
//        tests.add(bat_tests.get("hit#1"));
//        tests.add(bat_tests.get("hit#7"));
//        tests.add(bat_tests.get("hit#8"));
//        tests.add(bat_tests.get("hit#9"));
//        tests.add(bat_tests.get("hit#10"));
//        tests.add(bat_tests.get("hit#11"));
//        tests.add(bat_tests.get("hit#12"));
        	/** testing rules with more events per second**/
//        	tests.add(bat_tests.get("events_per_second#1"));
        	/** testing rules with different number of event types */
//        tests.add(bat_tests.get("nr_event_types#1"));
//        tests.add(bat_tests.get("nr_event_types#2"));
//        tests.add(bat_tests.get("nr_event_types#3"));
//        tests.add(bat_tests.get("nr_event_types#4"));
//        tests.add(bat_tests.get("nr_event_types#5"));
//        tests.add(bat_tests.get("nr_event_types#6"));
//        tests.add(bat_tests.get("nr_event_types#7"));
//        tests.add(bat_tests.get("nr_event_types#8"));
//        tests.add(bat_tests.get("nr_event_types#9"));
//        tests.add(bat_tests.get("nr_event_types#10"));
//        tests.add(bat_tests.get("nr_event_types#10"));
//        tests.add(bat_tests.get("nr_event_types#10"));
//        tests.add(bat_tests.get("nr_event_types#10"));
        	/** testing rules different number of attribute types */
//        tests.add(bat_tests.get("nr_attr_types#1"));
//        tests.add(bat_tests.get("nr_attr_types#2"));
//        tests.add(bat_tests.get("nr_attr_types#3"));
//        tests.add(bat_tests.get("nr_attr_types#4"));
//        tests.add(bat_tests.get("nr_attr_types#5"));
//        tests.add(bat_tests.get("nr_attr_types#6"));
//        tests.add(bat_tests.get("nr_attr_types#7"));
//        tests.add(bat_tests.get("nr_attr_types#8"));
        	/** testing traffic rules */
//        tests.add(bat_tests.get("traffic#1"));
//        tests.add(bat_tests.get("traffic#2"));
//        tests.add(bat_tests.get("traffic#3"));
//        tests.add(bat_tests.get("traffic#4"));
//        tests.add(bat_tests.get("traffic#5"));
//        tests.add(bat_tests.get("traffic#6"));
        
        for(BatTestCase t: tests) {
        	System.out.println(t.getTrainingData().getRule());
        }
        System.out.println();
        executeTests(tests);
    }

    private static void executeTests(List<BatTestCase> tests) throws FileNotFoundException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String folderName = "logs" + File.separator + dateFormat.format(date);
        File d = new File(folderName);
        if (!d.exists()) {
            d.mkdirs();
        }

        IndividualLogsCSV individualCSV = new IndividualLogsCSV(folderName + File.separator + dateFormat.format(date) + "_individual.csv");
        LogCSV csv = new LogCSV(folderName + File.separator + dateFormat.format(date) + ".csv");

        for (BatTestCase t : tests) {
        	System.out.println(t.getTrainingData().getData());
        	Pair<List<Event>, List<Event>> allEvents = DataCreator.getEvents(t.getTrainingData());
        	
            List<Event> eventsTraining = allEvents.getFirst();
            List<Event> eventsHoldout = allEvents.getSecond();

            List<RunResult> results = executeTest(t, folderName, eventsTraining, eventsHoldout);
            individualCSV.addRuns(results);
            csv.addRuns(t, results, eventsTraining, eventsHoldout);
        	
        }
        csv.close();
        individualCSV.close();
    }

    public static List<RunResult> executeTest(BatTestCase test, String logFolder, List<Event> eventsTraining, List<Event> eventsHoldout) throws FileNotFoundException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        SimpleLogger log = SimpleLogger.getSimpleLogger(new File(logFolder + File.separator + dateFormat.format(date) + "_log.txt"));
        log.add(System.out);

        log.println("# Swarm config:");

        log.println("# Timesteps: " + test.getBatConfig().getTimesteps());
        log.println("# Name: " + test.getBatConfig().getSwarmName());
        log.println("# Size: " + test.getBatConfig().getSwarmSize());
        log.println("# Alpha: " + test.getBatConfig().getAlpha());
        log.println("# Gamma: " + test.getBatConfig().getGamma());
        log.println("# Loudness: " + test.getBatConfig().getLoudness());
        log.println("# Max frequency: " + test.getBatConfig().getMaxFrequency());
        log.println("# Pulsrate: " + test.getBatConfig().getPulserate());

        log.println("#");
        log.println("# Data creation config");

        log.println(getConfigString(test.getTrainingData()).toString());

        String fileTraining = logFolder + File.separator + dateFormat.format(date) + "_trainings_data.dat";
        String fileTest = logFolder + File.separator + dateFormat.format(date) + "_test_data.dat";

        
        saveEventsToFile(test.getTrainingData(), eventsTraining, fileTraining);

        saveEventsToFile(test.getTrainingData(), eventsHoldout, fileTest);

        log.println("# ");
        log.println("# Data files ");

        log.println("# Trainings data: " + fileTraining);
        log.println("# Test data: " + fileTest);
        log.println("# ");
        log.println("# Hits:");
        log.println("# Training hit count: " + new EventHandler(eventsTraining, test.getTrainingData().getComplexEvent()).getComplexEventCount());
        log.println("# Test  hit count: " + new EventHandler(eventsHoldout, test.getTrainingData().getComplexEvent()).getComplexEventCount());
        log.println("#");
        log.println("# Log:");

        log.println("Executing BatTest");

        Event complex_event = test.getTrainingData().getComplexEvent();

        BatConfig config = test.getBatConfig();
        log.flush();

        List<RunResult> result = Bat4CEP.execute(config, eventsTraining, eventsHoldout, complex_event, test.getNumRuns(), test.getMaxECTHeight(), test.getMaxACTHeight(), log, NUMBER_OF_THREADS);
        log.close();
        return result;
    }

    private static void saveEventsToFile(DataCreatorConfig dataConfig, List<Event> events, String outputFilename) {
        Path file = Paths.get(outputFilename);
        PrintWriter out = null;
        try {
            out = new PrintWriter(Files.newBufferedWriter(file), true);
            StringBuilder header = getConfigString(dataConfig).append(System.lineSeparator());

            out.println(header.toString());
            for (Event event : events) {
                StringBuilder sb = new StringBuilder();
                Set<String> attribute_names = event.getAttributeNames();
                if (attribute_names != null) {
                    for (String name : attribute_names) {
                        sb.append("; ").append(name).append(": ").append(event.getValue(name));
                    }
                }
                String s = event.toString() + sb.toString();
                out.println(s);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder getConfigString(DataCreatorConfig dataConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Number of events: " + dataConfig.getNumEvents()).append(System.lineSeparator());
        sb.append("# Number of events types: " + dataConfig.getNumEventTypes()).append(System.lineSeparator());
        sb.append("# Number of events attributes: " + dataConfig.getNumEventAttributes()).append(System.lineSeparator());
        sb.append("# Minimum intermediate seconds: " + dataConfig.getMinIntermediateSeconds()).append(System.lineSeparator());
        sb.append("# Maximum intermediate seconds: " + dataConfig.getMaxIntermediateSeconds()).append(System.lineSeparator());
        sb.append("# Complex Event: " + dataConfig.getComplexEvent()).append(System.lineSeparator());
        sb.append("# Event attributes: ");
        for (Entry<String, AttributeConfig<?>> entry : dataConfig.getFixedSensorRange().entrySet()) {
            sb.append(entry.getKey() + ": {" + entry.getValue().getMinValue() + " - " + entry.getValue().getMaxValue() + "}, ");
        }
        for (int i = dataConfig.getFixedSensorRange().size(); i < dataConfig.getNumEventAttributes(); i++) {
            sb.append("OTHER_" + i + " : {" + dataConfig.getDefaultRange().getMinValue() + " - " + dataConfig.getDefaultRange().getMaxValue()
                    + "}, ");
        }

        sb.append(System.lineSeparator());
        sb.append("# Used rule: " + dataConfig.getRule());
        return sb;
    }

    private static void initBatTests() {
    	
        Map<String, AttributeConfig<?>> defaultRange = new HashMap<>();

        Event defaultComplexEvent = new Event("HIT");

        defaultRange.put("ID", new AttributeConfig<Integer>(1, 10));
        defaultRange.put("ROOM", new AttributeConfig<Integer>(1, 10));
        defaultRange.put("TEMP", new AttributeConfig<Integer>(1, 100));

        DataCreatorConfig defaultConfig = new DataCreatorConfig();
        defaultConfig.setRule(rules.get("complex#1"));
        defaultConfig.setNumEvents(DEFAULT_EVENT_SIZE);
        defaultConfig.setNumEventTypes(DEFAULT_EVENT_TYPES);
        defaultConfig.setHitPercentage(1.0);
        defaultConfig.setMinIntermediateSeconds(DEFAULT_MIN_INTERMEDIATE_SEC);
        defaultConfig.setMaxIntermediateSeconds(DEFAULT_MAX_INTERMEDIATE_SEC);
        defaultConfig.setNumEventAttributes(DEFAULT_EVENT_ATTRIBUTES);
        defaultConfig.getFixedSensorRange().putAll(defaultRange);
        defaultConfig.setDefaultRange(new AttributeConfig<Integer>(-100, 100));
        defaultConfig.setComplexEvent(defaultComplexEvent);
        defaultConfig.setData(DatasetEnum.SYNTHETIC);

         BatConfig batConfig = new BatConfig("swarm", 
                 DEFAULT_SWARM_SIZE, 
                 DEFAULT_TIMESTEPS,
                 DEFAULT_MIN_FREQUENCE,
                 DEFAULT_MAX_FREQUENCE,
                 DEFAULT_PULSE_RATE, 
                 DEFAULT_GAMMA,
                 DEFAULT_LOUDNESS, 
                 DEFAULT_ALPHA 
         );
         
         /** TEST: COMPLEXITY **/
        DataCreatorConfig complexityConfig1 = defaultConfig.copy();
        complexityConfig1.setRule(rules.get("complex#1"));
        BatTestCase complexityTest1 = new BatTestCase(complexityConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig2 = defaultConfig.copy();
        complexityConfig2.setRule(rules.get("complex#2"));
        BatTestCase complexityTest2 = new BatTestCase(complexityConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig3 = defaultConfig.copy();
        complexityConfig3.setRule(rules.get("complex#3"));
        BatTestCase complexityTest3 = new BatTestCase(complexityConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig4 = defaultConfig.copy();
        complexityConfig4.setRule(rules.get("complex#4"));
        BatTestCase complexityTest4 = new BatTestCase(complexityConfig4, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig5 = defaultConfig.copy();
        complexityConfig5.setRule(rules.get("complex#5"));
        BatTestCase complexityTest5 = new BatTestCase(complexityConfig5, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig6 = defaultConfig.copy();
        complexityConfig6.setRule(rules.get("complex#6"));
        BatTestCase complexityTest6 = new BatTestCase(complexityConfig6, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig7 = defaultConfig.copy();
        complexityConfig7.setRule(rules.get("complex#7"));
        BatTestCase complexityTest7 = new BatTestCase(complexityConfig7, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig8 = defaultConfig.copy();
        complexityConfig8.setRule(rules.get("complex#8"));
        BatTestCase complexityTest8 = new BatTestCase(complexityConfig8, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig9 = defaultConfig.copy();
        complexityConfig9.setRule(rules.get("complex#9"));
        BatTestCase complexityTest9 = new BatTestCase(complexityConfig9, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig10 = defaultConfig.copy();
        complexityConfig10.setRule(rules.get("complex#10"));
        BatTestCase complexityTest10 = new BatTestCase(complexityConfig10, batConfig, TEST_RUNS);
        complexityTest10.setMaxACTHeight(3);
        
        DataCreatorConfig complexityConfig11 = defaultConfig.copy();
        complexityConfig11.setRule(rules.get("complex#11"));
        BatTestCase complexityTest11 = new BatTestCase(complexityConfig11, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig12 = defaultConfig.copy();
        complexityConfig12.setRule(rules.get("complex#12"));
        BatTestCase complexityTest12 = new BatTestCase(complexityConfig12, batConfig, TEST_RUNS);
        
        DataCreatorConfig complexityConfig13 = defaultConfig.copy();
        complexityConfig13.setRule(rules.get("complex#13"));
        BatTestCase complexityTest13 = new BatTestCase(complexityConfig13, batConfig, TEST_RUNS);
           
        /** TEST: ARITHMETIC **/
        DataCreatorConfig arithConfig1 = defaultConfig.copy();
        arithConfig1.setRule(rules.get("arithmetic#1"));
        BatTestCase arithTest1 = new BatTestCase(arithConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig arithConfig2 = defaultConfig.copy();
        arithConfig2.setRule(rules.get("arithmetic#2"));
        BatTestCase arithTest2 = new BatTestCase(arithConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig arithConfig3 = defaultConfig.copy();
        arithConfig3.setRule(rules.get("arithmetic#3"));
        BatTestCase arithTest3 = new BatTestCase(arithConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig arithConfig4 = defaultConfig.copy();
        arithConfig4.setRule(rules.get("arithmetic#4"));
        BatTestCase arithTest4 = new BatTestCase(arithConfig4, batConfig, TEST_RUNS);
        
        DataCreatorConfig arithConfig5 = defaultConfig.copy();
        arithConfig5.setRule(rules.get("arithmetic#5"));
        BatTestCase arithTest5 = new BatTestCase(arithConfig5, batConfig, TEST_RUNS);
        
        DataCreatorConfig arithConfig6 = defaultConfig.copy();
        arithConfig6.setRule(rules.get("arithmetic#6"));
        BatTestCase arithTest6 = new BatTestCase(arithConfig6, batConfig, TEST_RUNS);
        
        /** TEST: OR **/
        
        DataCreatorConfig orConfig1 = defaultConfig.copy();
        orConfig1.setRule(rules.get("or#1"));
        BatTestCase orTest1 = new BatTestCase(orConfig1, batConfig, TEST_RUNS);
        
        /** TEST: OR **/
        
        DataCreatorConfig notConfig1 = defaultConfig.copy();
        notConfig1.setRule(rules.get("not#1"));
        BatTestCase notTest1 = new BatTestCase(notConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig notConfig2 = defaultConfig.copy();
        notConfig2.setRule(rules.get("not#2"));
        BatTestCase notTest2 = new BatTestCase(notConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig notConfig3= defaultConfig.copy();
        notConfig3.setRule(rules.get("not#3"));
        BatTestCase notTest3 = new BatTestCase(notConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig notConfig4= defaultConfig.copy();
        notConfig4.setRule(rules.get("not#4"));
        BatTestCase notTest4 = new BatTestCase(notConfig4, batConfig, TEST_RUNS);
        
        /** TEST: AGGREGATION **/
        
        DataCreatorConfig aggr_config_1 = defaultConfig.copy();
        aggr_config_1.setRule(rules.get("aggregation#1"));
        BatTestCase aggrTest1 = new BatTestCase(aggr_config_1, batConfig, TEST_RUNS);
        
        DataCreatorConfig aggr_config_2 = defaultConfig.copy();
        aggr_config_2.setRule(rules.get("aggregation#2"));
        BatTestCase aggrTest2 = new BatTestCase(aggr_config_2, batConfig, TEST_RUNS);
        
        DataCreatorConfig aggr_config_3 = defaultConfig.copy();
        aggr_config_3.setRule(rules.get("aggregation#3"));
        BatTestCase aggrTest3 = new BatTestCase(aggr_config_3, batConfig, TEST_RUNS);
        
        DataCreatorConfig aggr_config_4 = defaultConfig.copy();
        aggr_config_4.setRule(rules.get("aggregation#4"));
        BatTestCase aggrTest4 = new BatTestCase(aggr_config_4, batConfig, TEST_RUNS);
        
        DataCreatorConfig aggr_config_5 = defaultConfig.copy();
        aggr_config_5.setRule(rules.get("aggregation#5"));
        BatTestCase aggrTest5 = new BatTestCase(aggr_config_5, batConfig, TEST_RUNS);

        /** TEST: SLIDING WINDOW **/
        
        DataCreatorConfig window_config_1 = defaultConfig.copy();
        window_config_1.setRule(rules.get("window#1"));
        BatTestCase windowTest1 = new BatTestCase(window_config_1, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_2 = defaultConfig.copy();
        window_config_2.setRule(rules.get("window#2"));
        BatTestCase windowTest2 = new BatTestCase(window_config_2, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_3 = defaultConfig.copy();
        window_config_3.setRule(rules.get("window#3"));
        BatTestCase windowTest3 = new BatTestCase(window_config_3, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_4 = defaultConfig.copy();
        window_config_4.setRule(rules.get("window#4"));
        BatTestCase windowTest4 = new BatTestCase(window_config_4, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_5 = defaultConfig.copy();
        window_config_5.setRule(rules.get("window#5"));
        BatTestCase windowTest5= new BatTestCase(window_config_5, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_6 = defaultConfig.copy();
        window_config_6.setRule(rules.get("window#6"));
        BatTestCase windowTest6 = new BatTestCase(window_config_6, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_7 = defaultConfig.copy();
        window_config_7.setRule(rules.get("window#7"));
        BatTestCase windowTest7 = new BatTestCase(window_config_7, batConfig, TEST_RUNS);
        
        DataCreatorConfig window_config_8 = defaultConfig.copy();
        window_config_8.setRule(rules.get("window#8"));
        BatTestCase windowTest8 = new BatTestCase(window_config_8, batConfig, TEST_RUNS);

        
        /** TEST: NUMBER OF EVENT TYPES **/
        // first: testing complexity rule #5
        DataCreatorConfig nrEventsConfigRuleNo5 = defaultConfig.copy();
        nrEventsConfigRuleNo5.setRule(rules.get("complex#5"));
        
        DataCreatorConfig nrEventsConfig1 = nrEventsConfigRuleNo5.copy();
        nrEventsConfig1.setNumEventTypes(10);
        BatTestCase nrEventTypesTest1 = new BatTestCase(nrEventsConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig2 = nrEventsConfigRuleNo5.copy();
        nrEventsConfig2.setNumEventTypes(20);
        BatTestCase nrEventTypesTest2 = new BatTestCase(nrEventsConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig3 = nrEventsConfigRuleNo5.copy();
        nrEventsConfig3.setNumEventTypes(40);
        BatTestCase nrEventTypesTest3 = new BatTestCase(nrEventsConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig4 = nrEventsConfigRuleNo5.copy();
        nrEventsConfig4.setNumEventTypes(60);
        BatTestCase nrEventTypesTest4 = new BatTestCase(nrEventsConfig4, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig5 = nrEventsConfigRuleNo5.copy();
        nrEventsConfig5.setNumEventTypes(80);
        BatTestCase nrEventTypesTest5 = new BatTestCase(nrEventsConfig5, batConfig, TEST_RUNS);
        
        // second: testing complexity rule #6
        DataCreatorConfig nrEventsConfigRuleNo6 = defaultConfig.copy();
        nrEventsConfigRuleNo6.setRule(rules.get("complex#6"));
        
        DataCreatorConfig nrEventsConfig6 = nrEventsConfigRuleNo6.copy();
        nrEventsConfig6.setNumEventTypes(10);
        BatTestCase nrEventTypesTest6 = new BatTestCase(nrEventsConfig6, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig7 = nrEventsConfigRuleNo6.copy();
        nrEventsConfig7.setNumEventTypes(20);
        BatTestCase nrEventTypesTest7 = new BatTestCase(nrEventsConfig7, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig8 = nrEventsConfigRuleNo6.copy();
        nrEventsConfig8.setNumEventTypes(40);
        BatTestCase nrEventTypesTest8 = new BatTestCase(nrEventsConfig8, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig9= nrEventsConfigRuleNo6.copy();
        nrEventsConfig9.setNumEventTypes(60);
        BatTestCase nrEventTypesTest9 = new BatTestCase(nrEventsConfig9, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrEventsConfig10 = nrEventsConfigRuleNo6.copy();
        nrEventsConfig10.setNumEventTypes(80);
        BatTestCase nrEventTypesTest10 = new BatTestCase(nrEventsConfig10, batConfig, TEST_RUNS);

        
        /** TEST: NUMBER OF ATTRIBUTE TYPES **/
        // first: testing complexity rule #5
        Map<String, AttributeConfig<?>> specificRange = new HashMap<>();
        specificRange.put("ID", new AttributeConfig<Integer>(1, 10));
        specificRange.put("ROOM", new AttributeConfig<Integer>(1, 10));
        specificRange.put("TEMP", new AttributeConfig<Integer>(1, 100));

        DataCreatorConfig nrAttributeTypesConfig = defaultConfig.copy();
        nrAttributeTypesConfig.setRule(rules.get("complex#5"));
        
        DataCreatorConfig nrAttributesConfig1 = nrAttributeTypesConfig.copy();
        nrAttributesConfig1.getFixedSensorRange().putAll(putAdditionalGenericAttributes(3, specificRange));
        BatTestCase nrAttributeTypesTest1 = new BatTestCase(nrAttributesConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig2 = nrAttributeTypesConfig.copy();
        nrAttributesConfig2.getFixedSensorRange().putAll(putAdditionalGenericAttributes(7, specificRange));
        BatTestCase nrAttributeTypesTest2 = new BatTestCase(nrAttributesConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig3 = nrAttributeTypesConfig.copy();
        nrAttributesConfig3.getFixedSensorRange().putAll(putAdditionalGenericAttributes(17, specificRange));
        BatTestCase nrAttributeTypesTest3 = new BatTestCase(nrAttributesConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig4 = nrAttributeTypesConfig.copy();
        nrAttributesConfig4.getFixedSensorRange().putAll(putAdditionalGenericAttributes(37, specificRange));
        BatTestCase nrAttributeTypesTest4 = new BatTestCase(nrAttributesConfig4, batConfig, TEST_RUNS);
        
        // second: testing complexity rule #6
        DataCreatorConfig nrAttributesConfig5 = nrAttributesConfig1.copy();
        nrAttributesConfig5.setRule(rules.get("complex#6"));
        BatTestCase nrAttributeTypesTest5 = new BatTestCase(nrAttributesConfig5, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig6 = nrAttributesConfig2.copy();
        nrAttributesConfig6.setRule(rules.get("complex#6"));
        BatTestCase nrAttributeTypesTest6 = new BatTestCase(nrAttributesConfig6, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig7 = nrAttributesConfig3.copy();
        nrAttributesConfig7.setRule(rules.get("complex#6"));
        BatTestCase nrAttributeTypesTest7 = new BatTestCase(nrAttributesConfig7, batConfig, TEST_RUNS);
        
        DataCreatorConfig nrAttributesConfig8 = nrAttributesConfig4.copy();
        nrAttributesConfig8.setRule(rules.get("complex#6"));
        BatTestCase nrAttributeTypesTest8 = new BatTestCase(nrAttributesConfig8, batConfig, TEST_RUNS);
        
        /** TEST: HITS**/
        DataCreatorConfig hitTestConfig1 = defaultConfig.copy();
        hitTestConfig1.setRule(rules.get("complex#6"));
        hitTestConfig1.setHitPercentage(0.001);
        BatTestCase hitTest1 = new BatTestCase(hitTestConfig1, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig2 = defaultConfig.copy();
        hitTestConfig2.setRule(rules.get("complex#6"));
        hitTestConfig2.setHitPercentage(0.01);
        BatTestCase hitTest2 = new BatTestCase(hitTestConfig2, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig3 = defaultConfig.copy();
        hitTestConfig3.setRule(rules.get("complex#6"));
        hitTestConfig3.setHitPercentage(0.1);
        BatTestCase hitTest3 = new BatTestCase(hitTestConfig3, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig4 = defaultConfig.copy();
        hitTestConfig4.setRule(rules.get("complex#6"));
        hitTestConfig4.setHitPercentage(0.3);
        BatTestCase hitTest4 = new BatTestCase(hitTestConfig4, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig5 = defaultConfig.copy();
        hitTestConfig5.setRule(rules.get("complex#6"));
        hitTestConfig5.setHitPercentage(0.6);
        BatTestCase hitTest5 = new BatTestCase(hitTestConfig5, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig6 = defaultConfig.copy();
        hitTestConfig6.setRule(rules.get("complex#6"));
        hitTestConfig6.setHitPercentage(1.0);
        BatTestCase hitTest6 = new BatTestCase(hitTestConfig6, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig7 = defaultConfig.copy();
        hitTestConfig7.setRule(rules.get("complex#11"));
        hitTestConfig7.setHitPercentage(0.001);
        BatTestCase hitTest7 = new BatTestCase(hitTestConfig7, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig8 = defaultConfig.copy();
        hitTestConfig8.setRule(rules.get("complex#11"));
        hitTestConfig8.setHitPercentage(0.01);
        BatTestCase hitTest8 = new BatTestCase(hitTestConfig8, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig9 = defaultConfig.copy();
        hitTestConfig9.setRule(rules.get("complex#11"));
        hitTestConfig9.setHitPercentage(0.1);
        BatTestCase hitTest9 = new BatTestCase(hitTestConfig9, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig10 = defaultConfig.copy();
        hitTestConfig10.setRule(rules.get("complex#11"));
        hitTestConfig10.setHitPercentage(0.3);
        BatTestCase hitTest10 = new BatTestCase(hitTestConfig10, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig11 = defaultConfig.copy();
        hitTestConfig11.setRule(rules.get("complex#11"));
        hitTestConfig11.setHitPercentage(0.6);
        BatTestCase hitTest11 = new BatTestCase(hitTestConfig11, batConfig, TEST_RUNS);
        
        DataCreatorConfig hitTestConfig12 = defaultConfig.copy();
        hitTestConfig12.setRule(rules.get("complex#11"));
        hitTestConfig12.setHitPercentage(1.0);
        BatTestCase hitTest12 = new BatTestCase(hitTestConfig12, batConfig, TEST_RUNS);

        /** TEST: EVENTS PER SECOND **/
        
        DataCreatorConfig eventPerSecondConfig = defaultConfig.copy();
        eventPerSecondConfig.setRule(rules.get("complex#6"));
        eventPerSecondConfig.setMinIntermediateSeconds(1);
        eventPerSecondConfig.setMaxIntermediateSeconds(1);
        BatTestCase eventPerSecondTest = new BatTestCase(eventPerSecondConfig, batConfig, TEST_RUNS);
        
        /** TEST: TRAFFIC EXAMPLE **/
     	Map<String, AttributeConfig<?>> trafficRange = new HashMap<>();
     	trafficRange.put("INTENSITY", new AttributeConfig<Integer>(0, 9400));
     	trafficRange.put("OCUPATION", new AttributeConfig<Integer>(0, 100));
     	trafficRange.put("VMED", new AttributeConfig<Integer>(0, 100));

         DataCreatorConfig trafficConfig1 = new DataCreatorConfig(); 
         trafficConfig1.setNumEventTypes(1);
         trafficConfig1.setNumEventAttributes(3);
         trafficConfig1.getFixedSensorRange().putAll(trafficRange);
         trafficConfig1.setComplexEvent(defaultComplexEvent);
         trafficConfig1.setData(DatasetEnum.TRAFFIC_FIRST_HALF);
         
         // tests for the first half of 2018
         trafficConfig1.setRule(rules.get("traffic#1"));
         BatTestCase trafficTest1 = new BatTestCase(trafficConfig1, batConfig, TEST_RUNS);
         trafficTest1.setMaxACTHeight(4);
         
         DataCreatorConfig trafficConfig2 = trafficConfig1.copy();
         trafficConfig2.setRule(rules.get("traffic#2"));
         BatTestCase trafficTest2 = new BatTestCase(trafficConfig2, batConfig, TEST_RUNS);
         trafficTest2.setMaxACTHeight(4);
         
         DataCreatorConfig trafficConfig3 = trafficConfig1.copy();
         trafficConfig3.setRule(rules.get("traffic#3"));
         BatTestCase trafficTest3 = new BatTestCase(trafficConfig3, batConfig, TEST_RUNS);
         trafficTest3.setMaxACTHeight(4);
         
         // tests for the second half of 2018
         DataCreatorConfig trafficConfig4 = trafficConfig1.copy();
         trafficConfig4.setRule(rules.get("traffic#1"));
         trafficConfig4.setData(DatasetEnum.TRAFFIC_SECOND_HALF);
         BatTestCase trafficTest4 = new BatTestCase(trafficConfig4, batConfig, TEST_RUNS);
         trafficTest4.setMaxACTHeight(4);
         
         DataCreatorConfig trafficConfig5 = trafficConfig1.copy();
         trafficConfig5.setRule(rules.get("traffic#2"));
         trafficConfig5.setData(DatasetEnum.TRAFFIC_SECOND_HALF);
         BatTestCase trafficTest5 = new BatTestCase(trafficConfig5, batConfig, TEST_RUNS);
         trafficTest5.setMaxACTHeight(4);
         
         DataCreatorConfig trafficConfig6 = trafficConfig1.copy();
         trafficConfig6.setRule(rules.get("traffic#3"));
         trafficConfig6.setData(DatasetEnum.TRAFFIC_SECOND_HALF);
         BatTestCase trafficTest6 = new BatTestCase(trafficConfig6, batConfig, TEST_RUNS);
         trafficTest6.setMaxACTHeight(4);

         
         //register all test cases
        bat_tests.put("complex#1", complexityTest1);
        bat_tests.put("complex#2", complexityTest2);
        bat_tests.put("complex#3", complexityTest3);
        bat_tests.put("complex#4", complexityTest4);
        bat_tests.put("complex#5", complexityTest5);
        bat_tests.put("complex#6", complexityTest6);
        bat_tests.put("complex#7", complexityTest7);
        bat_tests.put("complex#8", complexityTest8);
        bat_tests.put("complex#9", complexityTest9);
        bat_tests.put("complex#10", complexityTest10);
        bat_tests.put("complex#11", complexityTest11);
        bat_tests.put("complex#12", complexityTest12);
        bat_tests.put("complex#13", complexityTest13);
        
        bat_tests.put("arithmetic#1", arithTest1);
        bat_tests.put("arithmetic#2", arithTest2);
        bat_tests.put("arithmetic#3", arithTest3);
        bat_tests.put("arithmetic#4", arithTest4);
        bat_tests.put("arithmetic#5", arithTest5);
        bat_tests.put("arithmetic#6", arithTest6);
        
        bat_tests.put("or#1", orTest1);
        
        bat_tests.put("not#1", notTest1);
        bat_tests.put("not#2", notTest2);
        bat_tests.put("not#3", notTest3);
        bat_tests.put("not#4", notTest4);
        
        bat_tests.put("aggregation#1", aggrTest1);
        bat_tests.put("aggregation#2", aggrTest2);
        bat_tests.put("aggregation#3", aggrTest3);
        bat_tests.put("aggregation#4", aggrTest4);
        bat_tests.put("aggregation#5", aggrTest5);
        
        bat_tests.put("window#1", windowTest1);
        bat_tests.put("window#2", windowTest2);
        bat_tests.put("window#3", windowTest3);
        bat_tests.put("window#4", windowTest4);
        bat_tests.put("window#5", windowTest5);
        bat_tests.put("window#6", windowTest6);
        bat_tests.put("window#7", windowTest7);
        bat_tests.put("window#8", windowTest8);
        
        bat_tests.put("nr_event_types#1", nrEventTypesTest1);
        bat_tests.put("nr_event_types#2", nrEventTypesTest2);
        bat_tests.put("nr_event_types#3", nrEventTypesTest3);
        bat_tests.put("nr_event_types#4", nrEventTypesTest4);
        bat_tests.put("nr_event_types#5", nrEventTypesTest5);
        bat_tests.put("nr_event_types#6", nrEventTypesTest6);
        bat_tests.put("nr_event_types#7", nrEventTypesTest7);
        bat_tests.put("nr_event_types#8", nrEventTypesTest8);
        bat_tests.put("nr_event_types#9", nrEventTypesTest9);
        bat_tests.put("nr_event_types#10", nrEventTypesTest10);
        
        bat_tests.put("nr_attr_types#1", nrAttributeTypesTest1);
        bat_tests.put("nr_attr_types#2", nrAttributeTypesTest2);
        bat_tests.put("nr_attr_types#3", nrAttributeTypesTest3);
        bat_tests.put("nr_attr_types#4", nrAttributeTypesTest4);
        bat_tests.put("nr_attr_types#5", nrAttributeTypesTest5);
        bat_tests.put("nr_attr_types#6", nrAttributeTypesTest6);
        bat_tests.put("nr_attr_types#7", nrAttributeTypesTest7);
        bat_tests.put("nr_attr_types#8", nrAttributeTypesTest8);
        
        bat_tests.put("hit#1", hitTest1);
        bat_tests.put("hit#2", hitTest2);
        bat_tests.put("hit#3", hitTest3);
        bat_tests.put("hit#4", hitTest4);
        bat_tests.put("hit#5", hitTest5);
        bat_tests.put("hit#6", hitTest6);
        bat_tests.put("hit#7", hitTest7);
        bat_tests.put("hit#8", hitTest8);
        bat_tests.put("hit#9", hitTest9);
        bat_tests.put("hit#10", hitTest10);
        bat_tests.put("hit#11", hitTest11);
        bat_tests.put("hit#12", hitTest12);

        bat_tests.put("events_per_second#1", eventPerSecondTest);
        
        bat_tests.put("traffic#1", trafficTest1);
        bat_tests.put("traffic#2", trafficTest2);
        bat_tests.put("traffic#3", trafficTest3);
        bat_tests.put("traffic#4", trafficTest4);
        bat_tests.put("traffic#5", trafficTest5);
        bat_tests.put("traffic#6", trafficTest6);
    }

    private static void initRules() {
    	//A0.ROOM = B0.ROOM
    	AttributeCondition aRoom_eq_bRoom = new EqualToAttributeComparisonOperator(new EventAttribute("A0", "ROOM", null),
                new EventAttribute("B0", "ROOM", null));
    	
    	//A0.ROOM = C0.ROOM
        AttributeCondition aRoom_eq_cRoom = new EqualToAttributeComparisonOperator(new EventAttribute("A0", "ROOM", null),
                new EventAttribute("C0", "ROOM", null));
        
        //C0.ROOM = D0.ROOM
        AttributeCondition cRoom_eq_dRoom = new EqualToAttributeComparisonOperator(new EventAttribute("C0", "ROOM", null),
                new EventAttribute("D0", "ROOM", null));
        
        //E0.ROOM = F0.ROOM
        AttributeCondition eRoom_eq_fRoom = new EqualToAttributeComparisonOperator(new EventAttribute("E0", "ROOM", null),
                new EventAttribute("F0", "ROOM", null));
        
        //A0.TEMP > B0.TEMP
        AttributeCondition aTemp_gt_bTemp = new GreaterThanAttributeComparisonOperator(new EventAttribute("A0", "TEMP", null),
                new EventAttribute("B0", "TEMP", null));
        
        //B0.TEMP > C0.TEMP
        AttributeCondition bTemp_gt_cTemp = new GreaterThanAttributeComparisonOperator(new EventAttribute("B0", "TEMP", null),
                new EventAttribute("C0", "TEMP", null));
        
        //C0.TEMP > D0.TEMP
        AttributeCondition cTemp_gt_dTemp = new GreaterThanAttributeComparisonOperator(new EventAttribute("C0", "TEMP", null),
                new EventAttribute("D0", "TEMP", null));
        
        //A0.TEMP < B0.TEMP
        AttributeCondition aTemp_lt_bTemp = new LessThanAttributeComparisonOperator(new EventAttribute("A0", "TEMP", null),
                new EventAttribute("B0", "TEMP", null));
        
        //A0.TEMP < AVG(A0.TEMP)
        AttributeCondition aTemp_gt_aTempAVG = new LessThanAttributeComparisonOperator(new AvgAggregateAttribute("A0", "TEMP", null),
        		new EventAttribute("A0", "TEMP", null));
        
        //A0.TEMP > AVG(B0.TEMP)
        AttributeCondition aTemp_gt_bTempAVG = new LessThanAttributeComparisonOperator(new AvgAggregateAttribute("B0", "TEMP", null),
        		new EventAttribute("A0", "TEMP", null));
        
        //SUM(A0.ROOM) > 500
        AttributeCondition aRoomSUM_gt_500 = new GreaterThanAttributeComparisonOperator(
        		new SumAggregateAttribute("A0", "ROOM", null),new ConstantAttribute(500));
        
        //SUM(A0.TEMP) > 5000
        AttributeCondition aTempSUM_gt_5000 = new GreaterThanAttributeComparisonOperator(
        		new SumAggregateAttribute("A0", "TEMP", null),new ConstantAttribute(5000));
        
        //A0.TEMP = MIN(A0.TEMP) AND B0.TEMP = MAX(B0.TEMP) 
        AttributeCondition aTemp_eq_aTempMIN_and_bTemp_eq_bTempMAX = new AndAttributeOperator(
        		new EqualToAttributeComparisonOperator(new MinAggregateAttribute("A0", "TEMP", null), new EventAttribute("A0", "TEMP", null)),
        		new EqualToAttributeComparisonOperator(new MaxAggregateAttribute("B0", "TEMP", null), new EventAttribute("B0", "TEMP", null)));
        
        //A0.ROOM = B0.ROOM AND A0.TEMP > B0.TEMP
        AttributeCondition aRoom_eq_bRoom_and_aTemp_gt_bTemp = new AndAttributeOperator(aRoom_eq_bRoom, aTemp_gt_bTemp);
        
        //A0.ROOM = B0.ROOM AND A0.ROOM = C0.ROOM
        AttributeCondition aRoom_eq_bRoom_and_aRoom_eq_cRoom = new AndAttributeOperator(aRoom_eq_bRoom, aRoom_eq_cRoom);
        
        //A0.ROOM = B0.ROOM AND C0.ROOM = D0.ROOM
        AttributeCondition aRoom_eq_bRoom_and_cRoom_eq_dRoom = new AndAttributeOperator(aRoom_eq_bRoom, cRoom_eq_dRoom);
        
        //A0.ROOM = B0.ROOM AND C0.TEMP > D0.TEMP
        AttributeCondition aRoom_eq_bRoom_and_cTemp_gt_dTemp = new AndAttributeOperator(aRoom_eq_bRoom, cTemp_gt_dTemp);
        
        //A0.TEMP > B0.TEMP AND B0.TEMP > C0.TEMP AND C0.TEMP > D0.TEMP
        AttributeCondition aTemp_gt_bTemp_and_bTemp_gt_cTemp_and_cTemp_gt_dTemp = new AndAttributeOperator(new AndAttributeOperator(aTemp_gt_bTemp, bTemp_gt_cTemp), cTemp_gt_dTemp);
        
        //A0.TEMP > B0.TEMP AND B0.TEMP > C0.TEMP AND E0.ROOM > F0.ROOM
        AttributeCondition aTemp_gt_bTemp_and_bTemp_gt_cTemp_and_eRoom_eq_fRoom = new AndAttributeOperator(new AndAttributeOperator(aTemp_gt_bTemp, cTemp_gt_dTemp), eRoom_eq_fRoom);
        
        //A0.ROOM = B0.ROOM AND A0.TEMP > B0.TEMP AND A0.TEMP > 80
        AttributeCondition aRoom_eq_bRoom_and_aTemp_gt_bTemp_and_aTemp_gt_80 = new AndAttributeOperator(aRoom_eq_bRoom, new AndAttributeOperator(aTemp_gt_bTemp, new GreaterThanAttributeComparisonOperator(new EventAttribute("A0", "TEMP", null), new ConstantAttribute(80.0))));
        //A0.TEMP > 60 AND B0.TEMP > 60 AND C0.TEMP > 60 AND D0.TEMP > 60
        AttributeCondition aTemp_gt_60_and_bTemp_gt_60_and_cTemp_gt_60_and_dTemp_gt_60 = new AndAttributeOperator(new GreaterThanAttributeComparisonOperator(new EventAttribute("A0", "TEMP", null), new ConstantAttribute(60)),
        		new AndAttributeOperator(new GreaterThanAttributeComparisonOperator(new EventAttribute("B0", "TEMP", null), new ConstantAttribute(60)),
        				new AndAttributeOperator(new GreaterThanAttributeComparisonOperator(new EventAttribute("C0", "TEMP", null), new ConstantAttribute(60)),
        						new GreaterThanAttributeComparisonOperator(new EventAttribute("D0", "TEMP", null), new ConstantAttribute(60)))));  
        
        //A
        Rule rule_a = new Rule(new Event("A"), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        //A -> B
        Rule rule_a_seq_b = new Rule(new SequenceEventOperator(new Event("A"), new Event("B")), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        //A -> B -> C
        Rule rule_a_seq_b_seq_c = new Rule(new SequenceEventOperator(
        		new SequenceEventOperator(new Event("A"),new Event("B")), new Event("C")), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        //(A -> B) AND (C -> D)
        Rule rule_a_seq_b_and_c_seq_d = new Rule(new AndEventOperator(new SequenceEventOperator(new Event("A"), new Event("B")), 
        		new SequenceEventOperator(new Event("C"), new Event("D"))), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT"))); 
        // A AND B AND C AND D AND E AND F
        Rule rule_a_and_b_and_c_and_d_and_e_and_f = new Rule(new AndEventOperator(new Event("A"), new AndEventOperator(new Event("B"), new AndEventOperator(new Event("C"), new AndEventOperator(new Event("D"),
        		new AndEventOperator(new Event("E"), new Event("F")))))), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        
        Rule complexityRule1 = rule_a_seq_b.copy();
        complexityRule1.setAttributeConditionTreeRoot(aRoom_eq_bRoom);
        
        Rule complexityRule2 = rule_a_seq_b.copy();
        complexityRule2.setAttributeConditionTreeRoot(aTemp_gt_bTemp);
        
        Rule complexityRule3 = rule_a_seq_b.copy();
        complexityRule3.setAttributeConditionTreeRoot(aTemp_lt_bTemp);
        
        Rule complexityRule4 = rule_a_seq_b.copy();
        complexityRule4.setAttributeConditionTreeRoot(new NotAttributeOperator(aRoom_eq_bRoom));
        
        Rule complexityRule5 = rule_a_seq_b.copy();
        complexityRule5.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_aTemp_gt_bTemp);
        
        Rule complexityRule6 = rule_a_seq_b_seq_c.copy();
        complexityRule6.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_aTemp_gt_bTemp);
        
        Rule complexityRule7 = rule_a_seq_b_seq_c.copy();
        complexityRule7.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_aRoom_eq_cRoom);
        
        Rule complexityRule8 = rule_a_seq_b_and_c_seq_d.copy();
        complexityRule8.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_cRoom_eq_dRoom);
        
        Rule complexityRule9 = rule_a_seq_b_and_c_seq_d.copy();
        complexityRule9.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_cTemp_gt_dTemp);
        
        Rule complexityRule10 = rule_a_seq_b_and_c_seq_d.copy();
        complexityRule10.setAttributeConditionTreeRoot(aTemp_gt_bTemp_and_bTemp_gt_cTemp_and_cTemp_gt_dTemp);
        
        Rule complexityRule11 = rule_a_and_b_and_c_and_d_and_e_and_f.copy();
        complexityRule11.setAttributeConditionTreeRoot(aTemp_gt_bTemp_and_bTemp_gt_cTemp_and_eRoom_eq_fRoom);
        
        Rule complexityRule12 = rule_a_seq_b.copy();
        complexityRule12.setAttributeConditionTreeRoot(aRoom_eq_bRoom_and_aTemp_gt_bTemp_and_aTemp_gt_80);
        
        Rule complexityRule13 = rule_a_seq_b_and_c_seq_d.copy();
        complexityRule13.setAttributeConditionTreeRoot(aTemp_gt_60_and_bTemp_gt_60_and_cTemp_gt_60_and_dTemp_gt_60);      
        
        /** RULES: OR**/
        Rule orRule = rule_a_seq_b.copy();
        orRule.setAttributeConditionTreeRoot(new OrAttributeOperator(aRoom_eq_bRoom, aTemp_gt_bTemp));
        
        /** RULES: ARITHMETIC**/
        Rule addRule1 = rule_a_seq_b.copy();
        addRule1.setAttributeConditionTreeRoot(new GreaterThanAttributeComparisonOperator(
        		new AdditionOperator(
        				new EventAttribute("A0", "TEMP", null), 
        				new EventAttribute("B0", "TEMP", null)), 
        		new ConstantAttribute(110.0)));
        
        Rule addRule2 = rule_a_seq_b.copy();
        addRule2.setAttributeConditionTreeRoot(new GreaterThanAttributeComparisonOperator(
        		new SubtractionOperator(
        				new EventAttribute("A0", "TEMP", null), 
        				new EventAttribute("B0", "TEMP", null)), 
        		new ConstantAttribute(60.0)));
        
        Rule addRule3 = rule_a_seq_b.copy();
        addRule3.setAttributeConditionTreeRoot(new GreaterThanAttributeComparisonOperator(
        		new SubtractionOperator(
        				new EventAttribute("A0", "TEMP", null), 
        				new EventAttribute("B0", "TEMP", null)), 
        		new AdditionOperator(
        				new EventAttribute("A0", "ROOM", null), 
        				new EventAttribute("B0", "ROOM", null))));
        
        Rule addRule4 = rule_a_seq_b.copy();
        addRule4.setAttributeConditionTreeRoot(new GreaterThanAttributeComparisonOperator(
        		new AdditionOperator(
        				new EventAttribute("A0", "ROOM", null), 
        				new EventAttribute("B0", "ROOM", null)), 
        		new ConstantAttribute(60.0)));
        
        Rule addRule5 = rule_a_seq_b.copy();
        addRule5.setAttributeConditionTreeRoot(new GreaterThanAttributeComparisonOperator(
        		new AdditionOperator(
        				new EventAttribute("A0", "ROOM", null), 
        				new EventAttribute("B0", "ROOM", null)), 
        		new EventAttribute("A0", "TEMP", null)));
        
        Rule addRule6 = rule_a_seq_b.copy();
        addRule6.setAttributeConditionTreeRoot(new EqualToAttributeComparisonOperator(
        		new SubtractionOperator(
        				new EventAttribute("A0", "TEMP", null), 
        				new EventAttribute("B0", "TEMP", null)), 
        		new EventAttribute("B0", "ROOM", null)));
        
        /** RULES: NOT**/
        Rule notRule1 = new Rule(
        				new NotEventOperator(
        						new SequenceEventOperator(
        								new Event("A"),
        								new Event("C")), 
        						new Event("B")), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        notRule1.setAttributeConditionTreeRoot(aRoom_eq_cRoom);
        
        Rule notRule2 = new Rule(
				new NotEventOperator(
						new SequenceEventOperator(
								new Event("A"),
								new SequenceEventOperator(new Event("C"), new Event("D"))), 
						new Event("B")), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        notRule2.setAttributeConditionTreeRoot(aRoom_eq_cRoom);
        
        Rule notRule3 = new Rule(
				new NotEventOperator(
						new AndEventOperator(
								new Event("A"),
								new Event("C")), 
						new Event("B")), new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        notRule3.setAttributeConditionTreeRoot(aRoom_eq_cRoom);
        
        Rule notRule4 = new Rule(new SequenceEventOperator(new NotEventOperator(
				new SequenceEventOperator(
						new Event("A"),
						new Event("C")), 
				new Event("B")), new Event("B"))
				, new TimeWindow(60, ChronoUnit.SECONDS, 0, 0), new Action(new Event("HIT")));
        notRule4.setAttributeConditionTreeRoot(aRoom_eq_cRoom);
        /** RULES: AGGREGATION**/
        
        Rule aggrRule1 = rule_a.copy();
        aggrRule1.setAttributeConditionTreeRoot(aTemp_gt_aTempAVG);
        
        Rule aggrRule2 = rule_a_seq_b_seq_c.copy();
        aggrRule2.setAttributeConditionTreeRoot(new AndAttributeOperator(aRoom_eq_bRoom, aTemp_gt_bTempAVG));
        
        Rule aggrRule3 = rule_a.copy();
        aggrRule3.setWindow(new TimeWindow(3000, ChronoUnit.SECONDS, 0, 0));
        aggrRule3.setAttributeConditionTreeRoot(aRoomSUM_gt_500);
        
        Rule aggrRule4 = rule_a.copy();
        aggrRule4.setWindow(new TimeWindow(3000, ChronoUnit.SECONDS, 0, 0));
        aggrRule4.setAttributeConditionTreeRoot(aTempSUM_gt_5000);

        Rule aggrRule5 = rule_a_seq_b.copy();
        aggrRule5.setAttributeConditionTreeRoot(aTemp_eq_aTempMIN_and_bTemp_eq_bTempMAX);

        
        /** RULES: TIME WINDOW **/
        
        Rule timeWindowRule1 = aggrRule1.copy();
        
        Rule timeWindowRule2 = aggrRule1.copy();
        timeWindowRule2.setWindow(new TimeWindow(600, ChronoUnit.SECONDS, 0, 0));
        
        Rule timeWindowRule3 = aggrRule1.copy();
        timeWindowRule3.setWindow(new TimeWindow(3000, ChronoUnit.SECONDS, 0, 0));
        
        Rule timeWindowRule4 = aggrRule1.copy();
        timeWindowRule4.setWindow(new TimeWindow(6000, ChronoUnit.SECONDS, 0, 0));
        
        Rule timeWindowRule5 = complexityRule6.copy();
        
        Rule timeWindowRule6 = complexityRule6.copy();
        timeWindowRule6.setWindow(new TimeWindow(600, ChronoUnit.SECONDS, 0, 0));
        
        Rule timeWindowRule7 = complexityRule6.copy();
        timeWindowRule7.setWindow(new TimeWindow(3000, ChronoUnit.SECONDS, 0, 0));
        
        Rule timeWindowRule8 = complexityRule6.copy();
        timeWindowRule8.setWindow(new TimeWindow(6000, ChronoUnit.SECONDS, 0, 0));
        
        /** RULES: TRAFFIC **/
        Rule trafficRuleECT = new Rule(new Event("A"), new TimeWindow(15, ChronoUnit.SECONDS, 0, 0),  new Action(new Event("HIT")));	//window size arbitrary
        Rule trafficRule1 = trafficRuleECT.copy();
        AttributeCondition free_act = new OrAttributeOperator(
       		 new AndAttributeOperator(new LessThanAttributeComparisonOperator(new EventAttribute("A0", "OCUPATION", null), new ConstantAttribute(10)),
       				 new AndAttributeOperator(new LessThanAttributeComparisonOperator(new EventAttribute("A0", "INTENSITY", null), new ConstantAttribute(6000)),
       						 new GreaterThanAttributeComparisonOperator(new EventAttribute("A0", "VMED", null), new ConstantAttribute(60)))), 
       		 new AndAttributeOperator(new LessThanAttributeComparisonOperator(new EventAttribute("A0", "OCUPATION", null), new ConstantAttribute(2)), 
       				new LessThanAttributeComparisonOperator(new EventAttribute("A0", "INTENSITY", null), new ConstantAttribute(500))));
        trafficRule1.setAttributeConditionTreeRoot(free_act);
        
        Rule trafficRule2 = trafficRuleECT.copy();
        AttributeCondition congested_act = new AndAttributeOperator(new GreaterThanAttributeComparisonOperator(new EventAttribute("A0", "OCUPATION", null), new ConstantAttribute(30)), 
       		 new AndAttributeOperator(new LessThanAttributeComparisonOperator(new EventAttribute("A0", "INTENSITY", null), new ConstantAttribute(6000)), 
       				 new LessThanAttributeComparisonOperator(new EventAttribute("A0", "VMED", null), new ConstantAttribute(40))));
        trafficRule2.setAttributeConditionTreeRoot(congested_act);
        
        Rule trafficRule3 = trafficRuleECT.copy();
        trafficRule3.setAttributeConditionTreeRoot(new NotAttributeOperator(new OrAttributeOperator(free_act, congested_act)));
        
        rules.put("complex#1", complexityRule1);
        rules.put("complex#2", complexityRule2);
        rules.put("complex#3", complexityRule3);
        rules.put("complex#4", complexityRule4);
        rules.put("complex#5", complexityRule5);
        rules.put("complex#6", complexityRule6);
        rules.put("complex#7", complexityRule7);
        rules.put("complex#8", complexityRule8);
        rules.put("complex#9", complexityRule9);
        rules.put("complex#10", complexityRule10);
        rules.put("complex#11", complexityRule11);
        rules.put("complex#12", complexityRule12);
        rules.put("complex#13", complexityRule13);
        
        rules.put("arithmetic#1", addRule1);
        rules.put("arithmetic#2", addRule2);
        rules.put("arithmetic#3", addRule3);
        rules.put("arithmetic#4", addRule4);
        rules.put("arithmetic#5", addRule5);
        rules.put("arithmetic#6", addRule6);
        
        rules.put("or#1", orRule);
        
        rules.put("not#1", notRule1);
        rules.put("not#2", notRule2);
        rules.put("not#3", notRule3);
        rules.put("not#4", notRule4);
        
        rules.put("aggregation#1", aggrRule1);
        rules.put("aggregation#2", aggrRule2);
        rules.put("aggregation#3", aggrRule3);
        rules.put("aggregation#4", aggrRule4);
        rules.put("aggregation#5", aggrRule5);
        
        
        rules.put("window#1", timeWindowRule1);
        rules.put("window#2", timeWindowRule2);
        rules.put("window#3", timeWindowRule3);
        rules.put("window#4", timeWindowRule4);
        rules.put("window#5", timeWindowRule5);
        rules.put("window#6", timeWindowRule6);
        rules.put("window#7", timeWindowRule7);
        rules.put("window#8", timeWindowRule8);
        
        rules.put("traffic#1", trafficRule1);
        rules.put("traffic#2", trafficRule2);
        rules.put("traffic#3", trafficRule3);

    }
   
    private static Map<String, AttributeConfig<?>> putAdditionalGenericAttributes(int numberAttributeTypes, Map<String, AttributeConfig<?>> map) {
    	String defaultAttributeName = "Att";
    	for(int i=0; i<numberAttributeTypes;i++) {
    		AttributeConfig<Integer> range = new AttributeConfig<Integer>(1, 10);
    		if(i%3==0 && i>0) {
    			range = new AttributeConfig<Integer>(1, 100);
    		}
    		map.put(defaultAttributeName + i, range);
    	}
    	return map;
     }
}
