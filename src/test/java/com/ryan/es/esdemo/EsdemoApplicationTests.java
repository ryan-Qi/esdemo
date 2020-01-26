package com.ryan.es.esdemo;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@SpringBootTest
class EsdemoApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    void contextLoads() {
        System.out.println(jestClient);
    }

    @Test
    void index() throws IOException {
        //Update Delete Search Index

        User user = new User();
        user.setEmail("122222");
        user.setUserName("ryan");

        Index index = new Index.Builder(user).index("user").type("info").build();

        DocumentResult execute = jestClient.execute(index);

        System.out.println("执行？"+execute.getId()+"===>"+execute.isSucceeded());
    }

    @Test
    public void query() throws IOException {
        String queryJson = "{" +
                "  \"query\": {" +
                "    \"match_all\": {}" +
                "  }" +
                "}";
        Search user = new Search.Builder(queryJson).addIndex("user").build();

        SearchResult execute = jestClient.execute(user);

        System.out.println("总记录"+ execute.getTotal()+"===>最大得分："+execute.getMaxScore());

        List<SearchResult.Hit<User, Void>> hits = execute.getHits(User.class);


        hits.forEach(hit->{
            User u = hit.source;
            System.out.println(u.getEmail()+"====>"+u.getUserName());
        });
    }
}

class User{
    private String userName;
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
