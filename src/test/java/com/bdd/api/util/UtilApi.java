package com.bdd.api.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import net.serenitybdd.core.Serenity;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.mail.BodyPart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilApi {
    private static final String SOURCE_CLASS = UtilApi.class.getName();

    public UtilApi() {
    }

    public static void main(String[] args) {
    }

    public static String getValueFromDataTable(DataTable dataTable, String title) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return (String)((Map)list.get(0)).get(title);
    }

    public static String updateValueOfNodeInJson(String jsonString, String keyPath, String value) {
        Configuration configuration = Configuration.builder().jsonProvider(new JsonOrgJsonProvider()).mappingProvider(new JsonOrgMappingProvider()).build();
        return JsonPath.using(configuration).parse(jsonString).set("$." + keyPath, value, new Predicate[0]).jsonString();
    }

    public static File getLastFileCreated(String filePath, String serviceName, String format) {
        File theNewestFile = null;
        File dir = new File(filePath + serviceName + "/" + format + "/");
        logger(UtilApi.class).log(Level.INFO, "Direccion absoluta {}", dir.getAbsolutePath());
        FileFilter fileFilter = new WildcardFileFilter("*." + format);
        File[] files = dir.listFiles(fileFilter);
        if (files.length > 0) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
        }

        return theNewestFile;
    }

    public static String readAllFromFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        String scriptPath = file.getAbsolutePath();
        List<String> allLines = Files.readAllLines(Paths.get(scriptPath));
        Iterator var4 = allLines.iterator();

        while(var4.hasNext()) {
            String line = (String)var4.next();
            sb.append(line);
        }

        return sb.toString();
    }

    public static String verifySeedToSend(@Nullable String firstSeedOption, String defaultAndValidSeedOption) {
        return firstSeedOption != null ? firstSeedOption : defaultAndValidSeedOption.replace("\"", "");
    }

    public static String readLineOfFile(String fileTXT) throws IOException {
        String outPutLine = "";
        String scriptPath = (new File(fileTXT)).getAbsolutePath();
        List<String> allLines = Files.readAllLines(Paths.get(scriptPath));
        Iterator var4 = allLines.iterator();

        while(var4.hasNext()) {
            String line = (String)var4.next();
            if (!line.equals("") && !line.isEmpty()) {
                outPutLine = line;
                break;
            }
        }

        return outPutLine;
    }

    public static String getJsonFromAttribute(String json, String nodo) {
        StringBuilder value = new StringBuilder();

        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject)parser.parse(json);
            value.append(jsonObject.get(nodo).getAsString());
        } catch (Exception var5) {
            logger(UtilApi.class).throwing(SOURCE_CLASS, "getJsonFromAttribute()", var5);
        }

        return value.toString();
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int partCount = mimeMultipart.getCount();

        for(int i = 0; i < partCount; ++i) {
            MimeBodyPart bodyPart = mimeMultipart.getBodyPart(i);
            String html;
            if (bodyPart.isMimeType("text/plain")) {
                html = "\n" + bodyPart.getContent();
                result.append(html);
                break;
            }

            if (bodyPart.isMimeType("text/html")) {
                html = (String)bodyPart.getContent();
                result.append(html);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent()));
            }
        }

        return result.toString();
    }

    public static boolean isRUCValid(long ruc) {
        return isRUCValid(String.valueOf(ruc));
    }

    public static boolean isRUCValid(String ruc) {
        if (ruc == null) {
            return false;
        } else {
            int[] multipliers = new int[]{5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
            String[] prefixes = getRucPrefixes();
            int length = multipliers.length + 1;
            if (ruc.length() != length) {
                return false;
            } else {
                boolean isPrefixOk = false;
                String[] var5 = prefixes;
                int i = prefixes.length;

                for(int var7 = 0; var7 < i; ++var7) {
                    String prefix = var5[var7];
                    if (ruc.substring(0, 2).equals(prefix)) {
                        isPrefixOk = true;
                        break;
                    }
                }

                if (!isPrefixOk) {
                    return false;
                } else {
                    int sum = 0;

                    for(i = 0; i < multipliers.length; ++i) {
                        char section = ruc.charAt(i);
                        if (!Character.isDigit(section)) {
                            return false;
                        }

                        sum += Character.getNumericValue(ruc.charAt(i)) * multipliers[i];
                    }

                    i = sum % length;
                    String response = String.valueOf(length - i);
                    return response.charAt(response.length() - 1) == ruc.charAt(ruc.length() - 1);
                }
            }
        }
    }

    public static String[] getRucPrefixes() {
        return new String[]{"10", "15", "17", "20"};
    }

    public static String readFileByFormatBracket(String nodo, File servicePath) throws IOException {
        String value = "";

        try {
            Reader reader = new FileReader(servicePath);
            Throwable var4 = null;

            try {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject)parser.parse(reader);
                value = jsonObject.get(nodo).getAsString();
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (reader != null) {
                    if (var4 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (IOException var17) {
            logger(UtilApi.class).throwing(SOURCE_CLASS, "readFileByFormatBracket()", var17);
        }

        return value;
    }

    public static void saveVariableOnSession(String key, Object value) {
        Serenity.setSessionVariable(key).to(value);
    }

    public static <T> T getVariableOnSession(String key) {
        return Serenity.sessionVariableCalled(key);
    }

    public static void saveAPIEvidenceCucumberReport(Object[] objects, Scenario scenario) {
        QueryableRequestSpecification requestSpecification = (QueryableRequestSpecification)objects[0];
        Response responseBody = (Response)objects[1];
        String division = "**************************************************************";
        String scenarioDetail = "Escenario: " + scenario.getName() + "\nStatus: " + scenario.getStatus() + "\n";
        String request = "API_URL Request: \n" + division + "\nRequest method: " + requestSpecification.getMethod() + "\nRequest URI:" + requestSpecification.getURI() + "\nCookies:" + requestSpecification.getCookies() + "\nContent-Type: " + requestSpecification.getContentType() + "\nHeaders: " + requestSpecification.getHeaders() + "\nPathParams: " + requestSpecification.getPathParams().isEmpty() + "\nFormParams: " + requestSpecification.getFormParams() + "\nQueryParams: " + requestSpecification.getQueryParams() + "\nRequestParams: " + requestSpecification.getRequestParams() + "\nBody: " + requestSpecification.getBody() + "\n";
        String response = "API_URL Response: \n" + division + "\nStatusCode: " + responseBody.getStatusLine() + "\nHeaders: " + responseBody.getHeaders() + "\nCookies: " + responseBody.getCookies() + "\nTimeSeconds: " + responseBody.getTime() + "\nSessionID: " + responseBody.getSessionId() + "\nResponse: " + responseBody.getBody().prettyPrint();
        String evidence = scenarioDetail + "\n" + division + "\n" + request + "\n" + division + "\n" + response;
        logger(UtilApi.class).info(evidence);
        scenario.embed(evidence.getBytes(), "application/json");
    }

    public static String getCurrentDateWithFormat(String format) {
        return (new SimpleDateFormat(format)).format(Calendar.getInstance().getTime());
    }

    public static Logger logger(@NotNull Class clase) {
        return Logger.getLogger(clase.getName());
    }
}
