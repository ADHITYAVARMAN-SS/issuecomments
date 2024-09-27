package com.gitlabdemo.issuecomments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gitlab")
public class GitLabCommentController {

    @Autowired
    private GitLabCommentService gitLabCommentService;
    private final GitLabIssueService gitLabIssueService;

    public GitLabCommentController(GitLabIssueService gitLabIssueService) {
        this.gitLabIssueService = gitLabIssueService;
    }
    // add isssue to gitlab
     @PostMapping("/create-issue")
    public ResponseEntity<String> createIssue(@RequestParam String projectId, @RequestParam String title, @RequestParam String description) {
        String response = gitLabIssueService.createIssue(projectId, title, description);
        return ResponseEntity.ok(response);
    }

    // get issues from gitlab
    @GetMapping("/get-issues")
    public ResponseEntity<String> getIssues(@RequestParam String projectId) {
        String response = gitLabIssueService.getIssues(projectId);
        return ResponseEntity.ok(response);
    }
    
    // Add comment with an attachment   
    @PostMapping("/issues/{issueId}/comment-attachment")
    public String addCommentAndAttachment(@PathVariable String issueId, @RequestParam String comment, @RequestParam String filePath) {
        try {
            gitLabCommentService.addCommentAndAttachment(issueId, comment, filePath);
            return "Comment and attachment added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add comment: " + e.getMessage();
        }
    }

}