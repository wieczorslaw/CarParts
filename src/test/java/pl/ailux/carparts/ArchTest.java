package pl.ailux.carparts;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pl.ailux.carparts");

        noClasses()
            .that()
                .resideInAnyPackage("pl.ailux.carparts.service..")
            .or()
                .resideInAnyPackage("pl.ailux.carparts.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..pl.ailux.carparts.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
