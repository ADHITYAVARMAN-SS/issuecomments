package com.gitlabdemo.issuecomments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JiraIssueService {

    private static final String JIRA_API_URL = "http://localhost:8080/rest/api/2/issue";

    @Value("${jira.username}")
    private String username;

    @Value("${jira.password}")
    private String jiraPassword;

    private final RestTemplate restTemplate;

    public JiraIssueService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String createIssueInJira(String title, String description) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(("username:jiraPassword").getBytes()));

        Map<String, Object> body = new HashMap<>();
        body.put("fields", Map.of(
            "project", Map.of("key", "IS"),
            "summary", title,
            "description", description,
            "issuetype", Map.of("name", "Task")
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            JIRA_API_URL,
            HttpMethod.POST,
            entity,
            String.class
        );

        return response.getBody();
    }
}

