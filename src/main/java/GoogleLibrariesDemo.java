package main.java;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.BloggerScopes;
import com.sun.org.apache.xpath.internal.operations.Plus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;



public class GoogleLibrariesDemo {
    public static final String APP_NAME = "Google Libraries";

    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static java.io.File DATA_STORE_DIR;

    private static Credential authorize() throws Exception {
        DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/plus_sample");
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GoogleLibrariesDemo.class.getResourceAsStream("../resources/apps.googleusercontent.com-demo.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(BloggerScopes.BLOGGER)).setDataStoreFactory(dataStoreFactory)
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        // authorization
        Credential credential = authorize();
        Blogger blogger = new Blogger.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APP_NAME).setHttpRequestInitializer(credential)
                .build();

        Blogger.Blogs.GetByUrl getByUrl =  blogger.blogs().getByUrl("");

    }

}

