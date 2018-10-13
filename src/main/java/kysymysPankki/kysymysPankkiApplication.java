package kysymysPankki;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;
import kysymysPankki.db.QuestionDao;
import kysymysPankki.resources.QuestionResource;

import java.sql.SQLException;

public class kysymysPankkiApplication extends Application<kysymysPankkiConfiguration> {

    public static void main(final String[] args) throws Exception {
        new kysymysPankkiApplication().run(args);
    }

    @Override
    public String getName() {
        return "kysymysPankki";
    }

    @Override
    public void initialize(final Bootstrap<kysymysPankkiConfiguration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(new AssetsBundle("/assets/build", "/", "index.html", "static"));
    }

    @Override
    public void run(final kysymysPankkiConfiguration configuration,
                    final Environment environment) throws SQLException {

        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String dbUser = System.getenv("DATABASE_USER");
        String dbPass = System.getenv("DATABASE_PASSWORD");

        if (dbUrl == null || dbUrl.isEmpty()) {
            dbUrl = "jdbc:postgresql://localhost:5432/pankki";
        }

        if (dbUser == null || dbUser.isEmpty()) {
            dbUser = "postgres";
        }

        if (dbPass == null || dbPass.isEmpty()) {
            dbPass = "";
        }

        QuestionDao model = new QuestionDao(dbUrl, dbUser, dbPass);

        final QuestionResource resource = new QuestionResource(
                configuration.getTemplate(),
                configuration.getDefaultName(),
                model
        );
        environment.jersey().register(resource);
    }

}
