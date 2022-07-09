package com.zamron.mysql;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zamron.GameSettings;


@SuppressWarnings({ "rawtypes" })
public class MySQLController {

    /*DATABASES = new MySQLDatabase[] {
        new MySQLDatabase("ip",2222, "db user", "db pass", "db name");
    };*/


    public static final ExecutorService SQL_SERVICE = Executors.newSingleThreadExecutor();

    public static void toggle() {
        if (GameSettings.MYSQL_ENABLED) {
            MySQLProcessor.terminate();
            CONTROLLER = null;
            DATABASES = null;
            GameSettings.MYSQL_ENABLED = false;
        } else if (!GameSettings.MYSQL_ENABLED) {
            init();
            GameSettings.MYSQL_ENABLED = true;
        }
    }

    private static MySQLController CONTROLLER;

    public static void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        CONTROLLER = new MySQLController();
    }

    public static MySQLController getController() {
        return CONTROLLER;
    }

    public enum Database {
        HIGHSCORES,
    }


    private static MySQLDatabase[] DATABASES = new MySQLDatabase[2];

    public MySQLDatabase getDatabase(Database database) {
        return DATABASES[database.ordinal()];
    }


    public MySQLController() {

        DATABASES = new MySQLDatabase[]{


        };
        MySQLProcessor.process();
    }

    private static class MySQLProcessor {

        private static boolean running;

        private static void terminate() {
            running = false;
        }

        public static void process() {
            if (running) {
                return;
            }
            running = true;
            SQL_SERVICE.submit(new Runnable() {
                public void run() {
                    try {
                        while (running) {
                            if (!GameSettings.MYSQL_ENABLED) {
                                terminate();
                                return;
                            }
                            for (MySQLDatabase database : DATABASES) {

                                if (!database.active) {
                                    continue;
                                }

                                if (database.connectionAttempts >= 5) {
                                    database.active = false;
                                }

                                Connection connection = database.getConnection();
                                try {
                                    connection.createStatement().execute(" SELECT 1");
                                } catch (Exception e) {
                                    database.createConnection();
                                }
                            }
                            Thread.sleep(25000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
