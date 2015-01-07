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
        List<Example> examplesDefaultC = new VIDataGenerator<>(Example::new)
                .generateCombinations();
        List<Example> examplesDefaultV = new VIDataGenerator<>(Example::new)
                .generateValues();
        List<Example> examplesAll = new VIDataGenerator<>(Example::new)
                .generateCombinations();
        List<Example> examples1w = new VIDataGenerator<>(Example::new)
                .whiteList("subClass.Strings")
                .generateCombinations();
        List<Example> examples1b = new VIDataGenerator<>(Example::new)
                .blackList("subClass.Strings")
                .generateCombinations();
        List<Example> examples1w2 = new VIDataGenerator<>(Example::new)
                .whiteList("*.Strings")
                .generateCombinations();
        List<Example> examples1b2 = new VIDataGenerator<>(Example::new)
                .blackList("*.Strings")
                .generateCombinations();

        List examplesG1w = new VIDataGenerator<>(Example::new)
                .fieldsFilter(b -> !b.contains("subClass") || b.contains("Enums"))
                .generateCombinations();
        List examplesValue1 = new VIDataGenerator<>(Example::new)
                .dataFilter(e -> e.Enums == One && !e.subClass.Strings.equals(""))
                .generateCombinations();
        List examplesValue2 = new VIDataGenerator<>(Example::new)
                .dataFilter(e -> e.Enums != One || (e.Strings.equals("")))
                .generateCombinations();

        List<Example> examples2 = new VIDataGenerator<>(Example::new).generateValues();
        List<Example> examples3 = new VIDataGenerator<>(Example::new).generateCombinations("Test");
        return to2DArray(examples1w, Example.class);
    }

    @DataProvider(name = "someDP2")
    public static Object[][] someDP2() throws Exception {
        List<Example> examples4 = new VIDataGenerator<>(Example::new).generateValues("Test2");
        return to2DArray(examples4, Example.class);
    }
}
