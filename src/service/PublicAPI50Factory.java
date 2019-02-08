/*
 * Pricer Server Software
 *
 * Confidential Property of Pricer AB (publ). Copyright © 1998-2018 Pricer AB (publ),
 * Box 215,Västra Järnvägsgatan 7, SE-101 24 Stockholm, Sweden. All rights reserved.
 */
package service;

import ws.PricerPublicAPI50;
import ws.PricerPublicAPI50_Service;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static service.myTools.*;

public class PublicAPI50Factory {

  private static final String AUTHENTICATION_TOKEN_HEADER = "authentication-token";
  private static final String USERNAME_HEADER = "username";

//  private static String IP_ADDRESS = "10.0.2.128";
//  private static String PORT = "11097";
  private static String version;
  private static String API_KEY;
  private static String API_USER;
  private static String PORT;
  private static String IP_ADDRESS;
  private static String Page2Flash;

  private static Date lastUsed =null;
  private static PricerPublicAPI50 connection;

  public static PricerPublicAPI50 getConnection() throws FileNotFoundException {

      version = getAppVersion();
      IP_ADDRESS=getIp();
      PORT=getPort();
      API_KEY=getUserAPIKey();
      API_USER=getUserName();

//    private static String API_USER = ;
//    private static String API_KEY = "scoxS020_jNzb5tk2aScjGqK";

    if (connection == null || lastUsed.getTime() + 290000 < new Date().getTime()) {
      connection = newConnection(API_USER, API_KEY, IP_ADDRESS, PORT);
    }
    lastUsed = new Date();
    return connection;
  }

  private static PricerPublicAPI50 newConnection(String apiUser, String apiKey, String ipNum, String port) {
      String WS_URL = "http://" + ipNum + ":" + port + "/pricer_5_0?wsdl";
      try {
          PricerPublicAPI50_Service service = new PricerPublicAPI50_Service(new URL(WS_URL), new QName(
                  "http://public_5_0.interface.pricer.se/", "PricerPublicAPI_5_0"));
          PricerPublicAPI50 api50 = service.getPricerPublicAPI50Port();

          String loginChallenge = api50.getLoginChallenge(apiUser);

          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          digest.update(loginChallenge.getBytes(Charset.forName("UTF-8")));
          digest.update(apiKey.getBytes(Charset.forName("UTF-8")));
          String hash = Base64.getEncoder().encodeToString(digest.digest());

          Map<String, Object> req_ctx = ((BindingProvider) api50).getRequestContext();
          req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

          Map<String, List<String>> headers = new HashMap<>();
          headers.put(USERNAME_HEADER, Collections.singletonList(apiUser));
          headers.put(AUTHENTICATION_TOKEN_HEADER, Collections.singletonList(hash));
          req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

//          System.out.println(StringFormatter.format("Found server started at %s:%s with API user \"%s\" and API key \"%s\".",
//                  IP_ADDRESS, PORT, API_USER, API_KEY).getValue());
//          System.out.println("Found server started at %s:%s with API user \"%s\" and API key \"%s\".");
//          System.out.println(IP_ADDRESS + PORT + API_USER + API_KEY);
          return api50;
      } catch (MalformedURLException | NoSuchAlgorithmException e) {
          Logger.getLogger(PublicAPI50Factory.class.getName()).log(Level.SEVERE, null, e);
          return null;
      }

  }
}
