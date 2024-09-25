package com.gitlabdemo.issuecomments;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

@Service
public class GitLabCommentService {

    private static final String GITLAB_BASE_URL = "https://gitlab.com/api/v4/projects/{projectId}/issues/{issueId}/notes";
    private static final String GITLAB_BASE_URL1 = "https://gitlab.com/api/v4/projects/{projectId}/uploads";

    @Value("${gitlab.token}")
    private String privateToken;

    @Value("${gitlab.project.id}")
    private String projectId;

    public void addCommentAndAttachment(String issueId, String comment, String filePath) throws Exception {
        String fileUrl = uploadFile(filePath);
        
        String commentWithAttachment = comment + "\n\n![Attachment](" + fileUrl + ")";
        
        String commentId = addComment(issueId, commentWithAttachment);
        System.out.println("Comment added with ID: " + commentId);
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

    public String uploadFile(String filePath) throws Exception {
        HttpResponse<JsonNode> response = Unirest.post(GITLAB_BASE_URL1.replace("{projectId}", projectId))
            .header("PRIVATE-TOKEN", privateToken)
            .field("file", new File(filePath)) 
            .asJson();

        if (response.getStatus() != 201) {
            throw new Exception("Failed to upload file. Status code: " + response.getStatus());
        }

        JSONObject responseObject = response.getBody().getObject();
        return responseObject.getString("url");  // Returns the uploaded file URL
    }
}