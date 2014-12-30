package TestsExample;

import generator.VIDataGenerator;
import org.testng.annotations.DataProvider;

import java.util.List;

import static generator.ValuesGroup.ALL_GROUPS;

/**
 * Created by 12345 on 29.12.2014.
 */
public class SomeDataProvider {

    @DataProvider(name = "someDP")
    public static Object[][] someDP() throws Exception {
        List<Example> examplesDefaultC = new VIDataGenerator(Example.class).generateCombinations(ALL_GROUPS);
        List<Example> examplesDefaultV = new VIDataGenerator(Example.class).generateValues(ALL_GROUPS);
        List<Example> examplesAll = new VIDataGenerator(Example.class).generateCombinations(ALL_GROUPS);
        List<Example> examples1w = new VIDataGenerator(Example.class).setWhiteList("subClass.Strings").generateCombinations(ALL_GROUPS);
        List<Example> examples1b = new VIDataGenerator(Example.class).setBlackList("subClass.Strings").generateCombinations(ALL_GROUPS);
        List<Example> examples1w2 = new VIDataGenerator(Example.class).setWhiteList("*.Strings").generateCombinations(ALL_GROUPS);
        List<Example> examples1b2 = new VIDataGenerator(Example.class).setBlackList("*.Strings").generateCombinations(ALL_GROUPS);

        List examplesG1w = new VIDataGenerator(Example.class)
                .setGenRule(b -> !b.toString().contains("subClass") || b.toString().contains("Enums")).generateCombinations(ALL_GROUPS);

        List<Example> examples2 = new VIDataGenerator(Example.class).generateValues(ALL_GROUPS);
        List<Example> examples3 = new VIDataGenerator(Example.class).generateCombinations("Test");
        List<Example> examples4 = new VIDataGenerator(Example.class).generateValues("Test2");
        return new Object[][] {};
    }
}
