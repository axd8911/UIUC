package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOVBeanLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@DisplayName("Should pass Obstetric Bean Loader Test's using the method parameters provided by " +
        "the data provider() method")
public class ObstetricBeanLoaderTest {

    /**********************************Test Runners****************************************/
    @DisplayName("Should handle exception's for invalid arguments")
    @ParameterizedTest(name = "{index} => ps={0}, bean={1}")
    @ArgumentsSource(InvalidArgumentsProvider.class)
    void testBeanLoaderForInvalidInputs(PreparedStatement ps, ObstetricOVBean bean) {
        ObstetricOVBeanLoader beanLoader = new ObstetricOVBeanLoader();
        Assertions.assertThrows(IllegalStateException.class,
                () -> beanLoader.loadParameters(ps, bean));
        try {
            Assertions.assertTrue(beanLoader.loadSingle(null) == null);
            Assertions.assertTrue(ObstetricOVBeanLoader.beanBuilder(null) == null);
        } catch (SQLException sqe  ) {} catch (FormValidationException formValidationException) {}
    }

    @DisplayName("Should return valid obstetric bean object")
    @ParameterizedTest(name = "{index} => map={0}")
    @ArgumentsSource(BeanBuilderArguments.class)
    void testBeanLoaderBeanBuilder(Map<String, String[]> map) {
        ObstetricOVBeanLoader beanLoader = new ObstetricOVBeanLoader();
        try {
            Assertions.assertTrue(ObstetricOVBeanLoader.beanBuilder(map) != null);
        } catch (FormValidationException formValidationException) {}
    }

    /**********************************Argument providers************************************/
    static class InvalidArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(null, null)
            );
        }
    }

    static class BeanBuilderArguments implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            Map<String, String[]> map1 = new HashMap<>();
            map1.put("numDaysPreg",new String[]{"20"});
            map1.put("fhr",new String[]{"20"});
            map1.put("multiple",new String[]{"20"});
            map1.put("weight",new String[]{"20"});

            Map<String, String[]> map2 = new HashMap<>();
            map2.put("numDaysPreg",new String[]{"123"});
            map2.put("fhr",new String[]{"1100"});
            map2.put("multiple",new String[]{"10"});
            map2.put("weight",new String[]{"44"});

            Map<String, String[]> map3 = new HashMap<>();
            map3.put("numDaysPreg",new String[]{"20.22"});
            map3.put("fhr",new String[]{"20.12"});
            map3.put("multiple",new String[]{"20.21"});
            map3.put("weight",new String[]{"20.32"});

            Map<String, String[]> map4 = new HashMap<>();
            map4.put("numDaysPreg",new String[]{"20"});
            map4.put("fhr",new String[]{"20"});
            map4.put("multiple",new String[]{"20"});
            map4.put("weight",new String[]{"20"});

            return Stream.of(
                    Arguments.of(map1),
                    Arguments.of(map2),
                    Arguments.of(map3),
                    Arguments.of(map4)
            );
        }
    }
}
