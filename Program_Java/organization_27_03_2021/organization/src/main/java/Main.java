import lombok.extern.log4j.Log4j;
import repository.OrganizationRepository;
import repository.OrganizationRepositoryImpl;

import java.util.Arrays;

import static config.Constants.*;

@Log4j
public class Main {

    public static void main(final String[] args) {
        if (args.length == 1) {
            OrganizationRepository repository = new OrganizationRepositoryImpl();
            repository.importFromCsv(args[0]);
            System.out.println("\nUnsorted array: ");
            System.out.println(repository.toString());
            System.out.println("\nSorted array: ");
            repository.sort();
            System.out.println(repository.toString());
            repository.exportToJson(args[0].substring(0, args[0].lastIndexOf(DOT)) + JSON);
        } else {
            log.debug(RUN_WITH_WRONG_ARGUMENTS + Arrays.toString(args));
            System.out.println(ENTER_VALID_ARGUMENT_MESSAGE);
        }
    }
}