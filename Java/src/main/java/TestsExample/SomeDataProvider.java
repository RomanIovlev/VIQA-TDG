package TestsExample;

import ru.viqa.test_data_generator.generator.VIDataGenerator;
import org.testng.annotations.DataProvider;

import java.util.List;

import static TestsExample.PassportType.RUSSIAN;
import static ru.viqa.test_data_generator.generator.VIDataGenerator.to2DArray;

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
                .whiteList("passport.name")
                .generateCombinations();
        List<Example> examples1b = new VIDataGenerator<>(Example::new)
                .blackList("passport.name")
                .generateCombinations();
        List<Example> examples1w2 = new VIDataGenerator<>(Example::new)
                .whiteList("*.name")
                .generateCombinations();
        List<Example> examples1b2 = new VIDataGenerator<>(Example::new)
                .blackList("*.name")
                .generateCombinations();

        List<Example> examplesG1w = new VIDataGenerator<>(Example::new)
                .fieldsFilter(fieldName -> {
                    if (fieldName.contains("passport"))
                        return fieldName.contains("type");
                    else return true;
                })
                .generateCombinations();
        List examplesG1short = new VIDataGenerator<>(Example::new)
                .fieldsFilter(fieldName -> !fieldName.contains("passport") || fieldName.contains("type"))
                .generateCombinations();
        List examplesValue1 = new VIDataGenerator<>(Example::new)
                .dataFilter(e -> e.type == RUSSIAN && !e.passport.name.equals(""))
                .generateCombinations();
        List examplesValue2 = new VIDataGenerator<>(Example::new)
                .dataFilter(e -> e.type != RUSSIAN || (e.name.equals("")))
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
