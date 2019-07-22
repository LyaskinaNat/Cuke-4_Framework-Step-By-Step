package stepDefinitions;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import java.util.Locale;
import java.util.Map;
import testDataTypes.*;

public class Configurer implements TypeRegistryConfigurer {
    @Override
    public void configureTypeRegistry(TypeRegistry registry) {

        registry.defineDataTableType(new DataTableType(CustomerDataType.class, new TableEntryTransformer<CustomerDataType>() {
            @Override
            public CustomerDataType transform(Map<String, String> entry) {
                return CustomerDataType.customerDetails(entry);
            }
        }));
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }
}
