package TestsExample;

import ru.viqa.test_data_generator.generator.VIDataGenerator;
import org.testng.annotations.DataProvider;

import java.util.List;

import static TestsExample.TestEnum.One;
import static ru.viqa.test_data_generator.generator.VIDataGenerator.to2DArray;
import static ru.viqa.test_data_generator.generator.ValuesGroup.ALL_GROUPS;

/**
 * Created by 12345 on 29.12.2014.
 */
public class SomeDataProvider {

    @DataProvider(name = "someDP")
    public static Object[][] someDP() throws Exception {
        List<Example> examplesDefaultC = new VIDataGenerator(Example.class)
                .generateCombinations(ALL_GROUPS);
        List<Example> examplesDefaultV = new VIDataGenerator(Example.class)
                .generateValues(ALL_GROUPS);
        List<Example> examplesAll = new VIDataGenerator(Example.class)
                .generateCombinations(ALL_GROUPS);
        List<Example> examples1w = new VIDataGenerator(Example.class)
                .whiteList("subClass.Strings")
                .generateCombinations(ALL_GROUPS);
        List<Example> examples1b = new VIDataGenerator(Example.class)
                .blackList("subClass.Strings")
                .generateCombinations(ALL_GROUPS);
        List<Example> examples1w2 = new VIDataGenerator(Example.class)
                .whiteList("*.Strings")
                .generateCombinations(ALL_GROUPS);
        List<Example> examples1b2 = new VIDataGenerator(Example.class)
                .blackList("*.Strings")
                .generateCombinations(ALL_GROUPS);

        List examplesG1w = new VIDataGenerator<Example>(Example.class)
                .fieldsFilter(b -> !b.contains("subClass") || b.contains("Enums"))
                .generateCombinations(ALL_GROUPS);
        List examplesValue1 = new VIDataGenerator<Example>(Example.class)
                .dataFilter(e -> e.Enums == One && !e.subClass.Strings.equals(""))
                .generateCombinations(ALL_GROUPS);
        List examplesValue2 = new VIDataGenerator<Example>(Example.class)
                .dataFilter(e -> e.Enums != One || (e.Strings.equals("")))
                .generateCombinations(ALL_GROUPS);

        List<Example> examples2 = new VIDataGenerator(Example.class).generateValues(ALL_GROUPS);
        List<Example> examples3 = new VIDataGenerator(Example.class).generateCombinations("Test");
        return to2DArray(examples1w, Example.class);
    }

    @DataProvider(name = "someDP2")
    public static Object[][] someDP2() throws Exception {
        List<Example> examples4 = new VIDataGenerator(Example.class).generateValues("Test2");
        return to2DArray(examples4, Example.class);
    }
}
