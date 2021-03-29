package repository;

import exception.MyCustomException;
import lombok.extern.log4j.Log4j;
import model.Organization;
import model.OrganizationWithId;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.Constants.*;

@Log4j
public class OrganizationRepositoryImpl implements OrganizationRepository {
    private static final List<Organization> organizations = new ArrayList<>();

    @Override
    public void importFromCsv(final String filename) {
        try (final BufferedReader buffRead = Files.newBufferedReader(Paths.get(filename))) {
            String line = buffRead.readLine();
            if (StringUtils.isNotBlank(line)) {
                if(Organization.resolveProperties(line)) {
                    while ((line = buffRead.readLine()) != null) {
                        try {
                            organizations.add(new OrganizationWithId(line));
                        } catch (final MyCustomException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                else{
                    throw new MyCustomException(WRONG_FILE_COUNT);
                }
            } else {
                throw new MyCustomException(EMPTY_FILE_ERROR_MESSAGE);
            }

        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void exportToJson(final String filename) {
        if (organizations.isEmpty()) {
            return;
        }
        final StringBuilder strBuild = new StringBuilder();
        organizations.forEach(o -> strBuild.append(o.toJson()).append(NEW_LINE));
        try (final BufferedWriter br = Files.newBufferedWriter(Paths.get(filename))) {
            br.write(strBuild.toString());
        } catch (final IOException e) {
            log.error(FILE_WRITING_ERROR_MESSAGE + e.getMessage());
        }
    }

    @Override
    public void sort() {
        Collections.sort(organizations);
    }

    @Override
    public String toString() {
        return StringUtils.join(organizations, NEW_LINE);
    }
}