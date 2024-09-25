package com.gitlabdemo.issuecomments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

@Service
public class GitLabCommentService {

    private static final String GITLAB_BASE_URL = "https://gitlab.com/api/v4/projects/{projectId}/issues/{issueId}/notes";

    @Value("${gitlab.token}")
    private String privateToken;

    @Value("${gitlab.project.id}")
    private String projectId;

    public void addCommentAndAttachment(String issueId, String comment, String filePath) throws Exception {
        String commentId = addComment(issueId, comment);
        System.out.println(commentId);
    }

    public String addComment(String issueId, String comment) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("body", comment);

        HttpResponse<JsonNode> response = Unirest.post(GITLAB_BASE_URL.replace("{projectId}", projectId).replace("{issueId}", issueId))
            .header("PRIVATE-TOKEN", privateToken)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asJson();

        if (response.getStatus() != 201) { 
            throw new Exception("Failed to add comment. Status code: " + response.getStatus());
        }

        return response.getBody().getObject().getString("id");
    }
}