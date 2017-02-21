package org.parabot.core.bdn.api;

import org.json.simple.parser.ParseException;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.environment.api.utils.WebUtil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author JKetelaar
 */
public final class APICaller {

    public static Object callPoint(APIPoint apiPoint) {
        return callPoint(apiPoint, null, null);
    }

    public static Object callPoint(APIPoint apiPoint, SharedUserAuthenticator authenticator) {
        return callPoint(apiPoint, authenticator, null);
    }

    public static Object callPoint(APIPoint apiPoint, String parameters) {
        return callPoint(apiPoint, null, parameters);
    }

    public static Object callPoint(APIPoint apiPoint, SharedUserAuthenticator authenticator, String parameters) {
        try {
            HttpURLConnection connection = (HttpURLConnection) WebUtil.getConnection(apiPoint.getPoint());
            if (connection != null) {
                if (authenticator != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + authenticator.getAccessToken());
                }

                connection.setRequestMethod(apiPoint.getType().getType());

                if (apiPoint.getType().isDoOutput()) {
                    connection.setDoOutput(true);

                    if (parameters != null) {
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                        wr.write(parameters);
                        wr.flush();
                        wr.close();
                    }
                }

                if (connection.getResponseCode() == 200) {
                    if (apiPoint.isJson()) {
                        return WebUtil.getJsonParser().parse(WebUtil.getReader(connection));
                    } else {
                        return connection.getInputStream();
                    }
                } else {
                    System.err.println("Response not 200, code " + connection.getResponseCode() + " instead");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public enum APIPoint {
        LIST_SERVERS(APIConfiguration.API_ENDPOINT + "servers/list", true, true, APIPointType.GET),
        DOWNLOAD_SERVER(APIConfiguration.API_ENDPOINT + "servers/download/%d", true, false, APIPointType.GET),
        GET_SERVER(APIConfiguration.API_ENDPOINT + "servers/get/%d", true, true, APIPointType.GET),

        SEND_SLACK(APIConfiguration.API_ENDPOINT + "bot/notifications/slack/send/%d", true, true, APIPointType.POST),
        IN_SLACK(APIConfiguration.API_ENDPOINT + "users/in_slack", true, true, APIPointType.GET);

        private String point;
        private boolean accessRequired;
        private boolean json;
        private APIPointType type;

        private String editedPoint;

        APIPoint(String point, boolean accessRequired, boolean json, APIPointType type) {
            this.point = point;
            this.accessRequired = accessRequired;
            this.json = json;
            this.type = type;
        }

        public boolean isJson() {
            return json;
        }

        public URL getPoint() {
            try {
                if (editedPoint != null) {
                    return new URL(editedPoint);
                } else {
                    return new URL(point);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }

        public APIPoint setPointParams(Object... pointParams) {
            this.editedPoint = String.format(this.point, pointParams);

            return this;
        }

        public APIPointType getType() {
            return type;
        }

        public boolean isAccessRequired() {
            return accessRequired;
        }

        public enum APIPointType {
            GET("GET", false),
            PUT("PUT", true),
            HEAD("HEAD", true),
            POST("POST", true),
            DELETE("DELETE", true);

            private String type;
            private boolean doOutput;

            APIPointType(String type, boolean doOutput) {
                this.type = type;
                this.doOutput = doOutput;
            }

            public String getType() {
                return type;
            }

            public boolean isDoOutput() {
                return doOutput;
            }
        }
    }
}
