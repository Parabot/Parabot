package org.parabot.core.bdn.api;

import org.json.simple.JSONObject;
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

    public static Object callPoint(APIPoint apiPoint){
        return callPoint(apiPoint, null, null);
    }

    public static Object callPoint(APIPoint apiPoint, SharedUserAuthenticator authenticator){
        return callPoint(apiPoint, authenticator, null);
    }

    public static Object callPoint(APIPoint apiPoint, String parameters){
        return callPoint(apiPoint, null, parameters);
    }

    public static Object callPoint(APIPoint apiPoint, SharedUserAuthenticator authenticator, String parameters){
        try {
            HttpURLConnection connection = (HttpURLConnection) WebUtil.getConnection(apiPoint.getPoint());
            if (connection != null) {
                if (authenticator != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + authenticator.getAccessToken());
                }

                if (apiPoint.type.isDoOutput()) {
                    connection.setDoOutput(true);

                    if (parameters != null) {
                        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                        wr.write(parameters);
                        wr.flush();
                        wr.close();
                    }
                }

                return WebUtil.getJsonParser().parse(WebUtil.getReader(connection));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public enum APIPoint {
        LIST_SERVERS(APIConfiguration.API_ENDPOINT + "servers/list", true, APIPointType.GET);

        private String point;
        private boolean accessRequired;
        private APIPointType type;

        APIPoint(String point, boolean accessRequired, APIPointType type) {
            this.point = point;
            this.accessRequired = accessRequired;
            this.type = type;
        }

        public URL getPoint() {
            try {
                return new URL(point);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
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
