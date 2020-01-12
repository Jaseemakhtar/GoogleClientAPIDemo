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
import com.google.api.services.blogger.model.Post;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;



public class GoogleLibrariesDemo {
    public static final String APP_NAME = "Google Libraries";
    public static final String BLOG_ID = "4417696787443174159";
    public static final String POST_ID = "3293781334946994615";

    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/klrt2");;

    private static Credential authorize() throws Exception {
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(GoogleLibrariesDemo.class.getResourceAsStream("../resources/apps.googleusercontent.com-demo.json"))
        );


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(BloggerScopes.BLOGGER)).setDataStoreFactory(dataStoreFactory)
                .build();

        Credential credential = flow.loadCredential("user");
        if(credential != null && credential.getAccessToken() != null){
            println("Already authenticated");
            return credential;
        }else {
            try {
                println("Not authenticated");
                LocalServerReceiver localServerReceiver = new LocalServerReceiver();
                credential = new AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user");
                return credential;
            }catch (IOException  e){
                println("Error: " + e.getMessage());
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        Credential credential = authorize();


        Blogger blogger = new Blogger.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APP_NAME).setHttpRequestInitializer(credential)
                .build();

        //Blogger.Blogs.GetByUrl getByUrl =  blogger.blogs().getByUrl(BLOG_URL);
        //Blogger.Blogs.Get get = blogger.blogs().get("4417696787443174159");
        Blogger.Posts.Get getPost = blogger.posts().get(BLOG_ID, POST_ID + "");
        println("Is Empty: " + getPost.isEmpty());

        Post post = getPost.execute();

        /*println("Blogger ID: " + blog.getId());
        println("Blogger Name: " + blog.getName());
        println("Blogger Posts: " + blog.getPosts().getTotalItems());
        println("Blogger Description: " + blog.getDescription());*/

        println("Post ID: " + post.getId());
        println("Post Name: " + post.getTitle());
        println("Post Content: " + post.getContent());



    }

    public static void println(Object o){
        System.out.println(o);
    }

}

